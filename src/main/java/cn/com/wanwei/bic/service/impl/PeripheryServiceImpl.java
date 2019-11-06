package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.PeripheryEntity;
import cn.com.wanwei.bic.entity.PeripheryTagsEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.PeripheryMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.PeripheryModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.PeripheryService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        Sort sort = Sort.by(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "weight"), new Sort.Order(Sort.Direction.DESC, "created_date")});
        MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        PageHelper.orderBy(pageRequest.getOrders());
        Page<PeripheryEntity> peripheryEntities = peripheryMapper.findByPage(filter);
        PageInfo<PeripheryEntity> pageInfo = new PageInfo<>(peripheryEntities,pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        PeripheryEntity entity = peripheryMapper.selectByPrimaryKey(id);
        if (entity != null){
            responseMessage.setData(entity);
        }else {
            responseMessage.setStatus(0).setMsg("暂无该周边信息");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage save(PeripheryModel peripheryModel, User user, Long ruleId, Integer appCode) {
        PeripheryEntity peripheryEntity = peripheryModel.getPeripheryEntity();
        String type = peripheryModel.getType();
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode = coderServiceFeign.buildSerialByCode(ruleId,appCode,type);
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            peripheryEntity.setId(UUIDUtils.getInstance().getId());
            peripheryEntity.setCode(responseMessageGetCode.getData().toString());
            peripheryEntity.setStatus(0);
            peripheryEntity.setDeptCode(user.getOrg().getCode());
            peripheryEntity.setCreatedUser(user.getUsername());
            peripheryEntity.setCreatedDate(new Date());
            peripheryMapper.insert(peripheryEntity);
            this.saveTags(peripheryModel.getList(),peripheryEntity.getId(),user);
            return ResponseMessage.defaultResponse().setMsg("保存成功!");
        }
        return responseMessageGetCode;
    }

    private void saveTags(List<Map<String, Object>> tagsList, String peripheryId, User user){
        List<BaseTagsEntity> list = Lists.newArrayList();
        for(int i=0; i<tagsList.size(); i++){
            BaseTagsEntity baseTagsEntity = new BaseTagsEntity();
            baseTagsEntity.setTagName(tagsList.get(i).get("tagName").toString());
            baseTagsEntity.setTagCatagory(tagsList.get(i).get("tagCatagory").toString());
            list.add(baseTagsEntity);
        }
        tagsService.batchInsert(peripheryId,list,user, PeripheryTagsEntity.class);
    }

    @Override
    public ResponseMessage edit(String id, PeripheryModel peripheryModel, User user) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        PeripheryEntity peripheryEntity = peripheryModel.getPeripheryEntity();
        PeripheryEntity entity = peripheryMapper.selectByPrimaryKey(id);
        if(entity != null){
            peripheryEntity.setUpdatedUser(user.getUsername());
            peripheryEntity.setUpdatedDate(new Date());
            peripheryEntity.setId(entity.getId());
            peripheryEntity.setStatus(0);
            peripheryEntity.setCode(entity.getCode());
            peripheryMapper.updateByPrimaryKey(peripheryEntity);
            this.saveTags(peripheryModel.getList(),id,user);
            responseMessage.setMsg("更新成功");
        }else {
            responseMessage.setStatus(0).setMsg("暂无该周边信息");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage delete(String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        PeripheryEntity entity = peripheryMapper.selectByPrimaryKey(id);
        if(entity.getStatus() == 9){
            responseMessage.setStatus(0).setMsg("已上线，禁止删除");
        }else {
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
        for (String id : ids){
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
        Integer maxNum= peripheryMapper.maxWeight();
        List<String>ids =weightModel.getIds();
        if(ids!=null&&!ids.isEmpty()){
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if(weightModel.isFlag()||(maxNum+ids.size())>Integer.MAX_VALUE){
                peripheryMapper.clearWeight();
                maxNum=0;
            }
            for(int i=0;i<ids.size();i++){
                PeripheryEntity peripheryEntity = peripheryMapper.selectByPrimaryKey(ids.get(i));
                if(weightModel.isFlag()){
                    peripheryEntity.setWeight(ids.size()-i);
                }else{
                    peripheryEntity.setWeight(maxNum+ids.size()-i);
                }
                peripheryEntity.setDeptCode(user.getOrg().getCode());
                peripheryEntity.setUpdatedUser(user.getUsername());
                peripheryEntity.setUpdatedDate(new Date());
                peripheryMapper.updateByPrimaryKey(peripheryEntity);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }
}
