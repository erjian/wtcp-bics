package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.RentalCarEntity;
import cn.com.wanwei.bic.entity.RentalCarTagsEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.RentalCarMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.RentalCarService;
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
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * wtcp-bics/ RentalCarServiceImpl  汽车租赁实现类
 */
@Service
@Slf4j
@RefreshScope
public class RentalCarServiceImpl implements RentalCarService {

    @Autowired
    private RentalCarMapper rentalCarMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private AuditLogMapper auditLogMapper;

    private static final String TYPE = "ZC";

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title", "subTitle", "areaName");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<RentalCarEntity> rentalCarEntityPage = rentalCarMapper.findByPage(filter);
        PageInfo<RentalCarEntity> pageInfo = new PageInfo<>(rentalCarEntityPage, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage detail(String id) {
        RentalCarEntity carEntity = rentalCarMapper.selectByPrimaryKey(id);
        if (carEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("该数据不存在！");
        }
        return ResponseMessage.defaultResponse().setData(carEntity);
    }

    @Override
    public ResponseMessage deleteByPrimaryKey(String id) {
        rentalCarMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功！");
    }

    @Override
    public ResponseMessage save(EntityTagsModel<RentalCarEntity> rentalCarModel, User user, Long ruleId, Integer appCode) {
        RentalCarEntity carEntity = rentalCarModel.getEntity();
        String id = UUIDUtils.getInstance().getId();
        carEntity.setId(id);
        ResponseMessage result = coderServiceFeign.buildSerialByCode(ruleId, appCode, TYPE);
        carEntity.setCode(result.getData().toString());
        carEntity.setTitleJp(PinyinUtils.converterToFirstSpell(carEntity.getTitle()).toLowerCase());
        carEntity.setTitleQp(PinyinUtils.getPingYin(carEntity.getTitle()).toLowerCase());
        carEntity.setCreatedUser(user.getUsername());
        carEntity.setCreatedDate(new Date());
        carEntity.setStatus(0);
        carEntity.setWeight(0);
        rentalCarMapper.insert(carEntity);

        //处理标签
        if (CollectionUtils.isNotEmpty(rentalCarModel.getTagsList())) {
            tagsService.batchInsert(carEntity.getId(), rentalCarModel.getTagsList(), user, RentalCarTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage edit(String id, EntityTagsModel<RentalCarEntity> rentalCarModel, User user) {
        RentalCarEntity entity = rentalCarMapper.selectByPrimaryKey(id);
        RentalCarEntity carEntity = rentalCarModel.getEntity();
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("该租车信息不存在");
        }
        carEntity.setId(id);
        carEntity.setCode(entity.getCode());
        carEntity.setCreatedDate(entity.getCreatedDate());
        carEntity.setCreatedUser(entity.getCreatedUser());
        carEntity.setDeptCode(entity.getDeptCode());
        carEntity.setTitleQp(PinyinUtils.getPingYin(carEntity.getTitle()).toLowerCase());
        carEntity.setTitleJp(PinyinUtils.converterToFirstSpell(carEntity.getTitle()).toLowerCase());
        carEntity.setStatus(0);
        carEntity.setUpdatedDate(new Date());
        carEntity.setUpdatedUser(user.getUsername());
        rentalCarMapper.updateByPrimaryKeyWithBLOBs(carEntity);

        //处理标签
        if (CollectionUtils.isNotEmpty(rentalCarModel.getTagsList())) {
            tagsService.batchInsert(carEntity.getId(), rentalCarModel.getTagsList(), user, RentalCarTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage relateTags(Map<String, Object> tags, User user) {
        List<BaseTagsEntity> tagsList = (List<BaseTagsEntity>) tags.get("tagsArr");
        if (null != tagsList && !tagsList.isEmpty()) {
            tagsService.batchInsert(tags.get("id").toString(), tagsList, user,RentalCarTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    @Override
    public ResponseMessage changeWeight(WeightModel weightModel, User user) {
        //查出最大权重
        Integer maxNum = rentalCarMapper.maxWeight();
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {
                rentalCarMapper.clearWeight();
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                RentalCarEntity rentalCarEntity = rentalCarMapper.selectByPrimaryKey(ids.get(i));
                rentalCarEntity.setWeight(maxNum + ids.size() - i);
                rentalCarEntity.setUpdatedUser(user.getUsername());
                rentalCarEntity.setUpdatedDate(new Date());
                rentalCarMapper.updateByPrimaryKey(rentalCarEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage changeStatus(String id, Integer status, String username) {
        RentalCarEntity entity = rentalCarMapper.selectByPrimaryKey(id);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("无租车信息");
        }
        if (status == 9 && entity.getStatus() != 1) {
            return ResponseMessage.validFailResponse().setMsg("租车未审核通过，不能上线，请先审核租车信息");
        }
        // 添加上下线记录
        String msg = status == 9 ? "上线成功" : "下线成功";
        saveAuditLog(entity.getStatus(), status, id, username, msg, 1);
        entity.setUpdatedUser(username);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        rentalCarMapper.updateByPrimaryKeyWithBLOBs(entity);
        return ResponseMessage.defaultResponse().setMsg("状态变更成功");
    }

    @Override
    public ResponseMessage examineScenic(String id, int auditStatus, String msg, User user) {
        RentalCarEntity rentalCarEntity = rentalCarMapper.selectByPrimaryKey(id);
        if (rentalCarEntity != null) {
            // 添加审核记录
            saveAuditLog(rentalCarEntity.getStatus(), auditStatus, id, user.getUsername(), msg, 0);
            rentalCarEntity.setStatus(auditStatus);
            rentalCarEntity.setUpdatedDate(new Date());
            rentalCarMapper.updateByPrimaryKeyWithBLOBs(rentalCarEntity);
        } else {
            return ResponseMessage.validFailResponse().setMsg("租车信息不存在！");
        }
        return ResponseMessage.defaultResponse();
    }

    @Override
    public ResponseMessage dataBind(String username, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        rentalCarMapper.dataBind(username, new Date(), deptCode, ids);
        return ResponseMessage.defaultResponse().setMsg("关联机构成功");
    }

    @Override
    public ResponseMessage findByTitleAndIdNot(String title, String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        try {
            List<RentalCarEntity> rentalCarEntity = rentalCarMapper.findByTitleAndIdNot(title, id);
            if (CollectionUtils.isNotEmpty(rentalCarEntity)) {
                responseMessage.setStatus(ResponseMessage.FAILED).setMsg("该名称已经存在！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg(e.getMessage());
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage getRentalCarInfo(String title) {
        List<RentalCarEntity> entities = rentalCarMapper.getRentalCarInfo(title);
        return ResponseMessage.defaultResponse().setData(entities);
    }

    private int saveAuditLog(int preStatus, int auditStatus, String principalId, String userName, String msg, int type) {
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setId(UUIDUtils.getInstance().getId());
        auditLogEntity.setType(type);
        auditLogEntity.setPreStatus(preStatus);
        auditLogEntity.setStatus(auditStatus);
        auditLogEntity.setPrincipalId(principalId);
        auditLogEntity.setCreatedUser(userName);
        auditLogEntity.setCreatedDate(new Date());
        auditLogEntity.setOpinion(msg);
        return auditLogMapper.insert(auditLogEntity);
    }
}
