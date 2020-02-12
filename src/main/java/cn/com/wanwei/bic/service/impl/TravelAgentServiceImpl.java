package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.*;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.service.TravelAgentService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
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
public class TravelAgentServiceImpl implements TravelAgentService {

    @Autowired
    private TravelAgentMapper tarvaAgentMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private MaterialMapper materialMapper;


    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title", "subTitle");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size,filter, Sort.Direction.DESC,  "created_date", "updated_date");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        Page<TravelAgentEntity> travelAgentEntities = tarvaAgentMapper.findByPage(filter);
        PageInfo<TravelAgentEntity> pageInfo = new PageInfo<>(travelAgentEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        TravelAgentEntity travelAgentEntity = tarvaAgentMapper.selectByPrimaryKey(id);
        if (travelAgentEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该旅行社信息！");
        }
        return ResponseMessage.defaultResponse().setData(travelAgentEntity);
    }

    @Override
    public ResponseMessage create(EntityTagsModel<TravelAgentEntity> travelAgentModel, User user, Long ruleId, Integer appCode) {
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode =coderServiceFeign.buildSerialByCode(ruleId, appCode, travelAgentModel.getType());
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            TravelAgentEntity travelAgentEntity = travelAgentModel.getEntity();
            String id= UUIDUtils.getInstance().getId();
            travelAgentEntity.setId(id);
            travelAgentEntity.setFullSpell(PinyinUtils.getPingYin(travelAgentEntity.getTitle()));
            travelAgentEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(travelAgentEntity.getTitle()));
            travelAgentEntity.setCode(responseMessageGetCode.getData().toString());
            travelAgentEntity.setStatus(0);
            travelAgentEntity.setWeight(0);
            travelAgentEntity.setCreatedUser(user.getUsername());
            travelAgentEntity.setCreatedDate(new Date());
            travelAgentEntity.setUpdatedDate(new Date());
            travelAgentEntity.setDeptCode(user.getOrg().getCode());
            tarvaAgentMapper.insert(travelAgentEntity);
            //处理标签
            if(CollectionUtils.isNotEmpty(travelAgentModel.getTagsList())){
                tagsService.batchInsert(id,travelAgentModel.getTagsList(),user, TravelAgentTagsEntity.class);
            }

            //处理编辑页面新增素材
            if(CollectionUtils.isNotEmpty(travelAgentModel.getMaterialList())){
                materialService.batchInsert(id,travelAgentModel.getMaterialList(),user);
            }
            return ResponseMessage.defaultResponse().setMsg("保存成功!").setData(id);
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage update(String id, EntityTagsModel<TravelAgentEntity> travelAgentModel, User user) {
        TravelAgentEntity tEntity = tarvaAgentMapper.selectByPrimaryKey(id);
        if (tEntity != null) {
            TravelAgentEntity travelAgentEntity = travelAgentModel.getEntity();
            travelAgentEntity.setId(tEntity.getId());
            travelAgentEntity.setFullSpell(PinyinUtils.getPingYin(travelAgentEntity.getTitle()));
            travelAgentEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(travelAgentEntity.getTitle()));
            travelAgentEntity.setCreatedUser(tEntity.getCreatedUser());
            travelAgentEntity.setCreatedDate(tEntity.getCreatedDate());
            travelAgentEntity.setStatus(0);
            travelAgentEntity.setCode(tEntity.getCode());
            travelAgentEntity.setUpdatedUser(user.getUsername());
            travelAgentEntity.setUpdatedDate(new Date());
            tarvaAgentMapper.updateByPrimaryKey(travelAgentEntity);
            //处理标签
            if(CollectionUtils.isNotEmpty(travelAgentModel.getTagsList())){
                tagsService.batchInsert(id,travelAgentModel.getTagsList(),user, TravelAgentTagsEntity.class);
            }
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该旅行社信息！");
    }

    @Override
    public ResponseMessage delete(String id) {
        tarvaAgentMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功！");
    }

    @Override
    public ResponseMessage goWeight(WeightModel weightModel, User user) {
        //查出最大权重
        Integer maxNum= tarvaAgentMapper.maxWeight();
        List<String>ids =weightModel.getIds();
        if(ids!=null&&!ids.isEmpty()){
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if(weightModel.isFlag()||(maxNum+ids.size())>Integer.MAX_VALUE){
                tarvaAgentMapper.clearWeight();
                maxNum=0;
            }
            for(int i=0;i<ids.size();i++){
                TravelAgentEntity travelAgentEntity= tarvaAgentMapper.selectByPrimaryKey(ids.get(i));
                travelAgentEntity.setWeight(maxNum+ids.size()-i);
                travelAgentEntity.setUpdatedUser(user.getUsername());
                travelAgentEntity.setUpdatedDate(new Date());
                tarvaAgentMapper.updateByPrimaryKey(travelAgentEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            TravelAgentEntity travelAgentEntity = tarvaAgentMapper.checkTitle(title);
            if (travelAgentEntity != null) {
                if (!travelAgentEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) {
        TravelAgentEntity tEntity = tarvaAgentMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (tEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该旅行社信息！");
        }
        String msg="审核成功！";
        if(type==1){
            //上线
            if(auditLogEntity.getPreStatus()==0||auditLogEntity.getPreStatus()==2){
                return ResponseMessage.validFailResponse().setMsg("请先审核通过后，再进⾏上线操作！");
            }else {
                if(auditLogEntity.getStatus()==1||auditLogEntity.getStatus()==9){
                    msg=auditLogEntity.getStatus()==1?"下线成功！":"上线成功！";
                }else{
                    return ResponseMessage.validFailResponse().setMsg("上下线状态错误！");
                }
            }
        }else {
            //审核
            if(auditLogEntity.getStatus()==9){
                return ResponseMessage.validFailResponse().setMsg("审核状态错误！");
            }
        }
        tEntity.setStatus(auditLogEntity.getStatus());
        tEntity.setUpdatedUser(user.getUsername());
        tEntity.setUpdatedDate(new Date());
        tarvaAgentMapper.updateByPrimaryKey(tEntity);
        auditLogEntity.setType(type);
        auditLogService.create(auditLogEntity,user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public void dataBind(String updatedUser, String updatedDate, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        tarvaAgentMapper.dataBind(updatedUser, updatedDate, deptCode, ids);
    }

    @Override
    public ResponseMessage relateTags(String id, List<BaseTagsEntity> list, User user) {
        if(CollectionUtils.isNotEmpty(list)){
            tagsService.batchInsert(id,list,user, TravelAgentTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("关联成功！");
    }

    @Override
    public ResponseMessage getTravelAgentList() {
        return ResponseMessage.defaultResponse().setData(tarvaAgentMapper.getTravelAgentList());
    }

    @Override
    public ResponseMessage getTravelAgentInfo(String id) {
        Map<String,Object>map= Maps.newHashMap();
        TravelAgentEntity travelAgentEntity = tarvaAgentMapper.selectByPrimaryKey(id);
        if (travelAgentEntity != null) {
            map.put("travelAgentEntity",travelAgentEntity);
            //企业信息
            EnterpriseEntity enterpriseEntity = enterpriseMapper.selectByPrincipalId(id);
            map.put("enterpriseEntity",enterpriseEntity);
            //营业信息
            BusinessEntity businessEntity = businessMapper.selectByPrincipalId(id);
            map.put("businessEntity", businessEntity);
            //通讯信息
            ContactEntity contactEntity = contactMapper.selectByPrincipalId(id);
            map.put("contactEntity",contactEntity);
            //素材信息
            map.put("fileList",materialService.handleMaterialNew(id));
            return ResponseMessage.defaultResponse().setData(map);
        }else{
            return ResponseMessage.validFailResponse().setMsg("暂无该旅行社信息！");
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
        List<TravelAgentEntity> list = tarvaAgentMapper.findBySearchValue(name, idList);
        if (!list.isEmpty()) {
            for (TravelAgentEntity entity : list) {
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
        }else {
            responseMessage.setData("暂无数据");
        }
        return responseMessage;
    }
}
