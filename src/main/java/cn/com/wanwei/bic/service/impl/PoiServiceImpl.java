package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.MaterialMapper;
import cn.com.wanwei.bic.mapper.PoiMapper;
import cn.com.wanwei.bic.mapper.ScenicMapper;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.PoiService;
import cn.com.wanwei.bic.service.TagsService;
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
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - PoiServiceImpl poi管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
@SuppressWarnings("all")
public class PoiServiceImpl implements PoiService {

    @Autowired
    private PoiMapper poiMapper;

    @Autowired
    private ScenicMapper scenicMapper;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Value("${wtcp.bic.appCode}")
    protected Integer appId;

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        return getPageInfo(page, size, filter,null);
    }

    @Override
    public ResponseMessage findByPageForFeign(Integer page, Integer size, Map<String, Object> filter) {
        return getPageInfo(page, size, filter,"feign");
    }

    private ResponseMessage getPageInfo(Integer page, Integer size, Map<String, Object> filter, String type){
        EscapeCharUtils.escape(filter, "title");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "weight", "created_date");
        Page<PoiEntity> poiEntities = null;
        if(StringUtils.isNotEmpty(type) && "feign".equalsIgnoreCase(type)){
            poiEntities = poiMapper.findByPageForFeign(filter);
        }else{
            poiEntities = poiMapper.findByPage(filter);
        }
        PageInfo<PoiEntity> pageInfo = new PageInfo<>(poiEntities, pageRequest);
        for (PoiEntity entity : pageInfo.getContent()) {
            ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(entity.getPrincipalId());
            entity.setScenicName(scenicEntity.getTitle());
        }
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        try {
            PoiEntity poiEntity = poiMapper.selectByPrimaryKey(id);
            if (poiEntity == null) {
                return ResponseMessage.validFailResponse().setMsg("暂无该poi信息！");
            }
            return ResponseMessage.defaultResponse().setData(poiEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("获取失败！");
        }
    }

    @Override
    public ResponseMessage create(EntityTagsModel<PoiEntity> poiModel, User user, Long ruleId, Integer appCode) {
        try {
            PoiEntity poiEntity = poiModel.getEntity();
            //获取统一认证生成的code
            String type = poiModel.getType();
            ResponseMessage responseMessageGetCode = coderServiceFeign.buildSerialByCode(ruleId, appCode, type);
            if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
                poiEntity.setId(UUIDUtils.getInstance().getId());
                poiEntity.setCode(responseMessageGetCode.getData().toString());
                poiEntity.setStatus(1);
                poiEntity.setWeight(0);
                poiEntity.setCreatedUser(user.getUsername());
                poiEntity.setCreatedDate(new Date());
                ScenicEntity entity = scenicMapper.selectByPrimaryKey(poiEntity.getPrincipalId());
                poiEntity.setDeptCode(entity.getDeptCode());
                poiMapper.insert(poiEntity);
                //处理标签
                if(CollectionUtils.isNotEmpty(poiModel.getTagsList())){
                    tagsService.batchInsert(poiEntity.getId(), poiModel.getTagsList(),user,PoiTagsEntity.class);
                }
                //处理编辑页面新增素材
                materialMapper.batchUpdateByPrincipalId(poiEntity.getId(),poiEntity.getTimeId());
                return ResponseMessage.defaultResponse().setMsg("保存成功!");
            }
            return responseMessageGetCode;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("保存失败！");
        }
    }

    @Override
    public ResponseMessage update(String id, EntityTagsModel<PoiEntity> poiModel, User user) {
        PoiEntity poiEntity = poiModel.getEntity();
        PoiEntity pEntity = poiMapper.selectByPrimaryKey(id);
        ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(poiEntity.getPrincipalId());
        if (pEntity != null) {
            poiEntity.setId(id);
            poiEntity.setStatus(1);
            poiEntity.setCode(pEntity.getCode());
            poiEntity.setDeptCode(scenicEntity.getDeptCode());
            poiEntity.setUpdatedUser(user.getUsername());
            poiEntity.setUpdatedDate(new Date());
            poiMapper.updateByPrimaryKey(poiEntity);

            //处理标签
            if(CollectionUtils.isNotEmpty(poiModel.getTagsList())){
                tagsService.batchInsert(poiEntity.getId(), poiModel.getTagsList(),user,PoiTagsEntity.class);
            }
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该poi信息！");

    }

    @Override
    public ResponseMessage delete(String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        try {
            PoiEntity entity = poiMapper.selectByPrimaryKey(id);
            if (entity.getStatus() == 9) {
                responseMessage.setStatus(0).setMsg("已上线，禁止删除");
            } else {
                poiMapper.deleteByPrimaryKey(id);
                responseMessage.setMsg("删除成功！");
            }
            return responseMessage;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("删除失败！");
        }
    }

    @Override
    public ResponseMessage goWeight(String id, Integer weight, User user) {
        try {
            PoiEntity pntity = poiMapper.selectByPrimaryKey(id);
            if (pntity != null) {
                pntity.setWeight(weight);
                pntity.setDeptCode(user.getOrg().getCode());
                pntity.setUpdatedUser(user.getUsername());
                pntity.setUpdatedDate(new Date());
                poiMapper.updateByPrimaryKey(pntity);
                return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
            }
            return ResponseMessage.validFailResponse().setMsg("暂无该poi信息！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("权重修改失败！");
        }
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            PoiEntity pEntity = poiMapper.checkTitle(title);
            if (pEntity != null) {
                if (!pEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) {
        try {
            PoiEntity pntity = poiMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
            if (pntity == null) {
                return ResponseMessage.validFailResponse().setMsg("暂无该poi信息！");
            }
            String msg = "审核成功！";
            if (type == 1) {
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
            pntity.setStatus(auditLogEntity.getStatus());
            pntity.setDeptCode(user.getOrg().getCode());
            pntity.setUpdatedUser(user.getUsername());
            pntity.setUpdatedDate(new Date());
            poiMapper.updateByPrimaryKey(pntity);
            auditLogEntity.setType(type);
            auditLogService.create(auditLogEntity, user.getUsername());
            return ResponseMessage.defaultResponse().setMsg(msg);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("操作失败！");
        }
    }

    @Override
    public ResponseMessage findScenicList(String type,String principalId,String id) {
        List<PoiEntity> poiEntities = poiMapper.findScenicList(type,principalId,id);
        return ResponseMessage.defaultResponse().setData(poiEntities);
    }

    @Override
    public ResponseMessage batchDelete(List<String> ids) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        for (String id : ids) {
            PoiEntity entity = poiMapper.selectByPrimaryKey(id);
            if (entity.getStatus() == 9) {
                return responseMessage.setStatus(0).setMsg("所选数据中存在已上线数据，批量删除取消！");
            }
        }
        poiMapper.batchDelete(ids);
        return responseMessage.setStatus(1).setMsg("批量删除成功");
    }

    @Override
    public ResponseMessage relateTags(Map<String,Object> tags, User user){
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<BaseTagsEntity> list = (List<BaseTagsEntity>) tags.get("tagsArr");
        ObjectMapper mapper = new ObjectMapper();
        List<BaseTagsEntity> tagsList = mapper.convertValue(list, new TypeReference<List<BaseTagsEntity>>() { });
        if (CollectionUtils.isNotEmpty(tagsList)) {
            tagsService.batchInsert(tags.get("id").toString(), tagsList, user, PoiTagsEntity.class);
        }
        return responseMessage.setMsg("关联标签成功");
    }

    @Override
    public ResponseMessage getOne(String id) {
        //1、查询poi实体并判断是否存在
        PoiEntity poiEntity = poiMapper.selectByPrimaryKey(id);
        if(poiEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("该POI不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("poiEntity", poiEntity);

        //2、查询poi相关的素材信息
        map.put("fileList", materialService.handleMaterial(id));
        return ResponseMessage.defaultResponse().setData(map);
    }

    @Override
    public ResponseMessage getList(String principalId, String type) {
        List<Map<String, Object>> data = Lists.newArrayList();
        List<PoiEntity> list = poiMapper.getList(principalId, type);
        if(CollectionUtils.isNotEmpty(list)){
            for(PoiEntity entity:list){
                Map<String, Object> map = Maps.newHashMap();
                map.put("poiEntity", entity);
                map.put("fileList", materialService.handleMaterial(entity.getId()));
                data.add(map);
            }
        }
        return ResponseMessage.defaultResponse().setData(data);
    }


}
