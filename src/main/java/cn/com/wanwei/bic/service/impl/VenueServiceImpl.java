/**
 * 该源代码文件 VenueServiceImpl.java 是工程“wtcp-bics”的一部分。
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2020年3月2日14:54:56
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.VenueEntity;
import cn.com.wanwei.bic.entity.VenueTagsEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.VenueMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.HallService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.service.VenueService;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - VenueServiceImpl 场馆基础信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueMapper venueMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private HallService hallService;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private MaterialService materialService;



    @Override
    public ResponseMessage save(EntityTagsModel<VenueEntity> venueModel, User user, Long ruleId, Integer appCode) {
        VenueEntity record = venueModel.getEntity();
        String type = venueModel.getType();
        String id = UUIDUtils.getInstance().getId();
        record.setId(id);
        ResponseMessage result = coderServiceFeign.buildSerialByCode(ruleId, appCode, type);
        record.setCode(result.getData().toString());
        record.setFullSpell(PinyinUtils.getPingYin(record.getTitle()).toLowerCase());
        record.setSimpleSpell(PinyinUtils.converterToFirstSpell(record.getTitle()).toLowerCase());
        record.setCreatedUser(user.getUsername());
        record.setUpdatedUser(user.getUsername());
        record.setCreatedDate(new Date());
        record.setUpdatedDate(new Date());
        record.setDeptCode(user.getOrg().getCode());
        record.setStatus(0);
        record.setWeight(0);
        venueMapper.insert(record);

        //处理标签
        if (CollectionUtils.isNotEmpty(venueModel.getTagsList())) {
            tagsService.batchInsert(record.getId(), venueModel.getTagsList(), user, VenueTagsEntity.class);
        }

        //处理素材
        if(CollectionUtils.isNotEmpty(venueModel.getMaterialList())){
            materialService.batchInsert(id,venueModel.getMaterialList(),user);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage deleteByPrimaryKey(String id) {
        long hallNum = hallService.countByVenueId(id);
        if(hallNum > 0L){
            return ResponseMessage.validFailResponse().setMsg("该场馆存在厅室，不能删除");
        }
        venueMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public VenueEntity selectByPrimaryKey(String id) {
        return venueMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResponseMessage edit(String id, EntityTagsModel<VenueEntity> venueModel, User user) {
        VenueEntity entity = venueMapper.selectByPrimaryKey(id);
        VenueEntity record = venueModel.getEntity();
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("不存在该场馆");
        }
        record.setId(id);
        record.setCode(entity.getCode());
        record.setCreatedDate(entity.getCreatedDate());
        record.setCreatedUser(entity.getCreatedUser());
        record.setDeptCode(entity.getDeptCode());
        record.setFullSpell(PinyinUtils.getPingYin(record.getTitle()).toLowerCase());
        record.setSimpleSpell(PinyinUtils.converterToFirstSpell(record.getTitle()).toLowerCase());
        record.setStatus(0);
        record.setUpdatedDate(new Date());
        record.setUpdatedUser(user.getUsername());
        venueMapper.updateByPrimaryKey(record);

        //处理标签
        if (CollectionUtils.isNotEmpty(venueModel.getTagsList())) {
            tagsService.batchInsert(record.getId(), venueModel.getTagsList(), user, VenueTagsEntity.class);
        }

        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage findByPage(Integer page, Integer size,Map<String, Object> filter) {
        return getPageInfo(page, size, filter,null);
    }

    private ResponseMessage getPageInfo(Integer page, Integer size, Map<String, Object> filter, String type){
        EscapeCharUtils.escape(filter, "title", "subTitle", "areaName");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<VenueEntity> venueEntities = null;
        if(StringUtils.isNotEmpty(type) && "feign".equalsIgnoreCase(type)){
            venueEntities = venueMapper.findByPageForFeign(filter);
            for(VenueEntity item : venueEntities){
                item.setTagsEntities(tagsService.findListByPriId(item.getId(), VenueTagsEntity.class));
            }
        }else{
            venueEntities = venueMapper.findByPage(filter);
        }
        PageInfo<VenueEntity> pageInfo = new PageInfo<>(venueEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage dataBind(String updatedUser, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        venueMapper.dataBind(updatedUser, new Date(), deptCode, ids);
        return ResponseMessage.defaultResponse().setMsg("关联机构成功");
    }

    @Override
    public ResponseMessage changeStatus(String id, Integer status, String username) throws Exception {
        VenueEntity entity = venueMapper.selectByPrimaryKey(id);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("无场馆信息");
        }
        if (status == 9 && entity.getStatus() != 1) {
            return ResponseMessage.validFailResponse().setMsg("场馆未审核通过，不能上线，请先审核场馆信息");
        }
        // 添加上下线记录
        String msg = status == 9 ? "上线成功" : "下线成功";
        saveAuditLog(entity.getStatus(), status, id, username, msg, 1);
        entity.setUpdatedUser(username);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        venueMapper.updateByPrimaryKey(entity);
        return ResponseMessage.defaultResponse().setMsg("状态变更成功");
    }

    @Override
    public ResponseMessage examineVenue(String id, int auditStatus, String msg, User currentUser) throws Exception {
        VenueEntity venueEntity = venueMapper.selectByPrimaryKey(id);
        if (venueEntity != null) {
            // 添加审核记录
            saveAuditLog(venueEntity.getStatus(), auditStatus, id, currentUser.getUsername(), msg, 0);
            venueEntity.setStatus(auditStatus);
            venueEntity.setUpdatedDate(new Date());
            venueMapper.updateByPrimaryKey(venueEntity);
        } else {
            return ResponseMessage.validFailResponse().setMsg("场馆信息不存在");
        }
        return ResponseMessage.defaultResponse();
    }

    @Override
    public ResponseMessage getVenueInfo(String title, Integer status) {
        List<VenueEntity> entities = venueMapper.getVenueInfo(title, status);
        return ResponseMessage.defaultResponse().setData(entities);
    }

    @Override
    public ResponseMessage relateTags(Map<String, Object> tags, User user) {
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
            tagsService.batchInsert(tags.get("id").toString(), bList, user, VenueTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    @Override
    public ResponseMessage findByTitleAndIdNot(String title, String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        try {
            List<VenueEntity> venueEntities = venueMapper.findByTitleAndIdNot(title, id);
            if (CollectionUtils.isNotEmpty(venueEntities)) {
                responseMessage.setStatus(ResponseMessage.FAILED).setMsg("该名称已经存在！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg(e.getMessage());
        }
        return responseMessage;
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
