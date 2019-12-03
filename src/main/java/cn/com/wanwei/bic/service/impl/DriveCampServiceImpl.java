package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.BusinessMapper;
import cn.com.wanwei.bic.mapper.ContactMapper;
import cn.com.wanwei.bic.mapper.DriveCampMapper;
import cn.com.wanwei.bic.mapper.EnterpriseMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.DriveCampService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.PinyinUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RefreshScope
public class DriveCampServiceImpl implements DriveCampService {

    @Autowired
    private DriveCampMapper driveCampMapper;

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

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title", "subTitle");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size,filter, Sort.Direction.DESC,  "created_date", "updated_date");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        Page<DriveCampEntity> driveCampEntities = driveCampMapper.findByPage(filter);
        PageInfo<DriveCampEntity> pageInfo = new PageInfo<>(driveCampEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        DriveCampEntity driveCampEntity = driveCampMapper.selectByPrimaryKey(id);
        if (driveCampEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该自驾营地信息！");
        }
        return ResponseMessage.defaultResponse().setData(driveCampEntity);
    }

    @Override
    public ResponseMessage create(EntityTagsModel<DriveCampEntity> driveCampModel, User user, Long ruleId, Integer appCode) {
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode =coderServiceFeign.buildSerialByCode(ruleId, appCode, driveCampModel.getType());
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            DriveCampEntity driveCampEntity  = driveCampModel.getEntity();
            String id= UUIDUtils.getInstance().getId();
            driveCampEntity.setId(id);
            driveCampEntity.setFullSpell(PinyinUtil.getPingYin(driveCampEntity.getTitle()));
            driveCampEntity.setSimpleSpell(PinyinUtil.getPinYinHeadChar(driveCampEntity.getTitle()));
            driveCampEntity.setCode(responseMessageGetCode.getData().toString());
            driveCampEntity.setStatus(0);
            driveCampEntity.setWeight(0);
            driveCampEntity.setCreatedUser(user.getUsername());
            driveCampEntity.setCreatedDate(new Date());
            driveCampEntity.setDeptCode(user.getOrg().getCode());
            driveCampMapper.insert(driveCampEntity);
            //处理标签
            if(CollectionUtils.isNotEmpty(driveCampModel.getTagsList())){
                tagsService.batchInsert(id,driveCampModel.getTagsList(),user, DriveCampEntity.class);
            }
            return ResponseMessage.defaultResponse().setMsg("保存成功!").setData(id);
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage update(String id, EntityTagsModel<DriveCampEntity> driveCampModel, User user) {
        DriveCampEntity dCampEntity = driveCampMapper.selectByPrimaryKey(id);
        if (dCampEntity != null) {
            DriveCampEntity  driveCampEntity = driveCampModel.getEntity();
            driveCampEntity.setId(dCampEntity.getId());
            driveCampEntity.setFullSpell(PinyinUtil.getPingYin(driveCampEntity.getTitle()));
            driveCampEntity.setSimpleSpell(PinyinUtil.getPinYinHeadChar(driveCampEntity.getTitle()));
            driveCampEntity.setCreatedUser(dCampEntity.getCreatedUser());
            driveCampEntity.setCreatedDate(dCampEntity.getCreatedDate());
            driveCampEntity.setStatus(0);
            driveCampEntity.setCode(dCampEntity.getCode());
            driveCampEntity.setUpdatedUser(user.getUsername());
            driveCampEntity.setUpdatedDate(new Date());
            driveCampMapper.updateByPrimaryKey(driveCampEntity);
            //处理标签
            if(CollectionUtils.isNotEmpty(driveCampModel.getTagsList())){
                tagsService.batchInsert(id,driveCampModel.getTagsList(),user, DriveCampEntity.class);
            }
            // 先删除关联的附件再解析富文本中的附件并保存
            materialService.deleteByPrincipalId(id);
            materialService.saveByDom(driveCampEntity.getContent(), id, user);
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该自驾营地信息！");
    }

    @Override
    public ResponseMessage delete(String id) {
        driveCampMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功！");
    }

    @Override
    public ResponseMessage goWeight(WeightModel weightModel, User user) {
        //查出最大权重
        Integer maxNum= driveCampMapper.maxWeight();
        List<String>ids =weightModel.getIds();
        if(ids!=null&&!ids.isEmpty()){
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if(weightModel.isFlag()||(maxNum+ids.size())>Integer.MAX_VALUE){
                driveCampMapper.clearWeight();
                maxNum=0;
            }
            for(int i=0;i<ids.size();i++){
                DriveCampEntity driveCampEntity= driveCampMapper.selectByPrimaryKey(ids.get(i));
                driveCampEntity.setWeight(maxNum+ids.size()-i);
                driveCampEntity.setUpdatedUser(user.getUsername());
                driveCampEntity.setUpdatedDate(new Date());
                driveCampMapper.updateByPrimaryKey(driveCampEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            DriveCampEntity driveCampEntity = driveCampMapper.checkTitle(title);
            if (driveCampEntity != null) {
                if (!driveCampEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) {
        DriveCampEntity dCampEntity = driveCampMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (dCampEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该自驾营地信息！");
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
        dCampEntity.setStatus(auditLogEntity.getStatus());
        dCampEntity.setUpdatedUser(user.getUsername());
        dCampEntity.setUpdatedDate(new Date());
        driveCampMapper.updateByPrimaryKey(dCampEntity);
        auditLogEntity.setType(type);
        auditLogService.create(auditLogEntity,user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public void dataBind(String updatedUser, String updatedDate, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        driveCampMapper.dataBind(updatedUser, updatedDate, deptCode, ids);
    }

    @Override
    public ResponseMessage relateTags(String id, List<BaseTagsEntity> list, User user) {
        if(CollectionUtils.isNotEmpty(list)){
            tagsService.batchInsert(id,list,user, DriveCampEntity.class);
        }
        return ResponseMessage.defaultResponse().setMsg("关联成功！");
    }

    @Override
    public ResponseMessage getDriveCampList() {
        return ResponseMessage.defaultResponse().setData(driveCampMapper.getDriveCampList());
    }

    @Override
    public ResponseMessage getDriveCampInfo(String id) {
        Map<String,Object>map= Maps.newHashMap();
        DriveCampEntity driveCampEntity = driveCampMapper.selectByPrimaryKey(id);
        if (driveCampEntity != null) {
            map.put("driveCampEntity",driveCampEntity);
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
            return ResponseMessage.validFailResponse().setMsg("暂无该自驾营地信息！");
        }
    }
}
