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
import cn.com.wanwei.bic.model.ScenicModel;
import cn.com.wanwei.bic.model.WeightModel;
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
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private MaterialMapper materialMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private MaterialService materialService;

    @Override
    public ResponseMessage save(ScenicModel scenicModel, User user, Long ruleId, Integer appCode) {
        ScenicEntity record = scenicModel.getScenicEntity();
        String type = scenicModel.getType();
        record.setId(UUIDUtils.getInstance().getId());
        ResponseMessage result = coderServiceFeign.buildSerialByCode(ruleId, appCode, type);
        record.setCode(result.getData().toString());
        record.setTitleQp(PinyinUtils.getPingYin(record.getTitle()).toLowerCase());
        record.setTitleJp(PinyinUtils.converterToFirstSpell(record.getTitle()).toLowerCase());
        record.setCreatedUser(user.getUsername());
        record.setCreatedDate(new Date());
        record.setStatus(0);
        scenicMapper.insert(record);
        this.saveTags(scenicModel.getList(), record.getId(), user);

        // 解析富文本中的附件并保存
        materialService.saveByDom(record.getContent(), record.getId(), user);

        return ResponseMessage.defaultResponse().setMsg("保存成功");
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
    public ResponseMessage edit(String id, ScenicModel scenicModel, User user) {
        ScenicEntity entity = scenicMapper.selectByPrimaryKey(id);
        ScenicEntity record = scenicModel.getScenicEntity();
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
        this.saveTags(scenicModel.getList(), id, user);

        // 先删除关联的附件再解析富文本中的附件并保存
        materialService.deleteByPrincipalId(id);
        materialService.saveByDom(record.getContent(), id, user);

        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    private void saveTags(List<Map<String, Object>> tagsList, String principalId, User user) {
        List<BaseTagsEntity> list = Lists.newArrayList();
        for (int i = 0; i < tagsList.size(); i++) {
            BaseTagsEntity entity = new BaseTagsEntity();
            entity.setTagCatagory(tagsList.get(i).get("tagCatagory").toString());
            entity.setTagName(tagsList.get(i).get("tagName").toString());
            list.add(entity);
        }
        tagsService.batchInsert(principalId, list, user, ScenicTagsEntity.class);
    }

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, User user1, Map<String, Object> filter) {
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<ScenicEntity> scenicEntities = scenicMapper.findByPage(filter);
        PageInfo<ScenicEntity> pageInfo = new PageInfo<>(scenicEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage dataBind(String updatedUser, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        scenicMapper.dataBind(updatedUser, new Date(), deptCode, ids);
        return ResponseMessage.defaultResponse().setMsg("关联机构成功");
    }

    @Override
    public ResponseMessage changeWeight(WeightModel weightModel, User user) {
        //查出最大权重
        Integer maxNum = scenicMapper.maxWeight();
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {
                scenicMapper.clearWeight();
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(ids.get(i));
                scenicEntity.setWeight(maxNum + ids.size() - i);
                scenicEntity.setUpdatedUser(user.getUsername());
                scenicEntity.setUpdatedDate(new Date());
                scenicMapper.updateByPrimaryKey(scenicEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
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
    public ResponseMessage getScenicInfo(String title) {
        List<ScenicEntity> entities = scenicMapper.getScenicInfo(title);
        return ResponseMessage.defaultResponse().setData(entities);
    }

    @Override
    public ResponseMessage relateTags(Map<String, Object> tags, User user) {
        List<Map<String, Object>> tagsList = (List<Map<String, Object>>) tags.get("tagsArr");
        String relateId = tags.get("id").toString();
        if (null != tagsList && !tagsList.isEmpty()) {
            this.saveTags(tagsList, relateId, user);
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
        data.put("scenicEntity", scenicEntity);

        //2、查询企业信息并添加到返回数据中
        EnterpriseEntity enterpriseEntity = enterpriseMapper.selectByPrincipalId(id);
        data.put("enterpriseEntity", enterpriseEntity);

        //3、查询营业信息并添加到返回数据中
        BusinessEntity businessEntity = businessMapper.selectByPrincipalId(id);
        data.put("businessEntity", businessEntity);

        //4、查询通讯信息并添加到返回数据中
        ContactEntity contactEntity = contactMapper.selectByPrincipalId(id);
        data.put("contactEntity", contactEntity);

        //5、查询素材信息，按照素材类型分类处理并添加到返回数据中
        List<MaterialEntity> list = materialMapper.findByPrincipalId(id);
        Map<String, Object> materialList = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(list)){
            List<MaterialEntity> imageList = Lists.newArrayList();
            List<MaterialEntity> audioList = Lists.newArrayList();
            List<MaterialEntity> videoList = Lists.newArrayList();
            List<MaterialEntity> fileList = Lists.newArrayList();
            List<MaterialEntity> titleImageList = Lists.newArrayList();
            List<MaterialEntity> spotImageList = Lists.newArrayList();
            for(MaterialEntity entity:list){
                if(entity.getFileType().toLowerCase().equals("image")){
                    imageList.add(entity);
                }
                if(entity.getFileType().toLowerCase().equals("audio")){
                    audioList.add(entity);
                }
                if(entity.getFileType().toLowerCase().equals("video")){
                    videoList.add(entity);
                }
                if(entity.getFileType().toLowerCase().equals("file")){
                    fileList.add(entity);
                }
                if(null != entity.getFileIdentify() && entity.getFileIdentify() == 1){
                    titleImageList.add(entity);
                }
                if(null != entity.getFileIdentify() && entity.getFileIdentify() == 2){
                    spotImageList.add(entity);
                }
                if(null != entity.getFileIdentify() && entity.getFileIdentify() == 3){
                    titleImageList.add(entity);
                    spotImageList.add(entity);
                }
            }
            materialList.put("image", imageList);
            materialList.put("audio", audioList);
            materialList.put("video", videoList);
            materialList.put("file", fileList);
            materialList.put("titleImage", titleImageList);
            materialList.put("spotImage", spotImageList);
        }
        data.put("fileList", materialList);
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
