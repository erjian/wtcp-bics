/**
 * 该源代码文件 ScenicServiceImpl.java 是工程“wtcp-bics”的一部分。
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:53:22
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.entity.ScenicTagsEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.ScenicMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.ScenicModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - ScenicServiceImpl 景区基础信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class ScenicServiceImpl implements ScenicService {

    @Autowired
    private ScenicMapper scenicMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private MaterialService materialService;

    @Override
    public ResponseMessage save(ScenicModel scenicModel, User user, Long ruleId, Integer appCode) {
        ScenicEntity record = scenicModel.getScenicEntity();
        String type = scenicModel.getType();
        record.setId(UUIDUtils.getInstance().getId());
        ResponseMessage result = coderServiceFeign.buildSerialByCode(ruleId, appCode, type);
        record.setCode(result.getData().toString());
        record.setTitleQp(PinyinUtils.getPingYin(record.getTitle()).toLowerCase());
        record.setTitleJp(PinyinUtils.converterToFirstSpell(record.getTitle()).toLowerCase());
        record.setCreatedUser(user.getUsername());
        record.setCreatedDate(new Date());
        record.setStatus(0);
        scenicMapper.insert(record);
        this.saveTags(scenicModel.getList(), record.getId(), user);

        // 解析富文本中的附件并保存
        materialService.saveByDom(record.getContent(), record.getId(), user);

        return ResponseMessage.defaultResponse().setMsg("保存成功");
    }

    @Override
    public ResponseMessage deleteByPrimaryKey(String id) {
        scenicMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ScenicEntity selectByPrimaryKey(String id) {
        return scenicMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResponseMessage edit(String id, ScenicModel scenicModel, User user) {
        ScenicEntity entity = scenicMapper.selectByPrimaryKey(id);
        ScenicEntity record = scenicModel.getScenicEntity();
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("不存在该景区");
        }
        record.setId(id);
        record.setCode(entity.getCode());
        record.setCreatedDate(entity.getCreatedDate());
        record.setCreatedUser(entity.getCreatedUser());
        record.setDeptCode(entity.getDeptCode());
        record.setTitleQp(PinyinUtils.getPingYin(record.getTitle()).toLowerCase());
        record.setTitleJp(PinyinUtils.converterToFirstSpell(record.getTitle()).toLowerCase());
        record.setStatus(0);
        record.setUpdatedDate(new Date());
        record.setUpdatedUser(user.getUsername());
        scenicMapper.updateByPrimaryKeyWithBLOBs(record);
        this.saveTags(scenicModel.getList(), id, user);

        // 先删除关联的附件再解析富文本中的附件并保存
        materialService.deleteByPrincipalId(id);
        materialService.saveByDom(record.getContent(), id, user);

        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    private void saveTags(List<Map<String, Object>> tagsList, String principalId, User user) {
        List<BaseTagsEntity> list = Lists.newArrayList();
        for (int i = 0; i < tagsList.size(); i++) {
            BaseTagsEntity entity = new BaseTagsEntity();
            entity.setTagCatagory(tagsList.get(i).get("tagCatagory").toString());
            entity.setTagName(tagsList.get(i).get("tagName").toString());
            list.add(entity);
        }
        tagsService.batchInsert(principalId, list, user, ScenicTagsEntity.class);
    }

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, User user1, Map<String, Object> filter) {
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, Sort.Direction.DESC, "created_date", "updated_date");
        Page<ScenicEntity> scenicEntities = scenicMapper.findByPage(filter);
        PageInfo<ScenicEntity> pageInfo = new PageInfo<>(scenicEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage dataBind(String updatedUser, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        scenicMapper.dataBind(updatedUser, new Date(), deptCode, ids);
        return ResponseMessage.defaultResponse().setMsg("关联机构成功");
    }

    @Override
    public ResponseMessage changeWeight(WeightModel weightModel, User user) {
        //查出最大权重
        Integer maxNum = scenicMapper.maxWeight();
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {
                scenicMapper.clearWeight();
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(ids.get(i));
                scenicEntity.setWeight(maxNum + ids.size() - i);
                scenicEntity.setUpdatedUser(user.getUsername());
                scenicEntity.setUpdatedDate(new Date());
                scenicMapper.updateByPrimaryKey(scenicEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage changeStatus(String id, Integer status, String username) throws Exception {
        ScenicEntity entity = scenicMapper.selectByPrimaryKey(id);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("无景区信息");
        }
        if (status == 9 && entity.getStatus() != 1) {
            return ResponseMessage.validFailResponse().setMsg("景区未审核通过，不能上线，请先审核景区信息");
        }
        // 添加上下线记录
        String msg = status == 9 ? "上线成功" : "下线成功";
        saveAuditLog(entity.getStatus(), status, id, username, msg, 1);
        entity.setUpdatedUser(username);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        scenicMapper.updateByPrimaryKeyWithBLOBs(entity);
        return ResponseMessage.defaultResponse().setMsg("状态变更成功");
    }

    @Override
    public ResponseMessage examineScenic(String id, int auditStatus, String msg, User currentUser) throws Exception {
        ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(id);
        if (scenicEntity != null) {
            // 添加审核记录
            saveAuditLog(scenicEntity.getStatus(), auditStatus, id, currentUser.getUsername(), msg, 0);
            scenicEntity.setStatus(auditStatus);
            scenicEntity.setUpdatedDate(new Date());
            scenicMapper.updateByPrimaryKeyWithBLOBs(scenicEntity);
        } else {
            return ResponseMessage.validFailResponse().setMsg("景区信息不存在");
        }
        return ResponseMessage.defaultResponse();
    }

    @Override
    public ResponseMessage getScenicInfo(String title) {
        List<ScenicEntity> entities = scenicMapper.getScenicInfo(title);
        return ResponseMessage.defaultResponse().setData(entities);
    }

    @Override
    public ResponseMessage relateTags(Map<String, Object> tags, User user) {
        List<Map<String, Object>> tagsList = (List<Map<String, Object>>) tags.get("tagsArr");
        String relateId = tags.get("id").toString();
        if (null != tagsList && !tagsList.isEmpty()) {
            this.saveTags(tagsList, relateId, user);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    private int saveAuditLog(int preStatus, int auditStatus, String principalId, String userName, String msg, int type) {
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setId(UUIDUtils.getInstance().getId());
        auditLogEntity.setType(type);
        auditLogEntity.setPreStatus(preStatus);
        auditLogEntity.setStatus(auditStatus);
        auditLogEntity.setPrincipalId(principalId);
        auditLogEntity.setCreatedDate(new Date());
        auditLogEntity.setCreatedUser(userName);
        auditLogEntity.setOpinion(msg);
        return auditLogMapper.insert(auditLogEntity);
    }
}
