package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.CateringEntity;
import cn.com.wanwei.bic.entity.CateringTagsEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.CateringMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.CateringService;
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
@SuppressWarnings("all")
public class CateringServiceImpl implements CateringService {

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private CateringMapper cateringMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title", "subTilte","simpleSpell","fullSpell");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<CateringEntity> cateringEntities = cateringMapper.findByPage(filter);
        PageInfo<CateringEntity> pageInfo = new PageInfo<>(cateringEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage findByList(Map<String, Object> filter) {
        List<CateringEntity> entityList = cateringMapper.findByList(filter);
        return ResponseMessage.defaultResponse().setData(entityList);
    }

    @Override
    public ResponseMessage findById(String id) {
        CateringEntity entity = cateringMapper.findById(id);
        if(entity == null){
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(entity);
    }

    @Override
    public ResponseMessage deleteById(String id) {
        cateringMapper.deleteById(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage insert(EntityTagsModel<CateringEntity> cateringModel, User user, Long ruleId, Integer appCode) {
        CateringEntity entity = cateringModel.getEntity();
        String id = UUIDUtils.getInstance().getId();
        String type = cateringModel.getType();
        ResponseMessage result = coderServiceFeign.buildSerialByCode(ruleId, appCode, type);
        entity.setId(id);
        entity.setCreatedDate(new Date());
        entity.setCreatedUser(user.getUsername());
        entity.setUpdatedDate(new Date());
        entity.setUpdatedUser(user.getUsername());
        entity.setCode(result.getData().toString());
        entity.setSimpleSpell(PinyinUtils.converterToFirstSpell(entity.getTitle()).toLowerCase());
        entity.setFullSpell(PinyinUtils.getPingYin(entity.getTitle()).toLowerCase());
        entity.setStatus(0);
        entity.setWeight(0);
        cateringMapper.insert(entity);


        //处理标签
        if (CollectionUtils.isNotEmpty(cateringModel.getTagsList())) {
            tagsService.batchInsert(id, cateringModel.getTagsList(), user, CateringTagsEntity.class);
        }

        //处理素材
        if(CollectionUtils.isNotEmpty(cateringModel.getMaterialList())){
            materialService.batchInsert(id,cateringModel.getMaterialList(),user);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage updateById(String id, EntityTagsModel<CateringEntity> cateringModel, User user) {
        CateringEntity cateringEntity = cateringMapper.findById(id);
        if(cateringEntity == null){
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        CateringEntity entity = cateringModel.getEntity();
        entity.setUpdatedDate(new Date());
        entity.setUpdatedUser(user.getUsername());
        entity.setSimpleSpell(PinyinUtils.converterToFirstSpell(entity.getTitle()).toLowerCase());
        entity.setFullSpell(PinyinUtils.getPingYin(entity.getTitle()).toLowerCase());
        entity.setStatus(0);

        cateringMapper.updateById(entity);
        //处理标签
        if (CollectionUtils.isNotEmpty(cateringModel.getTagsList())) {
            tagsService.batchInsert(id, cateringModel.getTagsList(), user, CateringTagsEntity.class);
        }

        //处理素材
        if(CollectionUtils.isNotEmpty(cateringModel.getMaterialList())){
            materialService.batchInsert(id,cateringModel.getMaterialList(),user);
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
            tagsService.batchInsert(tags.get("id").toString(), bList, user, CateringTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    @Override
    public ResponseMessage updateStatus(String id, Integer status, String user) {
        CateringEntity entity = cateringMapper.findById(id);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("无餐饮信息");
        }
        if (status == 9 && entity.getStatus() != 1) {
            return ResponseMessage.validFailResponse().setMsg("餐饮信息未审核通过，不能上线，请先审核餐饮信息信息");
        }
        // 添加上下线记录
        String msg = status == 9 ? "上线成功" : "下线成功";
        insertAuditLog(entity.getStatus(), status, id, user, msg, 1);
        entity.setUpdatedUser(user);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        cateringMapper.updateById(entity);
        return ResponseMessage.defaultResponse().setMsg("状态变更成功");
    }

    @Override
    public ResponseMessage updateAuditStatus(String id, int auditStatus, String msg, User user) {
        CateringEntity entity = cateringMapper.findById(id);
        if (entity != null) {
            // 添加审核记录
            insertAuditLog(entity.getStatus(), auditStatus, id, user.getUsername(), msg, 0);
            entity.setStatus(auditStatus);
            entity.setUpdatedDate(new Date());
            entity.setUpdatedUser(user.getUsername());
            cateringMapper.updateById(entity);
        } else {
            return ResponseMessage.validFailResponse().setMsg("餐饮信息不存在");
        }
        return ResponseMessage.defaultResponse().setMsg("审核成功");
    }

    @Override
    public ResponseMessage updateDeptCode(String updatedUser, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        cateringMapper.updateDeptCode(updatedUser, new Date(), deptCode, ids);
        return ResponseMessage.defaultResponse().setMsg("关联机构成功");
    }

    @Override
    public ResponseMessage findByTitleAndIdNot(String title, String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<CateringEntity> cateringEntities = cateringMapper.findByTitleAndIdNot(title, id);
        if (CollectionUtils.isNotEmpty(cateringEntities)) {
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg("该名称已经存在！");
        }
        return responseMessage;
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
