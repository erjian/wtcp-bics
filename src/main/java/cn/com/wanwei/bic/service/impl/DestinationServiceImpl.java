package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.DestinationEntity;
import cn.com.wanwei.bic.entity.DestinationTagsEntity;
import cn.com.wanwei.bic.mapper.DestinationMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.DestinationService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/10 9:30:30
 * @desc wtcp-bics - DestinationServiceImpl 目的地基础信息管理接口实现类.
 */
@Service
@Slf4j
@RefreshScope
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationMapper destinationMapper;

    @Autowired
    AuditLogServiceImpl auditLogService;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    /**
     * 查询目的地分页列表数据
     * @param page  页数
     * @param size  条数
     * @param user  用户信息
     * @param filter   查询参数
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage findByPage(Integer page, Integer size, User user, Map<String, Object> filter) throws Exception {
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<DestinationEntity> DestinationEntities = destinationMapper.findByPage(filter);
        PageInfo<DestinationEntity> pageInfo = new PageInfo<>(DestinationEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    /**
     * 目的地基础信息新增
     * @param destinationModel
     * @param user
     * @return
     */
    @Override
    public ResponseMessage save(EntityTagsModel<DestinationEntity> destinationModel, User user) throws Exception{
        DestinationEntity destinationEntity = destinationModel.getEntity();
        destinationEntity.setId(UUIDUtils.getInstance().getId());
        destinationEntity.setCreatedUser(user.getUsername());
        destinationEntity.setCreatedDate(new Date());
        destinationEntity.setStatus(1);
        destinationEntity.setWeight(0);
        destinationEntity.setDeptCode(user.getOrg().getCode());
        destinationMapper.insert(destinationEntity);

        //处理标签
        if(CollectionUtils.isNotEmpty(destinationModel.getTagsList())){
            tagsService.batchInsert(destinationEntity.getId(),destinationModel.getTagsList(),user, DestinationTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功!");
    }

    /**
     *
     * @param id  主键ID
     * @param destinationModel
     * @param user
     * @return
     */
    @Override
    public ResponseMessage edit(String id, EntityTagsModel<DestinationEntity> destinationModel, User user)throws Exception {
        DestinationEntity entity = destinationMapper.selectByPrimaryKey(id);
        if(null == entity){
            return ResponseMessage.validFailResponse().setMsg("不存在该目的地！");
        }
        DestinationEntity destinationEntity = destinationModel.getEntity();
        destinationEntity.setId(id);
        destinationEntity.setDeptCode(entity.getDeptCode());
        destinationEntity.setCreatedDate(entity.getCreatedDate());
        destinationEntity.setCreatedUser(entity.getCreatedUser());
        destinationEntity.setStatus(1);  //编辑修改状态为--> 1: 下线
        destinationEntity.setUpdatedDate(new Date());
        destinationEntity.setUpdatedUser(user.getUsername());
        destinationMapper.updateByPrimaryKey(destinationEntity);

        //处理标签
        if(CollectionUtils.isNotEmpty(destinationModel.getTagsList())){
            tagsService.batchInsert(destinationEntity.getId(),destinationModel.getTagsList(),user, DestinationTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("更新成功!");
    }

    /**
     * 根据目的地id查询目的地信息
     * @param id   主键id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage selectByPrimaryKey(String id) throws Exception{
        DestinationEntity destinationEntity = destinationMapper.selectByPrimaryKey(id);
        if(destinationEntity==null){
            return ResponseMessage.validFailResponse().setMsg("暂无该目的地信息！");
        }
        return ResponseMessage.defaultResponse().setData(destinationEntity);
    }

    /**
     * 删除目的地信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage deleteByPrimaryKey(String id) throws Exception {
        destinationMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功!");
    }

    /**
     * 目的地信息审核/上线
     * @param id  目的地id
     * @param username
     * @param type  操作类型: 0-审核  1-上线
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage changeStatus(String id, String username, int type) throws Exception{
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        DestinationEntity destinationEntity = destinationMapper.selectByPrimaryKey(id);
        if(destinationEntity == null){
            return ResponseMessage.validFailResponse().setMsg("无目的地信息！");
        }
        auditLogEntity.setPrincipalId(id);
        auditLogEntity.setType(type);
        //更新目的地状态
        destinationEntity.setId(id);
        destinationEntity.setUpdatedUser(username);
        destinationEntity.setUpdatedDate(new Date());
        if(type == 1 ){
            if(destinationEntity.getStatus() == 1){
                responseMessage.setMsg("上线成功!");
                destinationEntity.setStatus(9);
                auditLogEntity.setPreStatus(1);
                auditLogEntity.setStatus(destinationEntity.getStatus());
            }else{
                responseMessage.setMsg("下线成功!");
                destinationEntity.setStatus(1);
                auditLogEntity.setPreStatus(9);
                auditLogEntity.setStatus(destinationEntity.getStatus());
            }
        }
        destinationMapper.updateByPrimaryKey(destinationEntity);
        // 记录审核/上线流水操作
        auditLogService.create(auditLogEntity,username);
        return responseMessage;
    }

    /**
     * 校验目的地名称的唯一性
     * @param id   目的地id
     * @param regionFullCode   目的地编码
     * @return
     */
    @Override
    public ResponseMessage checkRegionFullName(String id, String regionFullCode) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(regionFullCode)) {
            DestinationEntity destinationEntity = destinationMapper.checkRegionFullCode(regionFullCode);
            if (destinationEntity != null) {
                if (!destinationEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("该目的地已经存在！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage relateTags(Map<String, Object> tags, User user) {
        List<BaseTagsEntity> list = (List<BaseTagsEntity>) tags.get("tagsArr");
        ObjectMapper mapper = new ObjectMapper();
        List<BaseTagsEntity> tagsList = mapper.convertValue(list, new TypeReference<List<BaseTagsEntity>>() { });
        if (CollectionUtils.isNotEmpty(tagsList)) {
            tagsService.batchInsert(tags.get("id").toString(),tagsList,user, DestinationTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    @Override
    public ResponseMessage dataBind(String updatedUser, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        destinationMapper.dataBind(updatedUser, new Date(), deptCode, ids);
        return ResponseMessage.defaultResponse().setMsg("关联机构成功");
    }

    @Override
    public ResponseMessage changeWeight(WeightModel weightModel, User user) {
        //查出最大权重
        Integer maxNum = destinationMapper.maxWeight();
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {
                destinationMapper.clearWeight();
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                DestinationEntity destinationEntity = destinationMapper.selectByPrimaryKey(ids.get(i));
                destinationEntity.setWeight(maxNum + ids.size() - i);
                destinationEntity.setUpdatedUser(user.getUsername());
                destinationEntity.setUpdatedDate(new Date());
                destinationMapper.updateByPrimaryKey(destinationEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage getDestinationDetail(String region,String id) {
        Map<String,Object>map= Maps.newHashMap();
        DestinationEntity destinationEntity;
        if(StringUtils.isNotBlank(id)){
             destinationEntity = destinationMapper.selectByPrimaryKey(id);
        }else{
             destinationEntity = destinationMapper.getDestinationDetailByRegion(region);
        }
        if(destinationEntity!=null){
            map.put("destinationEntity",destinationEntity);
            //素材信息
            map.put("fileList",materialService.handleMaterial(destinationEntity.getId()));
            return ResponseMessage.defaultResponse().setData(map);
        }else{
            return ResponseMessage.validFailResponse().setMsg("该目的地信息不存在！");
        }
    }

    @Override
    public ResponseMessage getDestinationList(Integer page, Integer size, User user, Map<String, Object> filter){
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        List<Map<String, Object>> list = new ArrayList<>();
        Page<DestinationEntity> DestinationEntities = destinationMapper.findByPage(filter);
        for(DestinationEntity destinationEntity:DestinationEntities){
            Map<String,Object>map= Maps.newHashMap();
            map.put("destinationEntity",destinationEntity);
            //素材信息
            map.put("fileList",materialService.handleMaterial(destinationEntity.getId()));
            list.add(map);
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

}
