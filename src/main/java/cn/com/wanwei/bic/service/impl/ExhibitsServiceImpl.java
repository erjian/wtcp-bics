package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.*;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.ExhibitsService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RefreshScope
public class ExhibitsServiceImpl implements ExhibitsService {
    @Autowired
    private ExhibitsMapper exhibitsMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Value("${wtcp.bic.appCode}")
    protected Integer appId;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title", "subTitle");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        Page<ExhibitsEntity> exhibitsEntities = exhibitsMapper.findByPage(filter);
        PageInfo<ExhibitsEntity> pageInfo = new PageInfo<>(exhibitsEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        ExhibitsEntity exhibitsEntity = exhibitsMapper.selectByPrimaryKey(id);
        if (exhibitsEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该展品信息！");
        }
        return ResponseMessage.defaultResponse().setData(exhibitsEntity);
    }

    @Override
    public ResponseMessage create(EntityTagsModel<ExhibitsEntity> exhibitsModel, User user, Long ruleId, Integer appCode) {
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode = coderServiceFeign.buildSerialByCode(ruleId, appCode, exhibitsModel.getType());
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            ExhibitsEntity exhibitsEntity = exhibitsModel.getEntity();
            String id = UUIDUtils.getInstance().getId();
            exhibitsEntity.setId(id);
            exhibitsEntity.setCode(responseMessageGetCode.getData().toString());
            exhibitsEntity.setStatus(0);
            exhibitsEntity.setWeight(0);
            exhibitsEntity.setCreatedUser(user.getUsername());
            exhibitsEntity.setCreatedDate(new Date());
            exhibitsEntity.setUpdatedDate(new Date());
            exhibitsEntity.setDeptCode(user.getOrg().getCode());
            exhibitsEntity.setFullSpell(PinyinUtils.getPingYin(exhibitsEntity.getTitle()).toLowerCase());
            exhibitsEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(exhibitsEntity.getTitle()).toLowerCase());
            exhibitsMapper.insert(exhibitsEntity);

            //处理标签
            if (CollectionUtils.isNotEmpty(exhibitsModel.getTagsList())) {
                tagsService.batchInsert(id, exhibitsModel.getTagsList(), user, ExhibitsTagsEntity.class);
            }
            //处理编辑页面新增素材
            if(CollectionUtils.isNotEmpty(exhibitsModel.getMaterialList())){
                materialService.batchInsert(id,exhibitsModel.getMaterialList(),user);
            }

            return ResponseMessage.defaultResponse().setMsg("保存成功!").setData(id);
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage update(String id, EntityTagsModel<ExhibitsEntity> exhibitsModel, User user) {
        ExhibitsEntity eEntity = exhibitsMapper.selectByPrimaryKey(id);
        if (eEntity != null) {
            ExhibitsEntity exhibitsEntity = exhibitsModel.getEntity();
            exhibitsEntity.setId(eEntity.getId());
            exhibitsEntity.setCreatedUser(eEntity.getCreatedUser());
            exhibitsEntity.setCreatedDate(eEntity.getCreatedDate());
            exhibitsEntity.setStatus(0);
            exhibitsEntity.setCode(eEntity.getCode());
            exhibitsEntity.setDeptCode(eEntity.getDeptCode());
            exhibitsEntity.setUpdatedUser(user.getUsername());
            exhibitsEntity.setUpdatedDate(new Date());
            exhibitsMapper.updateByPrimaryKey(exhibitsEntity);

            //处理标签
            if (CollectionUtils.isNotEmpty(exhibitsModel.getTagsList())) {
                tagsService.batchInsert(id, exhibitsModel.getTagsList(), user, ExhibitsTagsEntity.class);
            }
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该展品信息！");
    }

    @Override
    public ResponseMessage goWeight(WeightModel weightModel, User user) {
        //查出最大权重
        Integer maxNum = exhibitsMapper.maxWeight();
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {
                exhibitsMapper.clearWeight();
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                ExhibitsEntity exhibitsEntity = exhibitsMapper.selectByPrimaryKey(ids.get(i));
                exhibitsEntity.setWeight(maxNum + ids.size() - i);
                exhibitsEntity.setUpdatedUser(user.getUsername());
                exhibitsEntity.setUpdatedDate(new Date());
                exhibitsMapper.updateByPrimaryKey(exhibitsEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            ExhibitsEntity exhibitsEntity = exhibitsMapper.checkTitle(title);
            if (exhibitsEntity != null) {
                if (!exhibitsEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) {
        ExhibitsEntity eEntity = exhibitsMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (eEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该展品信息！");
        }
        String msg = "审核成功！";
        if (type == 1) {
            //上线
            if (auditLogEntity.getPreStatus() == 0 || auditLogEntity.getPreStatus() == 2) {
                return ResponseMessage.validFailResponse().setMsg("请先审核通过后，再进⾏上线操作！");
            } else {
                if (auditLogEntity.getStatus() == 1 || auditLogEntity.getStatus() == 9) {
                    msg = auditLogEntity.getStatus() == 1 ? "下线成功！" : "上线成功！";
                } else {
                    return ResponseMessage.validFailResponse().setMsg("上下线状态错误！");
                }
            }
        } else {
            //审核
            if (auditLogEntity.getStatus() == 9) {
                return ResponseMessage.validFailResponse().setMsg("审核状态错误！");
            }
        }
        eEntity.setStatus(auditLogEntity.getStatus());
        eEntity.setUpdatedUser(user.getUsername());
        eEntity.setUpdatedDate(new Date());
        exhibitsMapper.updateByPrimaryKey(eEntity);
        auditLogEntity.setType(type);
        auditLogService.insert(auditLogEntity, user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public ResponseMessage delete(String id) {
        exhibitsMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功！");
    }

    @Override
    public void dataBind(String updatedUser, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        exhibitsMapper.dataBind(updatedUser, new Date(), deptCode, ids);
    }

    @Override
    public ResponseMessage relateTags(String id, List<BaseTagsEntity> list, User user) {
        if (CollectionUtils.isNotEmpty(list)) {
            tagsService.batchInsert(id, list, user, ExhibitsTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("关联成功！");
    }

    @Override
    public ResponseMessage getExhibitsList() {
        return ResponseMessage.defaultResponse().setData(exhibitsMapper.getExhibitsList());
    }
}
