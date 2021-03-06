package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.bic.entity.PoiTagsEntity;
import cn.com.wanwei.bic.entity.TrafficAgentEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.TrafficAgentMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.GouldModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TrafficAgentService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RefreshScope
public class TrafficAgentServiceImpl implements TrafficAgentService {

    @Autowired
    private TrafficAgentMapper trafficAgentMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private MaterialService materialService;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        return getPageInfo(page, size, filter, null);
    }

    @Override
    public ResponseMessage findByPageToC(Integer page, Integer size, Map<String, Object> filter) {
        return getPageInfo(page, size, filter, "toc");
    }

    private ResponseMessage getPageInfo(Integer page, Integer size, Map<String, Object> filter, String type){
        EscapeCharUtils.escape(filter, "title");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        Page<TrafficAgentEntity> trafficAgentEntities = null;
        if(StringUtils.isNotEmpty(type) && "toc".equalsIgnoreCase(type)){
            trafficAgentEntities = trafficAgentMapper.findByPageToC(filter);
        }else{
            trafficAgentEntities = trafficAgentMapper.findByPage(filter);
        }
        PageInfo<TrafficAgentEntity> pageInfo = new PageInfo<>(trafficAgentEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        TrafficAgentEntity trafficAgentEntity = trafficAgentMapper.selectByPrimaryKey(id);
        if (trafficAgentEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
        }
        return ResponseMessage.defaultResponse().setData(trafficAgentEntity);
    }

    @Override
    public ResponseMessage create(TrafficAgentEntity trafficAgentEntity, User user, Long ruleId, Integer appCode) {
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode = coderServiceFeign.buildSerialByCode(ruleId, appCode, trafficAgentEntity.getJpin());
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            String id = UUIDUtils.getInstance().getId();
            trafficAgentEntity.setId(id);
            trafficAgentEntity.setFullSpell(PinyinUtils.getPingYin(trafficAgentEntity.getTitle()));
            trafficAgentEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(trafficAgentEntity.getTitle()));
            trafficAgentEntity.setCode(responseMessageGetCode.getData().toString());
            trafficAgentEntity.setStatus(1);
            trafficAgentEntity.setWeight(0);
            trafficAgentEntity.setCreatedUser(user.getUsername());
            trafficAgentEntity.setCreatedDate(new Date());
            trafficAgentEntity.setUpdatedDate(new Date());
            trafficAgentEntity.setDeptCode(user.getOrg().getCode());
            trafficAgentMapper.insert(trafficAgentEntity);
            //处理编辑页面新增素材
            if(CollectionUtils.isNotEmpty(trafficAgentEntity.getMaterialList())){
                materialService.batchInsert(id,trafficAgentEntity.getMaterialList(),user);
            }
            return ResponseMessage.defaultResponse().setMsg("保存成功!").setData(id);
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage update(String id, TrafficAgentEntity trafficAgentEntity, User user) {
        TrafficAgentEntity tEntity = trafficAgentMapper.selectByPrimaryKey(id);
        if (tEntity != null) {
            trafficAgentEntity.setId(tEntity.getId());
            trafficAgentEntity.setFullSpell(PinyinUtils.getPingYin(trafficAgentEntity.getTitle()));
            trafficAgentEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(trafficAgentEntity.getTitle()));
            trafficAgentEntity.setCreatedUser(tEntity.getCreatedUser());
            trafficAgentEntity.setCreatedDate(tEntity.getCreatedDate());
            trafficAgentEntity.setStatus(1);
            trafficAgentEntity.setCode(tEntity.getCode());
            trafficAgentEntity.setDeptCode(tEntity.getDeptCode());
            trafficAgentEntity.setUpdatedUser(user.getUsername());
            trafficAgentEntity.setUpdatedDate(new Date());
            trafficAgentMapper.updateByPrimaryKey(trafficAgentEntity);
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
    }

    @Override
    public ResponseMessage delete(String id) {
        trafficAgentMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功！");
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            List<TrafficAgentEntity> trafficAgentEntities = trafficAgentMapper.checkTitle(title);
            if(trafficAgentEntities.size() > 1){
                return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
            } else if (trafficAgentEntities.size() == 1){
                if (!trafficAgentEntities.get(0).getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }

        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) {
        TrafficAgentEntity tEntity= trafficAgentMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (tEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
        }
        String msg = "审核成功！";
        if (type == 1) {
            //上线
            if (auditLogEntity.getStatus() == 1 || auditLogEntity.getStatus() == 9) {
                msg = auditLogEntity.getStatus() == 1 ? "下线成功！" : "上线成功！";
            } else {
                return ResponseMessage.validFailResponse().setMsg("上下线状态错误！");
            }
        }
        tEntity.setStatus(auditLogEntity.getStatus());
        tEntity.setUpdatedUser(user.getUsername());
        tEntity.setUpdatedDate(new Date());
        trafficAgentMapper.updateByPrimaryKey(tEntity);
        auditLogEntity.setType(type);
        auditLogService.create(auditLogEntity, user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public void dataBind(String updatedUser, String updatedDate, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        trafficAgentMapper.dataBind(updatedUser, updatedDate, deptCode, ids);
    }

    @Override
    public ResponseMessage getTrafficAgentList() {
        return ResponseMessage.defaultResponse().setData(trafficAgentMapper.getTrafficAgentList());
    }


    @Override
    public ResponseMessage findBySearchValue(String name, String ids) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> idList = null;
        if(StringUtil.isNotEmpty(ids)){
            idList = Arrays.asList(ids.split(","));
        }
        List<TrafficAgentEntity> list = trafficAgentMapper.findBySearchValue(name, idList);
        if (!list.isEmpty()) {
            for (TrafficAgentEntity entity : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", entity.getId());
                map.put("unicode", entity.getCode());
                map.put("name", entity.getTitle());
                map.put("level", null);
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
        }else {
            responseMessage.setData("暂无数据");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage saveGouldTrafficData(GouldModel gouldModel,User user, Long ruleId, Integer appCode) {
        JSONArray jsonArray = JSONObject.parseArray(gouldModel.getData());
        jsonArray.forEach(array->{
            JSONObject jsonObject = (JSONObject)array;
            String gouldId = jsonObject.getString("id");
            List<TrafficAgentEntity>  trafficAgentList = trafficAgentMapper.findByGouldId(gouldId);
            if(null == trafficAgentList || trafficAgentList.size() == 0){
                TrafficAgentEntity trafficAgentEntity = new TrafficAgentEntity();
                String id = UUIDUtils.getInstance().getId();
                trafficAgentEntity.setId(id);
                trafficAgentEntity.setTitle(jsonObject.getString("name"));
                trafficAgentEntity.setType(gouldModel.getType());
                trafficAgentEntity.setGouldId(jsonObject.getString("id"));
                trafficAgentEntity.setLatitude(jsonObject.getJSONObject("location").getDouble("lat"));
                trafficAgentEntity.setLongitude(jsonObject.getJSONObject("location").getDouble("lng"));
                trafficAgentEntity.setAddress(jsonObject.getString("address"));
                trafficAgentEntity.setPhone(jsonObject.getString("tel"));
                trafficAgentEntity.setRegion(jsonObject.getString("adcode"));
                trafficAgentEntity.setRegionFullCode(String.format("%s,%s,%s",jsonObject.getString("adcode").substring(0,2),jsonObject.getString("adcode").substring(0,4),jsonObject.getString("adcode").substring(0,6)));
                trafficAgentEntity.setRegionFullName(String.format("%s,%s,%s",jsonObject.getString("pname"),jsonObject.getString("cityname"),jsonObject.getString("adname")));
                trafficAgentEntity.setStatus(0);
                trafficAgentEntity.setWeight(0);
                trafficAgentEntity.setCreatedUser(user.getUsername());
                trafficAgentEntity.setCreatedDate(new Date());
                trafficAgentEntity.setDeptCode(user.getOrg().getCode());
                trafficAgentEntity.setFullSpell(PinyinUtils.getPingYin(trafficAgentEntity.getTitle()));
                trafficAgentEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(trafficAgentEntity.getTitle()));
                ResponseMessage responseMessageOnlyCode = coderServiceFeign.buildSerialByCode(ruleId, appCode,"JTSN");
                if (responseMessageOnlyCode.getStatus() == 1 && null != responseMessageOnlyCode.getData()) {
                    trafficAgentEntity.setCode(responseMessageOnlyCode.getData().toString());
                }
                trafficAgentMapper.insert(trafficAgentEntity);
            }
        });
        return ResponseMessage.defaultResponse().setMsg("高德交通数据批量保存成功!");

    }
}
