package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.HeritageMapper;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.HeritageService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.mybatis.service.impl.BaseServiceImpl;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * wtcp-bics - HeritageServiceImpl 非遗基础信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class HeritageServiceImpl extends BaseServiceImpl<HeritageMapper,HeritageEntity,String> implements HeritageService {

    @Autowired
    private HeritageMapper heritageMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CommonService commonService;

    public static final int LINE_TYPE = 1;//上下线类型

    public static final int AUDIT_TYPE = 0;//审核类型


    @Override
    public ResponseMessage insert(EntityTagsModel<HeritageEntity> model, User currentUser, Long ruleId, Integer appCode) {
        HeritageEntity heritageEntity = model.getEntity();
        String id = UUIDUtils.getInstance().getId();
        heritageEntity.setId(id);
        ResponseMessage response = coderServiceFeign.buildSerialByCode(ruleId, appCode, model.getType());
        heritageEntity.setCode(response.getData().toString());
        heritageEntity.setFullSpell(PinyinUtils.getPingYin(heritageEntity.getTitle()).toLowerCase());
        heritageEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(heritageEntity.getTitle()).toLowerCase());
        heritageEntity.setCreatedUser(currentUser.getUsername());
        heritageEntity.setUpdatedUser(currentUser.getUsername());
        heritageEntity.setCreatedDate(new Date());
        heritageEntity.setUpdatedDate(new Date());
        heritageEntity.setDeptCode(currentUser.getOrg().getCode());
        heritageEntity.setStatus(0);
        heritageEntity.setWeight(0);
        heritageMapper.insert(heritageEntity);
        //保存素材
        if (CollectionUtils.isNotEmpty(model.getMaterialList())) {
            materialService.batchInsert(id, model.getMaterialList(), currentUser);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HeritageEntity> heritageModel, User currentUser) {
        try {
            HeritageEntity heritageEntity = heritageMapper.findById(id).get();
            HeritageEntity entity = heritageModel.getEntity();
            entity.setId(id);
            entity.setCode(heritageEntity.getCode());
            entity.setCreatedDate(heritageEntity.getCreatedDate());
            entity.setCreatedUser(heritageEntity.getCreatedUser());
            entity.setDeptCode(heritageEntity.getDeptCode());
            entity.setFullSpell(PinyinUtils.getPingYin(entity.getTitle()).toLowerCase());
            entity.setSimpleSpell(PinyinUtils.converterToFirstSpell(entity.getTitle()).toLowerCase());
            entity.setStatus(0);
            entity.setUpdatedDate(new Date());
            entity.setUpdatedUser(currentUser.getName());
            heritageMapper.updateById(entity);
            return ResponseMessage.defaultResponse().setMsg("更新成功");
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("该非遗信息不存在！");
        }
    }

    @Override
    public ResponseMessage updateOnlineStatus(String id, Integer status, String username) {
        try {
            HeritageEntity entity = heritageMapper.findById(id).get();
            if (status == 9 && entity.getStatus() != 1) {
                return ResponseMessage.validFailResponse().setMsg("该数据未审核通过，不能上线，请先进行审核！");
            }
            entity.setUpdatedUser(username);
            entity.setUpdatedDate(new Date());
            entity.setStatus(status);
            heritageMapper.updateById(entity);
            String msg = status == 9 ? "上线成功" : "下线成功";
            commonService.saveAuditLog(entity.getStatus(), status, id, username, msg, LINE_TYPE);
            return ResponseMessage.defaultResponse().setMsg(msg);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("该数据不存在");
        }
    }

    @Override
    public ResponseMessage updateAuditStatus(String id, int auditStatus, String msg, User currentUser) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        HeritageEntity heritageEntity = heritageMapper.findById(id).get();
        try {
            heritageEntity.setStatus(auditStatus);
            heritageEntity.setUpdatedDate(new Date());
            heritageMapper.updateById(heritageEntity);
            //添加审核记录
            commonService.saveAuditLog(heritageEntity.getStatus(), auditStatus, id, currentUser.getUsername(), msg, AUDIT_TYPE);
        } catch (Exception e) {
            log.info(e.getMessage());
            return responseMessage.validFailResponse().setMsg("该非遗信息不存在");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findHeritageInfoById(String id) {
        try {
            HeritageEntity heritageEntity = heritageMapper.findById(id).get();
            Map<String, Object> data = Maps.newHashMap();
            data.put("heritageEntity", heritageEntity);
            data.put("fileList", materialService.handleMaterialNew(id));
            return ResponseMessage.defaultResponse().setData(data);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("该非遗信息不存在");
        }

    }

    @Override
    public ResponseMessage findBySearchValue(String name, String ids) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> idList = null;
        if(StringUtil.isNotEmpty(ids)){
            idList = Arrays.asList(ids.split(","));
        }
        List<HeritageEntity> list = heritageMapper.findBySearchValue(name, idList);
        if (!list.isEmpty()) {
            for (HeritageEntity entity : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", entity.getId());
                map.put("unicode", entity.getCode());
                map.put("name", entity.getTitle());
                map.put("level", entity.getLevel());
                map.put("longitude", null);
                map.put("latitude", null);
                map.put("areaCode", entity.getRegion());
                map.put("areaName", entity.getRegionFullName());
                map.put("address", null);
                map.put("pinyin", entity.getSimpleSpell());
                map.put("pinyinqp", entity.getFullSpell());
                data.add(map);
            }
            responseMessage.setData(data);
        }else {
            responseMessage.setData("暂无数据");
        }
        return responseMessage;
    }


}
