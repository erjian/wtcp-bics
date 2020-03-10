package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.HotelEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.HotelMapper;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.mybatis.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * wtcp-bics - HotelServiceImpl 酒店基础信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class HotelServiceImpl extends BaseServiceImpl<HotelMapper,HotelEntity,String> implements HotelService {

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private CommonService commonService;

    public static final int LINE_TYPE = 1;//上下线类型

    public static final int AUDIT_TYPE = 0;//审核类型


    @Override
    public ResponseMessage insert(EntityTagsModel<HotelEntity> model, User currentUser, Long ruleId, Integer appCode) {
        HotelEntity HotelEntity = model.getEntity();
        String id = UUIDUtils.getInstance().getId();
        HotelEntity.setId(id);
        ResponseMessage response = coderServiceFeign.buildSerialByCode(ruleId, appCode, model.getType());
        HotelEntity.setCode(response.getData().toString());
        HotelEntity.setFullSpell(PinyinUtils.getPingYin(HotelEntity.getTitle()).toLowerCase());
        HotelEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(HotelEntity.getTitle()).toLowerCase());
        HotelEntity.setCreatedUser(currentUser.getUsername());
        HotelEntity.setUpdatedUser(currentUser.getUsername());
        HotelEntity.setCreatedDate(new Date());
        HotelEntity.setUpdatedDate(new Date());
        HotelEntity.setDeptCode(currentUser.getOrg().getCode());
        HotelEntity.setStatus(0);
        HotelEntity.setWeight(0);
        hotelMapper.insert(HotelEntity);
        //保存素材
        if (CollectionUtils.isNotEmpty(model.getMaterialList())) {
            materialService.batchInsert(id, model.getMaterialList(), currentUser);
        }

        //处理标签
        if (CollectionUtils.isNotEmpty(model.getTagsList())) {
            tagsService.batchInsert(id, model.getTagsList(), currentUser, HotelEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HotelEntity> hotelModel, User currentUser) {
        HotelEntity hotelEntity = hotelMapper.findById(id).orElse(null);;
        if (null == hotelEntity) {
            return ResponseMessage.validFailResponse().setMsg("该酒店信息不存在！");
        }
        HotelEntity entity = hotelModel.getEntity();
        entity.setId(id);
        entity.setCode(hotelEntity.getCode());
        entity.setCreatedDate(hotelEntity.getCreatedDate());
        entity.setCreatedUser(hotelEntity.getCreatedUser());
        entity.setDeptCode(hotelEntity.getDeptCode());
        entity.setFullSpell(PinyinUtils.getPingYin(entity.getTitle()).toLowerCase());
        entity.setSimpleSpell(PinyinUtils.converterToFirstSpell(entity.getTitle()).toLowerCase());
        entity.setStatus(0);
        entity.setUpdatedDate(new Date());
        entity.setUpdatedUser(currentUser.getName());
        hotelMapper.updateById(entity);

        //处理标签
        if (CollectionUtils.isNotEmpty(hotelModel.getTagsList())) {
            tagsService.batchInsert(hotelEntity.getId(), hotelModel.getTagsList(), currentUser, HotelEntity.class);
        }

        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage updateOnlineStatus(String id, Integer status, String username) {
        HotelEntity entity = hotelMapper.findById(id).orElse(null);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("该数据不存在");
        }
        if (status == 9 && entity.getStatus() != 1) {
            return ResponseMessage.validFailResponse().setMsg("该数据未审核通过，不能上线，请先进行审核！");
        }
        entity.setUpdatedUser(username);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        hotelMapper.updateById(entity);
        String msg = status == 9 ? "上线成功" : "下线成功";
        commonService.saveAuditLog(entity.getStatus(), status, id, username, msg, LINE_TYPE);
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public ResponseMessage updateAuditStatus(String id, int auditStatus, String msg, User currentUser) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        HotelEntity HotelEntity = hotelMapper.findById(id).orElse(null);;
        if (HotelEntity != null) {
            HotelEntity.setStatus(auditStatus);
            HotelEntity.setUpdatedDate(new Date());
            hotelMapper.updateById(HotelEntity);
            //添加审核记录
            commonService.saveAuditLog(HotelEntity.getStatus(), auditStatus, id, currentUser.getUsername(), msg, AUDIT_TYPE);
        } else {
            return responseMessage.validFailResponse().setMsg("该酒店信息不存在");
        }
        return responseMessage;
    }


}
