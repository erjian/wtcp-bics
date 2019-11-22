package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.*;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.EntertainmentService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - EntertainmentServiceImpl 休闲娱乐管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class EntertainmentServiceImpl implements EntertainmentService {

    @Autowired
    private EntertainmentMapper  entertainmentMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Value("${wtcp.bic.appCode}")
    protected Integer appId;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private ExtendMapper extendMapper;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private BusinessMapper businessMapper;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size,filter, Sort.Direction.DESC,  "created_date", "updated_date");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        Page<EntertainmentEntity> entertainmentEntities = entertainmentMapper.findByPage(filter);
        PageInfo<EntertainmentEntity> pageInfo = new PageInfo<>(entertainmentEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        EntertainmentEntity entertainmentEntity = entertainmentMapper.selectByPrimaryKey(id);
        if (entertainmentEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
        }
        return ResponseMessage.defaultResponse().setData(entertainmentEntity);
    }

    @Override
    public ResponseMessage create(EntityTagsModel<EntertainmentEntity> entertainmentModel, User user, Long ruleId, Integer appCode) {
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode =coderServiceFeign.buildSerialByCode(ruleId, appCode, entertainmentModel.getType());
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            EntertainmentEntity entertainmentEntity= entertainmentModel.getEntity();
            String id=UUIDUtils.getInstance().getId();
            entertainmentEntity.setId(id);
            entertainmentEntity.setCode(responseMessageGetCode.getData().toString());
            entertainmentEntity.setStatus(0);
            entertainmentEntity.setWeight(0);
            entertainmentEntity.setCreatedUser(user.getUsername());
            entertainmentEntity.setCreatedDate(new Date());
            entertainmentEntity.setDeptCode(user.getOrg().getCode());
            entertainmentMapper.insert(entertainmentEntity);

            //处理标签
            if(CollectionUtils.isNotEmpty(entertainmentModel.getTagsList())){
                tagsService.batchInsert(id,entertainmentModel.getTagsList(),user, EntertainmentTagsEntity.class);
            }
            return ResponseMessage.defaultResponse().setMsg("保存成功!").setData(id);
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage update(String id, EntityTagsModel<EntertainmentEntity> entertainmentModel, User user) {
        EntertainmentEntity eEntity = entertainmentMapper.selectByPrimaryKey(id);
        if (eEntity != null) {
            EntertainmentEntity entertainmentEntity= entertainmentModel.getEntity();
            entertainmentEntity.setId(eEntity.getId());
            entertainmentEntity.setCreatedUser(eEntity.getCreatedUser());
            entertainmentEntity.setCreatedDate(eEntity.getCreatedDate());
            entertainmentEntity.setStatus(0);
            entertainmentEntity.setCode(eEntity.getCode());
            entertainmentEntity.setUpdatedUser(user.getUsername());
            entertainmentEntity.setUpdatedDate(new Date());
            entertainmentMapper.updateByPrimaryKey(entertainmentEntity);

            //处理标签
            if(CollectionUtils.isNotEmpty(entertainmentModel.getTagsList())){
                tagsService.batchInsert(id,entertainmentModel.getTagsList(),user, EntertainmentTagsEntity.class);
            }
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
    }

    @Override
    public ResponseMessage goWeight(WeightModel weightModel,User user) {
        //查出最大权重
        Integer maxNum= entertainmentMapper.maxWeight();
        List<String>ids =weightModel.getIds();
        if(ids!=null&&!ids.isEmpty()){
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if(weightModel.isFlag()||(maxNum+ids.size())>Integer.MAX_VALUE){
                entertainmentMapper.clearWeight();
                maxNum=0;
            }
            for(int i=0;i<ids.size();i++){
                EntertainmentEntity entertainmentEntity = entertainmentMapper.selectByPrimaryKey(ids.get(i));
                entertainmentEntity.setWeight(maxNum+ids.size()-i);
                entertainmentEntity.setUpdatedUser(user.getUsername());
                entertainmentEntity.setUpdatedDate(new Date());
                entertainmentMapper.updateByPrimaryKey(entertainmentEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            EntertainmentEntity entertainmentEntity = entertainmentMapper.checkTitle(title);
            if (entertainmentEntity != null) {
                if (!entertainmentEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) {
        EntertainmentEntity eEntity = entertainmentMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (eEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
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
        eEntity.setStatus(auditLogEntity.getStatus());
        eEntity.setUpdatedUser(user.getUsername());
        eEntity.setUpdatedDate(new Date());
        entertainmentMapper.updateByPrimaryKey(eEntity);
        auditLogEntity.setType(type);
        auditLogService.create(auditLogEntity,user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public ResponseMessage delete(String id) {
        entertainmentMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功！");
    }

    @Override
    public void dataBind(String updatedUser, String updatedDate, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        entertainmentMapper.dataBind(updatedUser, updatedDate, deptCode, ids);
         extendMapper.dataBindExtend(updatedUser, updatedDate, deptCode,ids);
    }

    @Override
    public ResponseMessage relateTags(String id, List<BaseTagsEntity> list, User user) {
        if(CollectionUtils.isNotEmpty(list)){
            tagsService.batchInsert(id,list,user, EntertainmentTagsEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("关联成功！");
    }

    @Override
    public ResponseMessage getEnterList() {
        return ResponseMessage.defaultResponse().setData(entertainmentMapper.getEnterList());
    }

    @Override
    public ResponseMessage getEnterInfo(String id) {
        Map<String,Object>map= Maps.newHashMap();
        EntertainmentEntity entertainmentEntity = entertainmentMapper.selectByPrimaryKey(id);
        if (entertainmentEntity != null) {
            map.put("entertainmentEntity",entertainmentEntity);
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
            map.put("fileList",materialService.handleMaterial(id));
            return ResponseMessage.defaultResponse().setData(map);
        }else{
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
        }
    }
}
