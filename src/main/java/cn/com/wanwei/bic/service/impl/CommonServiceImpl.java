package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.config.Constant;
import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.CommonMapper;
import cn.com.wanwei.bic.model.BatchAuditModel;
import cn.com.wanwei.bic.model.DataType;
import cn.com.wanwei.bic.model.FindStatusModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.ExceptionUtils;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.util.*;

@Slf4j
@Service
public class CommonServiceImpl<T> implements CommonService<T> {

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private ScenicService scenicService;

    @Autowired
    private EntertainmentService entertainmentService;

    @Autowired
    private TravelAgentService travelAgentService;

    @Autowired
    private RentalCarService rentalCarService;

    @Autowired
    private PeripheryService peripheryService;

    @Autowired
    private DriveCampService driveCampService;

    @Autowired
    private TrafficAgentService trafficAgentService;

    @Override
    public ResponseMessage changeWeight(WeightModel weightModel, User user, Class<T> clazz) throws Exception {
        //1、根据class获取表名
        String tableName = getTableName(clazz);

        //2、根据表名查询对应的最大权重值
        Integer maxNum = commonMapper.commonMaxWeight(tableName);
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {

                //3、根据表名重置权重值
                commonMapper.commonClearWeight(tableName);
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                //4、根据表名修改权重值
                commonMapper.commonUpdateWeight(ids.get(i), maxNum + ids.size() - i, user.getUsername(), new Date(), tableName);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage batchChangeStatus(BatchAuditModel batchAuditModel, User user, Class<T> clazz) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        String tableName = getTableName(clazz);
        for (String id : batchAuditModel.getIds()) {
            // 获取数据信息
            FindStatusModel statusModel = commonMapper.findById(id, tableName);
            if (null != statusModel) {
                // 若包含审核，则进行上下线操作时，必须是审核通过的
                Boolean statusFlag = statusModel.getStatus() == 2 ? true : false;
                Boolean passFlag = (statusModel.getStatus() == 2 && batchAuditModel.getStatus() == 1) ? true : false;
                Boolean pendingFlag = (batchAuditModel.getHasAudit() && statusModel.getStatus() == 0 && batchAuditModel.getStatus() == 9) ? true : false;
                if (batchAuditModel.getHasAudit() && (statusFlag || passFlag || pendingFlag)) {
                    continue;
                }
                // 更新状态
                int successNum = commonMapper.updateById(this.makeParams(id, batchAuditModel.getStatus(), user, tableName));
                // 记录操作流水
                if (successNum > 0) {
                    this.saveAuditLog(statusModel.getStatus(), batchAuditModel.getStatus(), id, user.getUsername(),
                            batchAuditModel.getMsg(), batchAuditModel.getType());
                }
            }
        }
        String backMsg = "操作成功";
        if (batchAuditModel.getType() == 0) {
            backMsg = "审核成功";
        } else if (batchAuditModel.getHasAudit()) {
            backMsg = "操作成功，已过滤不可操作的数据";
        }
        return responseMessage.setMsg(backMsg);
    }

    @Override
    public List<Map<String, Object>> getAreaListByPcode(String pcode, int length) {
        ResponseMessage responseMessageByCity = coderServiceFeign.areaList(pcode);
        if (responseMessageByCity.getStatus() == 1 && responseMessageByCity.getData() != null) {
            JsonArray citys = new JsonParser().parse(responseMessageByCity.getData().toString()).getAsJsonArray();
            List<Map<String, Object>> areaList = new ArrayList<>();
            citys.forEach(areaObject -> {
                Map<String, Object> areaMap = new HashMap<>();
                JsonObject area = areaObject.getAsJsonObject();
                String code = area.get("code").getAsString();
                String name = area.get("name").getAsString();
                areaMap.put(Constant.LABEL, name);
                areaMap.put(Constant.VALUE, name);
                areaMap.put("code", code);
                //此处设置仅查询县级及以上数据
                if (code.length() < length) {
                    areaMap.put(Constant.CHILDREN, getAreaListByPcode(code, length));
                }
                areaList.add(areaMap);
            });
            return areaList;
        }
        return null;
    }

    @Override
    public ResponseMessage getDataByType(String type, String name, String ids) {
        ResponseMessage responseMessage;
        if (type.equals(DataType.SCENIC_TYPE.getKey()) || type.equals(DataType.TOUR_VILLAGE_TYPE.getKey())) {
            responseMessage = scenicService.findBySearchValue(type, name, ids);
        } else if (type.equals(DataType.TRAVEL_TYPE.getKey())) {
            responseMessage = travelAgentService.findBySearchValue(name, ids);
        } else if (type.equals(DataType.FOOD_TYPE.getKey())
                || type.equals(DataType.SHOPPING_TYPE.getKey())
                || type.equals(DataType.FOOD_STREET.getKey())
                || type.equals(DataType.SPECIAL_SNACKS.getKey())
                || type.equals(DataType.SPECIALTY.getKey())) {
            responseMessage = peripheryService.findBySearchValue(type, name, ids);
        } else if (type.equals(DataType.AGRITAINMENT_TYPE.getKey())) {
            responseMessage = entertainmentService.findBySearchValue(type, name, ids);
        } else if (type.equals(DataType.RENTAL_CAR_TYPE.getKey())) {
            responseMessage = rentalCarService.findBySearchValue(name, ids);
        } else if (type.equals(DataType.TRAFFIC_AGENT_TYPE.getKey())) {
            responseMessage = trafficAgentService.findBySearchValue(name, ids);
        } else if (type.equals(DataType.DRIVE_CAMP_TYPE.getKey())) {
            responseMessage = driveCampService.findBySearchValue(name, ids);
        } else {
            responseMessage = ResponseMessage.validFailResponse().setMsg("获取失败");
        }
        return responseMessage;
    }

    @Override
    public int saveAuditLog(int preStatus, int auditStatus, String principalId, String userName, String msg, int type) {
        try {
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
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return 0;
    }

    private Map<String, Object> makeParams(String id, Integer status, User user, String tableName) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("tableName", tableName);
        params.put("id", id);
        params.put("status", status);
        params.put("updatedDate", new Date());
        params.put("updatedUser", user.getUsername());
        return params;
    }

    private String getTableName(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            return clazz.getAnnotation(Table.class).name();
        }
        return null;
    }
}
