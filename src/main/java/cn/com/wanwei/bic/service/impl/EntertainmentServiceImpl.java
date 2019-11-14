package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.EntertainmentMapper;
import cn.com.wanwei.bic.mapper.ExtendMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntertainmentModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.EntertainmentService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - EntertainmentServiceImpl 休闲娱乐管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class EntertainmentServiceImpl implements EntertainmentService {

    @Autowired
    private EntertainmentMapper  entertainmentMapper;

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

    @Autowired
    private ExtendMapper extendMapper;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size,filter, Sort.Direction.DESC, "weight", "created_date");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        Page<EntertainmentEntity> entertainmentEntities = entertainmentMapper.findByPage(filter);
        PageInfo<EntertainmentEntity> pageInfo = new PageInfo<>(entertainmentEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        EntertainmentEntity entertainmentEntity = entertainmentMapper.selectByPrimaryKey(id);
        if (entertainmentEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
        }
        return ResponseMessage.defaultResponse().setData(entertainmentEntity);
    }

    @Override
    public ResponseMessage create(EntertainmentModel entertainmentModel, User user, Long ruleId, Integer appCode) {
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode =coderServiceFeign.buildSerialByCode(ruleId, appCode, entertainmentModel.getJpin());
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            EntertainmentEntity entertainmentEntity= entertainmentModel.getEntertainmentEntity();
            String id=UUIDUtils.getInstance().getId();
            entertainmentEntity.setId(id);
            entertainmentEntity.setCode(responseMessageGetCode.getData().toString());
            entertainmentEntity.setStatus(0);
            entertainmentEntity.setWeight(0);
            entertainmentEntity.setCreatedUser(user.getUsername());
            entertainmentEntity.setCreatedDate(new Date());
            entertainmentEntity.setDeptCode(user.getOrg().getCode());
            entertainmentMapper.insert(entertainmentEntity);
            this.saveTags(entertainmentModel.getList(),id,user);
            // 解析富文本中的附件并保存
            materialService.saveByDom(entertainmentEntity.getContent(), entertainmentEntity.getId(), user);
            return ResponseMessage.defaultResponse().setMsg("保存成功!").setData(id);
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage update(String id, EntertainmentModel entertainmentModel, User user) {
        EntertainmentEntity eEntity = entertainmentMapper.selectByPrimaryKey(id);
        if (eEntity != null) {
            EntertainmentEntity entertainmentEntity= entertainmentModel.getEntertainmentEntity();
            entertainmentEntity.setId(eEntity.getId());
            entertainmentEntity.setCreatedUser(eEntity.getCreatedUser());
            entertainmentEntity.setCreatedDate(eEntity.getCreatedDate());
            entertainmentEntity.setStatus(0);
            entertainmentEntity.setCode(eEntity.getCode());
            entertainmentEntity.setUpdatedUser(user.getUsername());
            entertainmentEntity.setUpdatedDate(new Date());
            entertainmentMapper.updateByPrimaryKey(entertainmentEntity);
            this.saveTags(entertainmentModel.getList(),id,user);
            // 先删除关联的附件再解析富文本中的附件并保存
            materialService.deleteByPrincipalId(id);
            materialService.saveByDom(entertainmentEntity.getContent(), id, user);
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
    }

    private void saveTags(List<Map<String, Object>> tagsList, String principalId, User user){
        List<BaseTagsEntity> list = Lists.newArrayList();
        for(int i=0; i<tagsList.size(); i++){
            BaseTagsEntity entity = new BaseTagsEntity();
            entity.setTagCatagory(tagsList.get(i).get("tagCatagory").toString());
            entity.setTagName(tagsList.get(i).get("tagName").toString());
            list.add(entity);
        }
        tagsService.batchInsert(principalId,list,user, EntertainmentTagsEntity.class);
    }

    @Override
    public ResponseMessage goWeight(WeightModel weightModel,User user) {
        //查出最大权重
        Integer maxNum= entertainmentMapper.maxWeight();
        List<String>ids =weightModel.getIds();
        if(ids!=null&&!ids.isEmpty()){
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if(weightModel.isFlag()||(maxNum+ids.size())>Integer.MAX_VALUE){
                entertainmentMapper.clearWeight();
                maxNum=0;
            }
            for(int i=0;i<ids.size();i++){
                EntertainmentEntity entertainmentEntity = entertainmentMapper.selectByPrimaryKey(ids.get(i));
                entertainmentEntity.setWeight(maxNum+ids.size()-i);
                entertainmentEntity.setUpdatedUser(user.getUsername());
                entertainmentEntity.setUpdatedDate(new Date());
                entertainmentMapper.updateByPrimaryKey(entertainmentEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            EntertainmentEntity entertainmentEntity = entertainmentMapper.checkTitle(title);
            if (entertainmentEntity != null) {
                if (!entertainmentEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) {
        EntertainmentEntity eEntity = entertainmentMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (eEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
        }
        String msg="审核成功！";
        if(type==1){
            //上线
            if(auditLogEntity.getPreStatus()==0||auditLogEntity.getPreStatus()==2){
                return ResponseMessage.validFailResponse().setMsg("当前信息未审核通过，不可上下线操作！");
            }else {
                if(auditLogEntity.getStatus()==1||auditLogEntity.getStatus()==9){
                    msg=auditLogEntity.getStatus()==1?"下线成功！":"上线成功！";
                }else{
                    return ResponseMessage.validFailResponse().setMsg("上下线状态错误！");
                }
            }
        }else {
            //审核
            if(auditLogEntity.getStatus()==9){
                return ResponseMessage.validFailResponse().setMsg("审核状态错误！");
            }
        }
        eEntity.setStatus(auditLogEntity.getStatus());
        eEntity.setUpdatedUser(user.getUsername());
        eEntity.setUpdatedDate(new Date());
        entertainmentMapper.updateByPrimaryKey(eEntity);
        auditLogEntity.setType(type);
        auditLogService.create(auditLogEntity,user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public ResponseMessage delete(String id) {
        entertainmentMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功！");
    }

    @Override
    public void dataBind(String updatedUser, String updatedDate, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        entertainmentMapper.dataBind(updatedUser, updatedDate, deptCode, ids);
         extendMapper.dataBindExtend(updatedUser, updatedDate, deptCode,ids);
    }

    @Override
    public ResponseMessage relateTags(String id, List<Map<String, Object>> list, User user) {
        this.saveTags(list,id,user);
        return ResponseMessage.defaultResponse().setMsg("关联成功！");
    }
}
