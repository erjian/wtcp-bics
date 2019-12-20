package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.CommonMapper;
import cn.com.wanwei.bic.mapper.ScenicMapper;
import cn.com.wanwei.bic.model.BatchAuditModel;
import cn.com.wanwei.bic.model.FindStatusModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl<T> implements CommonService<T> {

    @Autowired
    private ScenicMapper scenicMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Override
    public ResponseMessage changeWeight(WeightModel weightModel, User user, Class<T> clazz) throws Exception {
        //1、根据class获取表名
        String tableName = getTableName(clazz);

        //2、根据表名查询对应的最大权重值
        Integer maxNum = scenicMapper.commonMaxWeight(tableName);
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {

                //3、根据表名重置权重值
                scenicMapper.commonClearWeight(tableName);
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                //4、根据表名修改权重值
                scenicMapper.commonUpdateWeight(ids.get(i), maxNum + ids.size() - i, user.getUsername(), new Date(), tableName);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage batchChangeStatus(BatchAuditModel batchAuditModel, User user, Class<T> clazz) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        String tableName = getTableName(clazz);
        boolean flag = false;
        for (String id : batchAuditModel.getIds()) {
            // 获取数据信息
            FindStatusModel statusModel = commonMapper.findById(id, tableName);
            if (null != statusModel) {
//                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(entityMap.get("status")));
//                Integer preStatus = Integer.parseInt(entityMap.get("status").toString());
                // 进行上下线操作时，必须是审核通过的
                if (batchAuditModel.getType() == 1 && statusModel.getStatus() == 0) {
                    flag = true;
                    continue;
                }
                // 更新状态
                int successNum = commonMapper.updateById(this.makeParams(id, batchAuditModel.getStatus(), user, tableName));
                // 记录操作流水
                if (successNum > 0) {
                    this.saveAuditLog(id, statusModel.getStatus(), batchAuditModel, user);
                }
            }
        }
        String backMsg = "操作成功";
        if (batchAuditModel.getType() == 0) {
            backMsg = "审核成功";
        } else if (batchAuditModel.getType() == 1) {
            backMsg = flag ? "操作成功，已过滤未审核通过的数据" : backMsg;
        }
        return responseMessage.setMsg(backMsg);
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

    private void saveAuditLog(String id, Integer preStatus, BatchAuditModel batchAuditModel, User user) {
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setId(UUIDUtils.getInstance().getId());
        auditLogEntity.setCreatedDate(new Date());
        auditLogEntity.setCreatedUser(user.getUsername());
        auditLogEntity.setPreStatus(preStatus);
        auditLogEntity.setStatus(batchAuditModel.getStatus());
        auditLogEntity.setPrincipalId(id);
        auditLogEntity.setType(batchAuditModel.getType());
        auditLogEntity.setOpinion(batchAuditModel.getMsg());
        auditLogMapper.insert(auditLogEntity);
    }

    private String getTableName(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            return clazz.getAnnotation(Table.class).name();
        }
        return null;
    }
}
