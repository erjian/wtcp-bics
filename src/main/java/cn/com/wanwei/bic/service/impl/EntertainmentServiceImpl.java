package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.EntertainmentEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.EntertainmentMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.EntertainmentService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
            Sort sort = Sort.by(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "weight"), new Sort.Order(Sort.Direction.DESC, "created_date")});
            MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
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
    public ResponseMessage create(EntertainmentEntity entertainmentEntity, User user) {
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode = coderServiceFeign.buildSerialNum(appId);
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            entertainmentEntity.setId(UUIDUtils.getInstance().getId());
            entertainmentEntity.setCode(responseMessageGetCode.getData().toString());
            entertainmentEntity.setStatus(0);
            entertainmentEntity.setCreatedUser(user.getUsername());
            entertainmentEntity.setCreatedDate(new Date());
            entertainmentEntity.setDeptCode(user.getOrg().getCode());
            entertainmentMapper.insert(entertainmentEntity);
            return ResponseMessage.defaultResponse().setMsg("保存成功!");
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage update(String id, EntertainmentEntity entertainmentEntity, User user) {
        EntertainmentEntity eEntity = entertainmentMapper.selectByPrimaryKey(id);
        if (eEntity != null) {
            entertainmentEntity.setId(eEntity.getId());
            entertainmentEntity.setCreatedUser(eEntity.getCreatedUser());
            entertainmentEntity.setCreatedDate(eEntity.getCreatedDate());
            entertainmentEntity.setStatus(0);
            entertainmentEntity.setCode(eEntity.getCode());
            entertainmentEntity.setDeptCode(user.getOrg().getCode());
            entertainmentEntity.setUpdatedUser(user.getUsername());
            entertainmentEntity.setUpdatedDate(new Date());
            entertainmentMapper.updateByPrimaryKey(entertainmentEntity);
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
    }

    @Override
    public ResponseMessage goWeight(String id, Float weight, User user) {
        EntertainmentEntity entertainmentEntity = entertainmentMapper.selectByPrimaryKey(id);
        if (entertainmentEntity != null) {
            entertainmentEntity.setWeight(weight);
            entertainmentEntity.setDeptCode(user.getOrg().getCode());
            entertainmentEntity.setUpdatedUser(user.getUsername());
            entertainmentEntity.setUpdatedDate(new Date());
            entertainmentMapper.updateByPrimaryKey(entertainmentEntity);
            return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
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
                return ResponseMessage.validFailResponse().setMsg("当前信息未审核通过，不可上下线操作！");
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
        eEntity.setDeptCode(user.getOrg().getCode());
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
    public int dataBind(String updatedUser, String updatedDate, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        return entertainmentMapper.dataBind(updatedUser, updatedDate, deptCode, ids);
    }
}
