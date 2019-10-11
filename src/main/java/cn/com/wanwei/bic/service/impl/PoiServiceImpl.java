package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.PoiMapper;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.PoiService;
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
 * wtcp-bics - PoiServiceImpl poi管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class PoiServiceImpl implements PoiService {

    @Autowired
    private PoiMapper poiMapper;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Value("${wtcp.bic.appCode}")
    protected Integer appId;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        try {
            Sort sort = Sort.by(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "weight"), new Sort.Order(Sort.Direction.DESC, "created_date")});
            MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
            PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
            Page<PoiEntity> poiEntities = poiMapper.findByPage(filter);
            PageInfo<PoiEntity> pageInfo = new PageInfo<>(poiEntities, pageRequest);
            return ResponseMessage.defaultResponse().setData(pageInfo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("获取失败！");
        }
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
    public ResponseMessage create(PoiEntity poiEntity, User user) {
        try {
            //获取统一认证生成的code
            ResponseMessage responseMessageGetCode = coderServiceFeign.buildSerialNum(appId);
            if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
                poiEntity.setId(UUIDUtils.getInstance().getId());
                poiEntity.setCode(responseMessageGetCode.getData().toString());
                poiEntity.setStatus(0);
                poiEntity.setCreatedUser(user.getUsername());
                poiEntity.setCreatedDate(new Date());
                poiEntity.setDeptCode(user.getOrg().getCode());
                poiMapper.insert(poiEntity);
                return ResponseMessage.defaultResponse().setMsg("保存成功!");
            }
            return responseMessageGetCode;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("保存失败！");
        }
    }

    @Override
    public ResponseMessage update(String id, PoiEntity poiEntity, User user) {
        try {
            PoiEntity pEntity = poiMapper.selectByPrimaryKey(id);
            if (pEntity != null) {
                poiEntity.setId(pEntity.getId());
                poiEntity.setCreatedUser(pEntity.getCreatedUser());
                poiEntity.setCreatedDate(pEntity.getCreatedDate());
                poiEntity.setStatus(0);
                poiEntity.setCode(pEntity.getCode());
                poiEntity.setDeptCode(user.getOrg().getCode());
                poiEntity.setUpdatedUser(user.getUsername());
                poiEntity.setUpdatedDate(new Date());
                poiMapper.updateByPrimaryKey(poiEntity);
                return ResponseMessage.defaultResponse().setMsg("更新成功！");
            }
            return ResponseMessage.validFailResponse().setMsg("暂无该poi信息！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("更新失败！");
        }
    }

    @Override
    public ResponseMessage delete(String id) {
        try {
            poiMapper.deleteByPrimaryKey(id);
            return ResponseMessage.defaultResponse().setMsg("删除成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("删除失败！");
        }
    }

    @Override
    public ResponseMessage goWeight(String id, Float weight, User user) {
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
            pntity.setStatus(auditLogEntity.getStatus());
            pntity.setDeptCode(user.getOrg().getCode());
            pntity.setUpdatedUser(user.getUsername());
            pntity.setUpdatedDate(new Date());
            poiMapper.updateByPrimaryKey(pntity);
            auditLogEntity.setType(type);
            auditLogService.create(auditLogEntity,user.getUsername());
            return ResponseMessage.defaultResponse().setMsg(msg);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("操作失败！");
        }
    }
}
