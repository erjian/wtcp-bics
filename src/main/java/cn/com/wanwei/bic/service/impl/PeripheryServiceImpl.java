package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.CateRelationMapper;
import cn.com.wanwei.bic.mapper.MaterialMapper;
import cn.com.wanwei.bic.mapper.PeripheryMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RefreshScope
@SuppressWarnings("all")
public class PeripheryServiceImpl implements PeripheryService {

    @Autowired
    private PeripheryMapper peripheryMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private CateRelationService cateRelationService;

    private static final String FOOD_TYPE = "125006002";            //  餐饮
    private static final String SHOPPING_TYPE = "125006004";     // 购物

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title", "category");
        String regionFullCodeKey = "regionFullCode";
        Object fullCode = filter.get(regionFullCodeKey);
        if(filter.containsKey(regionFullCodeKey) && null != fullCode){
            filter.put(regionFullCodeKey, fullCode.toString().split(",")[fullCode.toString().split(",").length-1]);
        }
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<PeripheryEntity> peripheryEntities = peripheryMapper.findByPage(filter);
        PageInfo<PeripheryEntity> pageInfo = new PageInfo<>(peripheryEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        PeripheryEntity entity = peripheryMapper.selectByPrimaryKey(id);
        List<String> cateIds = cateRelationService.findByCateringId(id);
        entity.setCateIds(cateIds);
        if (entity != null) {
            responseMessage.setData(entity);
        } else {
            responseMessage.setStatus(0).setMsg("暂无该周边信息");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage save(EntityTagsModel<PeripheryEntity> peripheryModel, User user, Long ruleId, Integer appCode) {
        PeripheryEntity peripheryEntity = peripheryModel.getEntity();
        String type = peripheryModel.getType();
        String id = UUIDUtils.getInstance().getId();
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode = coderServiceFeign.buildSerialByCode(ruleId, appCode, type);
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            peripheryEntity.setId(id);
            peripheryEntity.setCode(responseMessageGetCode.getData().toString());
            peripheryEntity.setStatus(1);
            peripheryEntity.setWeight(0);
            peripheryEntity.setDeptCode(user.getOrg().getCode());
            peripheryEntity.setCreatedUser(user.getUsername());
            peripheryEntity.setCreatedDate(new Date());
            peripheryMapper.insert(peripheryEntity);

            // 保存关联的美食
            if (null != peripheryEntity.getCateIds() && CollectionUtils.isNotEmpty(peripheryEntity.getCateIds())) {
                cateRelationService.batchInsert(peripheryEntity.getCateIds(), id);
            }

            //处理标签
            if (CollectionUtils.isNotEmpty(peripheryModel.getTagsList())) {
                tagsService.batchInsert(peripheryEntity.getId(), peripheryModel.getTagsList(), user, PeripheryTagsEntity.class);
            }
            //处理编辑页面新增素材
            if (CollectionUtils.isNotEmpty(peripheryModel.getMaterialList())) {
                materialService.batchInsert(id, peripheryModel.getMaterialList(), user);
            }
            return ResponseMessage.defaultResponse().setMsg("保存成功!");
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage edit(String id, EntityTagsModel<PeripheryEntity> peripheryModel, User user) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        PeripheryEntity peripheryEntity = peripheryModel.getEntity();
        PeripheryEntity entity = peripheryMapper.selectByPrimaryKey(id);
        if (entity != null) {
            peripheryEntity.setUpdatedUser(user.getUsername());
            peripheryEntity.setUpdatedDate(new Date());
            peripheryEntity.setId(entity.getId());
            peripheryEntity.setDeptCode(entity.getDeptCode());
            peripheryEntity.setStatus(1);
            peripheryEntity.setCode(entity.getCode());
            peripheryMapper.updateByPrimaryKey(peripheryEntity);

            // 先删除之前关联的美食，再保存关联的美食
            cateRelationService.deleteByCateringId(id);
            if (null != peripheryEntity.getCateIds() && CollectionUtils.isNotEmpty(peripheryEntity.getCateIds())) {
                cateRelationService.batchInsert(peripheryEntity.getCateIds(), id);
            }

            //处理标签
            if (CollectionUtils.isNotEmpty(peripheryModel.getTagsList())) {
                tagsService.batchInsert(peripheryEntity.getId(), peripheryModel.getTagsList(), user, PeripheryTagsEntity.class);
            }

            responseMessage.setMsg("更新成功");
        } else {
            responseMessage.setStatus(0).setMsg("暂无该周边信息");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage delete(String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        PeripheryEntity entity = peripheryMapper.selectByPrimaryKey(id);
        if (entity.getStatus() == 9) {
            responseMessage.setStatus(0).setMsg("已上线，禁止删除");
        } else {
            peripheryMapper.deleteByPrimaryKey(id);
            responseMessage.setMsg("删除成功");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int audit) {
        PeripheryEntity entity = peripheryMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (entity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该周边信息！");
        }
        String msg = "审核成功！";
        if (audit == 1) {
            //上线
            if (auditLogEntity.getPreStatus() == 0 || auditLogEntity.getPreStatus() == 2) {
                return ResponseMessage.validFailResponse().setMsg("当前信息未审核通过，不可上下线操作！");
            } else {
                if (auditLogEntity.getStatus() == 1 || auditLogEntity.getStatus() == 9) {
                    msg = auditLogEntity.getStatus() == 1 ? "下线成功！" : "上线成功！";
                } else {
                    return ResponseMessage.validFailResponse().setMsg("上下线状态错误！");
                }
            }
        } else {
            //审核
            if (auditLogEntity.getStatus() == 9) {
                return ResponseMessage.validFailResponse().setMsg("审核状态错误！");
            }
        }
        entity.setStatus(auditLogEntity.getStatus());
        entity.setDeptCode(user.getOrg().getCode());
        entity.setUpdatedUser(user.getUsername());
        entity.setUpdatedDate(new Date());
        peripheryMapper.updateByPrimaryKey(entity);
        auditLogEntity.setType(audit);
        auditLogService.create(auditLogEntity, user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            PeripheryEntity pEntity = peripheryMapper.checkTitle(title);
            if (pEntity != null) {
                if (!pEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage batchDelete(List<String> ids) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        for (String id : ids) {
            PeripheryEntity entity = peripheryMapper.selectByPrimaryKey(id);
            if (entity.getStatus() == 9) {
                return responseMessage.setStatus(0).setMsg("所选数据中存在已上线数据，批量删除取消！");
            }
        }
        peripheryMapper.batchDelete(ids);
        return responseMessage.setStatus(1).setMsg("批量删除成功");
    }

    @Override
    public int dataBind(String updatedUser, String updatedDate, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        return peripheryMapper.dataBind(updatedUser, updatedDate, deptCode, ids);
    }

    @Override
    public ResponseMessage goWeight(WeightModel weightModel, User user) {
        //查出最大权重
        Integer maxNum = peripheryMapper.maxWeight();
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {
                peripheryMapper.clearWeight();
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                PeripheryEntity peripheryEntity = peripheryMapper.selectByPrimaryKey(ids.get(i));
                if (weightModel.isFlag()) {
                    peripheryEntity.setWeight(ids.size() - i);
                } else {
                    peripheryEntity.setWeight(maxNum + ids.size() - i);
                }
                peripheryEntity.setDeptCode(user.getOrg().getCode());
                peripheryEntity.setUpdatedUser(user.getUsername());
                peripheryEntity.setUpdatedDate(new Date());
                peripheryMapper.updateByPrimaryKey(peripheryEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage relateTags(Map<String, Object> tags, User currentUser) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<BaseTagsEntity> list = (List<BaseTagsEntity>) tags.get("tagsArr");
        ObjectMapper mapper = new ObjectMapper();
        List<BaseTagsEntity> tagsList = mapper.convertValue(list, new TypeReference<List<BaseTagsEntity>>() {
        });
        if (CollectionUtils.isNotEmpty(tagsList)) {
            tagsService.batchInsert(tags.get("id").toString(), tagsList, currentUser, PeripheryTagsEntity.class);
        }
        return responseMessage.setMsg("关联标签成功");
    }

    @Override
    public ResponseMessage findById(String id) {
        PeripheryEntity peripheryEntity = peripheryMapper.selectByPrimaryKey(id);
        if (peripheryEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("该周边管理信息不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("peripheryEntity", peripheryEntity);
        //获取素材
        map.put("fileList", materialService.handleMaterialNew(id));
        return ResponseMessage.defaultResponse().setData(map);
    }

    @Override
    public ResponseMessage findBySearchValue(String type, String name, String ids) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<String> idList = null;
        if (StringUtil.isNotEmpty(ids)) {
            idList = Arrays.asList(ids.split(","));
        }
        List<PeripheryEntity> list = peripheryMapper.findBySearchValue(type, name, idList);
        List<Map<String, Object>> data = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (PeripheryEntity entity : list) {
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
                map.put("pinyin", null);
                map.put("pinyinqp", null);
                data.add(map);
            }
            responseMessage.setData(data);
        } else {
            responseMessage.setData("暂无数据");
        }
        return responseMessage;
    }

    @Override
    public List<PeripheryEntity> findByIds(Map<String, Object> filter) {
        return peripheryMapper.findByIds(filter);
    }

    @Override
    public List<PeripheryEntity> findByCategory(Map<String, Object> filter) {
        return peripheryMapper.findByCategory(filter);
    }
}
