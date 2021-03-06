/**
 * 该源代码文件 ScenicServiceImpl.java 是工程“wtcp-bics”的一部分。
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:53:22
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.*;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.ScenicService;
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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * wtcp-bics - ScenicServiceImpl 景区基础信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class ScenicServiceImpl implements ScenicService {

    @Autowired
    private ScenicMapper scenicMapper;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private PoiMapper poiMapper;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private MaterialService materialService;



    @Override
    public ResponseMessage save(EntityTagsModel<ScenicEntity> scenicModel, User user, Long ruleId, Integer appCode) {
        ScenicEntity record = scenicModel.getEntity();
        String type = scenicModel.getType();
        String id = UUIDUtils.getInstance().getId();
        record.setId(id);
        ResponseMessage result = coderServiceFeign.buildSerialByCode(ruleId, appCode, type);
        record.setCode(result.getData().toString());
        record.setTitleQp(PinyinUtils.getPingYin(record.getTitle()).toLowerCase());
        record.setTitleJp(PinyinUtils.converterToFirstSpell(record.getTitle()).toLowerCase());
        record.setCreatedUser(user.getUsername());
        record.setCreatedDate(new Date());
        record.setUpdatedDate(new Date());
        record.setDeptCode(user.getOrg().getCode());
        record.setStatus(0);
        record.setWeight(0);
        scenicMapper.insert(record);

        //处理标签
        if (CollectionUtils.isNotEmpty(scenicModel.getTagsList())) {
            tagsService.batchInsert(record.getId(), scenicModel.getTagsList(), user, ScenicTagsEntity.class);
        }
        //处理编辑页面新增素材
        if(CollectionUtils.isNotEmpty(scenicModel.getMaterialList())){
            materialService.batchInsert(id,scenicModel.getMaterialList(),user);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage deleteByPrimaryKey(String id) {
        scenicMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ScenicEntity selectByPrimaryKey(String id) {
        return scenicMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResponseMessage edit(String id, EntityTagsModel<ScenicEntity> scenicModel, User user) {
        ScenicEntity entity = scenicMapper.selectByPrimaryKey(id);
        ScenicEntity record = scenicModel.getEntity();
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("不存在该景区");
        }
        record.setId(id);
        record.setCode(entity.getCode());
        record.setCreatedDate(entity.getCreatedDate());
        record.setCreatedUser(entity.getCreatedUser());
        record.setDeptCode(entity.getDeptCode());
        record.setTitleQp(PinyinUtils.getPingYin(record.getTitle()).toLowerCase());
        record.setTitleJp(PinyinUtils.converterToFirstSpell(record.getTitle()).toLowerCase());
        record.setStatus(0);
        record.setUpdatedDate(new Date());
        record.setUpdatedUser(user.getUsername());
        scenicMapper.updateByPrimaryKeyWithBLOBs(record);

        //处理标签
        if (CollectionUtils.isNotEmpty(scenicModel.getTagsList())) {
            tagsService.batchInsert(record.getId(), scenicModel.getTagsList(), user, ScenicTagsEntity.class);
        }

        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage findByPage(Integer page, Integer size,Map<String, Object> filter) {
        return getPageInfo(page, size, filter,null);
    }

    @Override
    public ResponseMessage findByPageForFeign(Integer page, Integer size,Map<String, Object> filter) {
        return getPageInfo(page, size, filter,"feign");
    }

    private ResponseMessage getPageInfo(Integer page, Integer size, Map<String, Object> filter, String type){
        EscapeCharUtils.escape(filter, "title", "subTitle", "areaName");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<ScenicEntity> scenicEntities = null;
        if(StringUtils.isNotEmpty(type) && "feign".equalsIgnoreCase(type)){
            scenicEntities = scenicMapper.findByPageForFeign(filter);
            for(ScenicEntity item : scenicEntities){
                item.setTagsEntities(tagsService.findListByPriId(item.getId(), ScenicTagsEntity.class));
            }
        }else{
            scenicEntities = scenicMapper.findByPage(filter);
        }
        PageInfo<ScenicEntity> pageInfo = new PageInfo<>(scenicEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage dataBind(String updatedUser, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        scenicMapper.dataBind(updatedUser, new Date(), deptCode, ids);
        poiMapper.dataBind(updatedUser, new Date(), deptCode, ids);
        return ResponseMessage.defaultResponse().setMsg("关联机构成功");
    }

    @Override
    public ResponseMessage changeStatus(String id, Integer status, String username) throws Exception {
        ScenicEntity entity = scenicMapper.selectByPrimaryKey(id);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("无景区信息");
        }
        if (status == 9 && entity.getStatus() != 1) {
            return ResponseMessage.validFailResponse().setMsg("景区未审核通过，不能上线，请先审核景区信息");
        }
        // 添加上下线记录
        String msg = status == 9 ? "上线成功" : "下线成功";
        saveAuditLog(entity.getStatus(), status, id, username, msg, 1);
        entity.setUpdatedUser(username);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        scenicMapper.updateByPrimaryKeyWithBLOBs(entity);
        return ResponseMessage.defaultResponse().setMsg("状态变更成功");
    }

    @Override
    public ResponseMessage examineScenic(String id, int auditStatus, String msg, User currentUser) throws Exception {
        ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(id);
        if (scenicEntity != null) {
            // 添加审核记录
            saveAuditLog(scenicEntity.getStatus(), auditStatus, id, currentUser.getUsername(), msg, 0);
            scenicEntity.setStatus(auditStatus);
            scenicEntity.setUpdatedDate(new Date());
            scenicMapper.updateByPrimaryKeyWithBLOBs(scenicEntity);
        } else {
            return ResponseMessage.validFailResponse().setMsg("景区信息不存在");
        }
        return ResponseMessage.defaultResponse();
    }

    @Override
    public ResponseMessage getScenicInfo(String title, Integer status, String category) {
        List<ScenicEntity> entities = scenicMapper.getScenicInfo(title, status,category);
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
            tagsService.batchInsert(tags.get("id").toString(), bList, user, ScenicTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    @Override
    public ResponseMessage getOne(String id) throws Exception {
        //1、查询景区信息，若存在，则添加到返回数据中
        ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(id);
        if (scenicEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("无景区信息");
        }

        Map<String, Object> data = Maps.newHashMap();
        scenicEntity.setTagsEntities(tagsService.findListByPriId(id, ScenicTagsEntity.class));
        data.put("scenicEntity", scenicEntity);

        //2、查询企业信息并添加到返回数据中
        EnterpriseEntity enterpriseEntity = enterpriseMapper.selectByPrincipalId(id);
        data.put("enterpriseEntity", enterpriseEntity);

        //3、查询营业信息并添加到返回数据中
        BusinessEntity businessEntity = businessMapper.findByPrincipalId(id);
        data.put("businessEntity", businessEntity);

        //4、查询通讯信息并添加到返回数据中
        ContactEntity contactEntity = contactMapper.selectByPrincipalId(id);
        data.put("contactEntity", contactEntity);

        //5、查询素材信息，按照素材类型分类处理并添加到返回数据中
        data.put("fileList", materialService.handleMaterialNew(id));
        return ResponseMessage.defaultResponse().setData(data);
    }

    @Override
    public ResponseMessage findByTitleAndIdNot(String title, String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        try {
            List<ScenicEntity> scenicEntities = scenicMapper.findByTitleAndIdNot(title, id);
            if (CollectionUtils.isNotEmpty(scenicEntities)) {
                responseMessage.setStatus(ResponseMessage.FAILED).setMsg("该名称已经存在！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg(e.getMessage());
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findBySearchValue(String type,String name, String ids) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> idList = null;
        if(StringUtil.isNotEmpty(ids)){
            idList = Arrays.asList(ids.split(","));
        }
        List<ScenicEntity> list = scenicMapper.findBySearchValue(type,name,idList);
        if (!list.isEmpty()) {
            for (ScenicEntity entity : list) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", entity.getId());
                map.put("unicode", entity.getCode());
                map.put("name", entity.getTitle());
                map.put("level", entity.getLevel());
                map.put("longitude", entity.getLongitude());
                map.put("latitude", entity.getLatitude());
                map.put("areaCode", entity.getRegion());
                map.put("areaName", entity.getRegionFullName());
                map.put("address", entity.getAddress());
                map.put("pinyin", entity.getTitleJp());
                map.put("pinyinqp", entity.getTitleQp());
                data.add(map);
            }
            responseMessage.setData(data);
        } else {
            responseMessage.setData("暂无数据");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage scenicPageNew(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title", "subTitle");
        if (filter.get("ids") != null) {
            List<String> ids = Arrays.asList(filter.get("ids").toString().split(","));
            filter.put("ids", ids);
        }
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "weight");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        Page<ScenicEntity> scenicEntities = scenicMapper.scenicPageNew(filter);
        PageInfo<ScenicEntity> pageInfo = new PageInfo<>(scenicEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage findListByIds(String ids,Integer status) {
        List<String> idList = Arrays.asList(ids.split(","));
        List<ScenicEntity> entitiesList = scenicMapper.findListByIds(idList,status);
        for (ScenicEntity scenicEntity : entitiesList) {
            ResponseMessage responseMessage = tagsService.findByPrincipalId(scenicEntity.getId(), EntertainmentTagsEntity.class);
            List<ScenicTagsEntity> tagsEntityList = (List<ScenicTagsEntity>) responseMessage.getData();
            scenicEntity.setTagsEntities(tagsEntityList);
            scenicEntity.setFileList(materialService.handleMaterialNew(scenicEntity.getId()));
        }
        return ResponseMessage.defaultResponse().setData(entitiesList);
    }

    @Override
    public ResponseMessage findByDeptCode(String deptCode) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<ScenicEntity> scenicList = scenicMapper.findByDeptCode(deptCode);
        if (CollectionUtils.isEmpty(scenicList)){
            return ResponseMessage.validFailResponse().setMsg("当前机构暂未绑定景区数据！");
        }else if(scenicList.size() > 1){
            return ResponseMessage.validFailResponse().setMsg("当前机构绑定景区数据错误，请重新绑定！");
        }else{
            ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(scenicList.get(0).getId());
            scenicEntity.setTagsEntities(tagsService.findListByPriId(scenicList.get(0).getId(),ScenicTagsEntity.class));
            responseMessage.setData(scenicEntity);
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
