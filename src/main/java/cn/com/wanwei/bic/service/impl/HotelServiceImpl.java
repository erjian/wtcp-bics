package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.BusinessMapper;
import cn.com.wanwei.bic.mapper.ContactMapper;
import cn.com.wanwei.bic.mapper.EnterpriseMapper;
import cn.com.wanwei.bic.mapper.HotelMapper;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.HotelInfoModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.HotelService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.mybatis.service.impl.BaseServiceImpl;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * wtcp-bics - HotelServiceImpl 酒店基础信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class HotelServiceImpl extends BaseServiceImpl<HotelMapper, HotelEntity, String> implements HotelService {

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

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private ContactMapper contactMapper;

    public static final int LINE_TYPE = 1;//上下线类型

    public static final int AUDIT_TYPE = 0;//审核类型


    @Override
    public ResponseMessage insert(EntityTagsModel<HotelEntity> model, User currentUser, Long ruleId, Integer appCode) {
        HotelEntity hotelEntity = model.getEntity();
        String id = UUIDUtils.getInstance().getId();
        hotelEntity.setId(id);
        ResponseMessage response = coderServiceFeign.buildSerialByCode(ruleId, appCode, model.getType());
        hotelEntity.setCode(response.getData().toString());
        hotelEntity.setFullSpell(PinyinUtils.getPingYin(hotelEntity.getTitle()).toLowerCase());
        hotelEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(hotelEntity.getTitle()).toLowerCase());
        hotelEntity.setCreatedUser(currentUser.getUsername());
        hotelEntity.setUpdatedUser(currentUser.getUsername());
        hotelEntity.setCreatedDate(new Date());
        hotelEntity.setUpdatedDate(new Date());
        hotelEntity.setDeptCode(currentUser.getOrg().getCode());
        hotelEntity.setStatus(0);
        hotelEntity.setWeight(0);
        hotelMapper.insert(hotelEntity);
        //保存素材
        if (CollectionUtils.isNotEmpty(model.getMaterialList())) {
            materialService.batchInsert(id, model.getMaterialList(), currentUser);
        }

        //处理标签
        if (CollectionUtils.isNotEmpty(model.getTagsList())) {
            tagsService.batchInsert(id, model.getTagsList(), currentUser, HotelTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HotelEntity> hotelModel, User currentUser) {
        Optional<HotelEntity> optional = hotelMapper.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.validFailResponse().setMsg("该酒店信息不存在！");
        }
        HotelEntity hotelEntity = optional.get();
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
            tagsService.batchInsert(hotelEntity.getId(), hotelModel.getTagsList(), currentUser, HotelTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage updateOnlineStatus(String id, Integer status, String username) {
        Optional<HotelEntity> optional = hotelMapper.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.validFailResponse().setMsg("该数据不存在");
        }
        HotelEntity entity = optional.get();
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
        Optional<HotelEntity> optional = hotelMapper.findById(id);
        if (!optional.isPresent()) {
            return responseMessage.validFailResponse().setMsg("该酒店信息不存在");
        }
        HotelEntity HotelEntity = optional.get();
        HotelEntity.setStatus(auditStatus);
        HotelEntity.setUpdatedDate(new Date());
        hotelMapper.updateById(HotelEntity);
        //添加审核记录
        commonService.saveAuditLog(HotelEntity.getStatus(), auditStatus, id, currentUser.getUsername(), msg, AUDIT_TYPE);
        return responseMessage;
    }

    @Override
    public ResponseMessage insertTags(Map<String, Object> tags, User currentUser) {
        List<Map<String, Object>> tagsList = (List<Map<String, Object>>) tags.get("tagsArr");
        List<BaseTagsEntity> bList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(tagsList)) {
            for (Map<String, Object> m : tagsList) {
                BaseTagsEntity entity = new BaseTagsEntity();
                entity.setCreatedUser(currentUser.getUsername());
                entity.setUpdatedUser(currentUser.getUsername());
                entity.setCreatedDate(new Date());
                entity.setUpdatedDate(new Date());
                entity.setPrincipalId(String.valueOf(m.get("principalId")));
                entity.setTagName(String.valueOf(m.get("tagName")));
                entity.setTagCatagory(String.valueOf(m.get("tagCatagory")));
                bList.add(entity);
            }
            tagsService.batchInsert(tags.get("id").toString(), bList, currentUser, HotelTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    @Override
    public ResponseMessage findHotelInfoById(String id) {
        Optional<HotelEntity> optional = hotelMapper.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.validFailResponse().setMsg("该酒店不存在");
        }
        HotelEntity hotelEntity = optional.get();
        Map<String, Object> data = Maps.newHashMap();
        hotelEntity.setTagsEntities(tagsService.findListByPriId(id, HotelTagsEntity.class));
        data.put("hotelEntity", hotelEntity);

        EnterpriseEntity enterpriseEntity = enterpriseMapper.selectByPrincipalId(id);
        data.put("enterpriseEntity", enterpriseEntity);

        BusinessEntity businessEntity = businessMapper.findByPrincipalId(id);
        data.put("businessEntity", businessEntity);

        ContactEntity contactEntity = contactMapper.selectByPrincipalId(id);
        data.put("contactEntity", contactEntity);

        data.put("fileList", materialService.handleMaterialNew(id));
        return ResponseMessage.defaultResponse().setData(data);
    }

    @Override
    public ResponseMessage findBySearchValue(String name, String ids) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> idList = null;
        if (StringUtil.isNotEmpty(ids)) {
            idList = Arrays.asList(ids.split(","));
        }
        List<HotelEntity> list = hotelMapper.findBySearchValue(name, idList);
        if (!list.isEmpty()) {
            for (HotelEntity entity : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", entity.getId());
                map.put("unicode", entity.getCode());
                map.put("name", entity.getTitle());
                map.put("level", entity.getLevel());
                map.put("longitude", entity.getLongitude());
                map.put("latitude", entity.getLatitude());
                map.put("areaCode", entity.getRegion());
                map.put("areaName", entity.getRegionFullName());
                map.put("address", entity.getAddress());
                map.put("pinyin", entity.getSimpleSpell());
                map.put("pinyinqp", entity.getFullSpell());
                data.add(map);
            }
            responseMessage.setData(data);
        } else {
            responseMessage.setData("暂无数据");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findByAreaCode(String areaCode) {
        return ResponseMessage.defaultResponse().setData(hotelMapper.findByAreaCode(areaCode));
    }

    @Override
    public ResponseMessage findInfoById(String id) {
        HotelInfoModel hotelInfoModel=new HotelInfoModel();
        HotelEntity hotelEntity = hotelMapper.findById(id).get();
        if(hotelEntity!=null){
            //标签信息
            hotelEntity.setTagsEntities(tagsService.findListByPriId(id, HotelTagsEntity.class));
            hotelInfoModel.setHotelEntity(hotelEntity);
            //通讯信息
            ContactEntity contactEntity = contactMapper.selectByPrincipalId(id);
            hotelInfoModel.setContactEntity(contactEntity);
            //素材信息
            hotelInfoModel.setFileList( materialService.handleMaterialNew(id));
            return ResponseMessage.defaultResponse().setData(hotelInfoModel);
        }
        return ResponseMessage.validFailResponse().setMsg("酒店信息不存在");

    }


}
