package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.CelebrityEntity;
import cn.com.wanwei.bic.entity.CelebrityTagsEntity;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.CelebrityMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.CelebrityService;
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
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Slf4j
@Service
public class CelebrityServiceImpl implements CelebrityService {

    @Autowired
    private CelebrityMapper celebrityMapper;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "name", "simpleSpell","fullSpell");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<CelebrityEntity> scenicEntities = celebrityMapper.findByPage(filter);
        PageInfo<CelebrityEntity> pageInfo = new PageInfo<>(scenicEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage findById(String id) {
        CelebrityEntity celebrityEntity = celebrityMapper.findById(id);
        if(celebrityEntity == null){
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(celebrityEntity);
    }

    @Override
    public ResponseMessage deleteById(String id) {
        celebrityMapper.deleteById(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage insert(EntityTagsModel<CelebrityEntity> celebrityModel, User currentUser) {
        CelebrityEntity celebrityEntity = celebrityModel.getEntity();
        String id = UUIDUtils.getInstance().getId();
        celebrityEntity.setId(id);
        celebrityEntity.setCreatedUser(currentUser.getUsername());
        celebrityEntity.setCreatedDate(new Date());
        celebrityEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(celebrityEntity.getName()).toLowerCase());
        celebrityEntity.setFullSpell(PinyinUtils.getPingYin(celebrityEntity.getName()).toLowerCase());
        celebrityEntity.setStatus(0);
        celebrityEntity.setWeight(0);
        celebrityEntity.setDeptCode(currentUser.getOrg().getCode());

        celebrityMapper.insert(celebrityEntity);

        //处理标签
        if (CollectionUtils.isNotEmpty(celebrityModel.getTagsList())) {
            tagsService.batchInsert(id, celebrityModel.getTagsList(), currentUser, CelebrityTagsEntity.class);
        }

        //处理素材
        if(CollectionUtils.isNotEmpty(celebrityModel.getMaterialList())){
            materialService.batchInsert(id,celebrityModel.getMaterialList(),currentUser);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage updateById(String id, EntityTagsModel<CelebrityEntity> celebrityModel, User user) {
        CelebrityEntity celebrityEntity = celebrityMapper.findById(id);
        if (celebrityEntity == null){
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        CelebrityEntity entity = celebrityModel.getEntity();
        entity.setUpdatedDate(new Date());
        entity.setUpdatedUser(user.getUsername());
        entity.setSimpleSpell(PinyinUtils.converterToFirstSpell(entity.getName()).toLowerCase());
        entity.setFullSpell(PinyinUtils.getPingYin(entity.getName()).toLowerCase());
        entity.setStatus(0);

        celebrityMapper.updateById(entity);

        //处理标签
        if (CollectionUtils.isNotEmpty(celebrityModel.getTagsList())) {
            tagsService.batchInsert(id, celebrityModel.getTagsList(), user, CelebrityTagsEntity.class);
        }

        //处理素材
        if(CollectionUtils.isNotEmpty(celebrityModel.getMaterialList())){
            materialService.batchInsert(id,celebrityModel.getMaterialList(),user);
        }
        return ResponseMessage.defaultResponse().setMsg("更新成功").setData(id);
    }

    @Override
    public ResponseMessage insertTagsBatch(Map<String, Object> tags, User user) {
        List<Map<String, Object>> tagsList = (List<Map<String, Object>>) tags.get("tagsArr");
        List<BaseTagsEntity> bList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(tagsList)) {
            for (Map<String, Object> m : tagsList) {
                BaseTagsEntity entity = new BaseTagsEntity();
                entity.setPrincipalId(String.valueOf(m.get("principalId")));
                entity.setTagName(String.valueOf(m.get("tagName")));
                entity.setTagCatagory(String.valueOf(m.get("tagCatagory")));
                bList.add(entity);
            }
            tagsService.batchInsert(tags.get("id").toString(), bList, user, CelebrityTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    @Override
    public ResponseMessage updateStatus(String id, Integer status, String user) {
        CelebrityEntity entity = celebrityMapper.findById(id);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("无名人信息");
        }
        // 添加上下线记录
        entity.setUpdatedUser(user);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        celebrityMapper.updateById(entity);
        return ResponseMessage.defaultResponse().setMsg("状态变更成功");
    }

    @Override
    public ResponseMessage updateAuditStatus(String id, int auditStatus, String msg, User user) {
        CelebrityEntity entity = celebrityMapper.findById(id);
        if (entity != null) {
            // 添加审核记录
            insertAuditLog(entity.getStatus(), auditStatus, id, user.getUsername(), msg, 0);
            entity.setStatus(auditStatus);
            entity.setUpdatedDate(new Date());
            celebrityMapper.updateById(entity);
        } else {
            return ResponseMessage.validFailResponse().setMsg("名人信息不存在");
        }
        return ResponseMessage.defaultResponse().setMsg("审核成功");
    }

    @Override
    public ResponseMessage updateDeptCode(String updatedUser, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        celebrityMapper.updateDeptCode(updatedUser, new Date(), deptCode, ids);
        return ResponseMessage.defaultResponse().setMsg("关联机构成功");
    }

    @Override
    public ResponseMessage findByList() {
        List<CelebrityEntity> list = celebrityMapper.findByList();
        return ResponseMessage.defaultResponse().setData(list);
    }

    private int insertAuditLog(int preStatus, int auditStatus, String principalId, String userName, String msg, int type) {
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
