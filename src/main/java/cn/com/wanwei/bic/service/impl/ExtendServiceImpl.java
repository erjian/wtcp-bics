package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.EntertainmentMapper;
import cn.com.wanwei.bic.mapper.ExtendMapper;
import cn.com.wanwei.bic.mapper.MaterialMapper;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.ExtendService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
 * @date 2019/10/18 10:33:33
 * @desc wtcp-bics - ExtendServiceImpl 扩展信息管理业务层实现类
 */
@Service
@Slf4j
@RefreshScope
public class ExtendServiceImpl implements ExtendService {

    @Autowired
    private ExtendMapper extendMapper;

    @Autowired
    private EntertainmentMapper entertainmentMapper;

    @Autowired
    private AuditLogServiceImpl auditLogService;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialMapper materialMapper;


    /**
     * 扩展信息管理分页列表
     *
     * @param page
     * @param size
     * @param currentUser
     * @param filter
     * @return
     */
    @Override
    public ResponseMessage findByPage(Integer page, Integer size, User currentUser, Map<String, Object> filter) throws Exception {
        EscapeCharUtils.escape(filter, "title");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        //查询扩展信息列表数据
        Page<ExtendEntity> userEntities = extendMapper.findByPage(filter);
        PageInfo<ExtendEntity> pageInfo = new PageInfo<>(userEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    /**
     * 扩展信息新增
     *
     * @param extendModel
     * @param user
     * @param ruleId
     * @param appCode
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage save(EntityTagsModel<ExtendEntity> extendModel, User user, Long ruleId, Integer appCode) throws Exception {
        String id = UUIDUtils.getInstance().getId();
        ExtendEntity extendEntity = extendModel.getEntity();
        ResponseMessage responseMessage = coderServiceFeign.buildSerialByCode(ruleId, appCode, extendEntity.getCode());
        extendEntity.setId(id);
        extendEntity.setCreatedUser(user.getUsername());
        extendEntity.setCreatedDate(new Date());
        extendEntity.setUpdatedDate(new Date());
        extendEntity.setStatus(0);
        extendEntity.setCode(responseMessage.getData().toString());
        extendMapper.insert(extendEntity);

        //保存标签
        if (CollectionUtils.isNotEmpty(extendModel.getTagsList())) {
            tagsService.batchInsert(extendEntity.getId(), extendModel.getTagsList(), user, ExtendTagsEntity.class);
        }

        //处理编辑页面新增素材
        if (CollectionUtils.isNotEmpty(extendModel.getMaterialList())) {
            materialService.batchInsert(id, extendModel.getMaterialList(), user);
        }
        return ResponseMessage.defaultResponse().setMsg("新增成功!");
    }

    /**
     * 扩展信息编辑
     *
     * @param id
     * @param extendModel
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage edit(String id, EntityTagsModel<ExtendEntity> extendModel, User user) throws Exception {
        ExtendEntity entity = extendMapper.selectByPrimaryKey(id);
        ExtendEntity extendEntity = extendModel.getEntity();
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("不存在扩展信息");
        }
        extendEntity.setId(id);
        extendEntity.setCreatedDate(entity.getCreatedDate());
        extendEntity.setCreatedUser(entity.getCreatedUser());
        extendEntity.setStatus(0);  //编辑修改状态为--> 0: 待审
        extendEntity.setUpdatedDate(new Date());
        extendEntity.setUpdatedUser(user.getUsername());
        extendMapper.updateByPrimaryKey(extendEntity);
        //更新标签
        if (CollectionUtils.isNotEmpty(extendModel.getTagsList())) {
            tagsService.batchInsert(extendEntity.getId(), extendModel.getTagsList(), user, ExtendTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("更新成功!");
    }

    /**
     * 根据扩展信息id查询扩展信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage selectByPrimaryKey(String id) throws Exception {
        ExtendEntity extendEntity = extendMapper.selectByPrimaryKey(id);
        if (extendEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该扩展信息！");
        }
        return ResponseMessage.defaultResponse().setData(extendEntity);
    }

    /**
     * 扩展信息审核 、 上线
     *
     * @param auditLogEntity 审核实体类
     * @param user           用户
     * @param type           0-审核   1-上线
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) throws Exception {
        ExtendEntity extendEntity = extendMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (extendEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("无扩展信息!");
        }
        String msg = "审核成功！";
        if (type == 1) {  //上线操作
            if (auditLogEntity.getPreStatus() == 0 || auditLogEntity.getPreStatus() == 2) {
                return ResponseMessage.validFailResponse().setMsg("请先审核通过后，再进⾏上线操作!");
            } else {
                if (auditLogEntity.getStatus() == 1 || auditLogEntity.getStatus() == 9) {
                    msg = auditLogEntity.getStatus() == 1 ? "下线成功！" : "上线成功！";
                } else {
                    return ResponseMessage.validFailResponse().setMsg("上下线状态错误！");
                }
            }
        } else {  //审核操作
            if (auditLogEntity.getStatus() == 9) {
                return ResponseMessage.validFailResponse().setMsg("审核状态错误！");
            }
        }
        extendEntity.setStatus(auditLogEntity.getStatus());
        extendEntity.setUpdatedUser(user.getUsername());
        extendEntity.setUpdatedDate(new Date());
        extendMapper.updateByPrimaryKey(extendEntity);
        auditLogEntity.setType(type);
        auditLogService.create(auditLogEntity, user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    /**
     * 根据扩展信息id删除扩展信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage deleteByPrimaryKey(String id) throws Exception {
        extendMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功!");
    }

    /**
     * 扩展信息权重修改
     *
     * @param id       扩展信息id
     * @param weight   权重
     * @param username
     * @return
     */
    @Override
    public ResponseMessage changeWeight(String id, Float weight, String username) throws Exception {
        ExtendEntity extendEntity = extendMapper.selectByPrimaryKey(id);
        if (extendEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("无扩展信息！");
        }
        extendEntity.setId(id);
        extendEntity.setUpdatedUser(username);
        extendEntity.setUpdatedDate(new Date());
        extendEntity.setWeight(weight);
        extendMapper.updateByPrimaryKey(extendEntity);
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    /**
     * 扩展信息关联标签
     *
     * @param tags
     * @param user
     * @return
     */
    @Override
    public ResponseMessage relateTags(Map<String, Object> tags, User user) throws Exception {
        List<BaseTagsEntity> list = (List<BaseTagsEntity>) tags.get("tagsArr");
        ObjectMapper mapper = new ObjectMapper();
        List<BaseTagsEntity> tagsList = mapper.convertValue(list, new TypeReference<List<BaseTagsEntity>>() {
        });
        if (CollectionUtils.isNotEmpty(tagsList)) {
            tagsService.batchInsert(tags.get("id").toString(), tagsList, user, ExtendTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("标签关联成功");
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            ExtendEntity extendEntity = extendMapper.checkTitle(title);
            if (extendEntity != null) {
                if (!extendEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage getList(String principalId, Integer type) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<ExtendEntity> extendList = extendMapper.getList(principalId, type);
        if (extendList != null && !extendList.isEmpty()) {
            for (ExtendEntity extendEntity : extendList) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("ExtendEntity", extendEntity);
                //素材信息
                map.put("fileList", materialService.handleMaterialNew(extendEntity.getId()));
                map.put("tagList", tagsService.findListByPriId(extendEntity.getId(), ExtendTagsEntity.class));
                list.add(map);
            }
        }
        return ResponseMessage.defaultResponse().setData(list);
    }

    @Override
    public ResponseMessage getExtendInfo(String id) {
        Map<String, Object> map = Maps.newHashMap();
        ExtendEntity extendEntity = extendMapper.selectByPrimaryKey(id);
        if (extendEntity != null) {
            map.put("extendEntity", extendEntity);
            //素材信息
            map.put("fileList", materialService.handleMaterialNew(id));
            return ResponseMessage.defaultResponse().setData(map);
        } else {
            return ResponseMessage.validFailResponse().setMsg("无扩展信息！");
        }
    }
}
