package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.TrafficAgentEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.TrafficAgentMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.service.TrafficAgentService;
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
public class TrafficAgentServiceImpl implements TrafficAgentService {

    @Autowired
    private TrafficAgentMapper trafficAgentMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), pageRequest.getOrders());
        Page<TrafficAgentEntity> trafficAgentEntities = trafficAgentMapper.findByPage(filter);
        PageInfo<TrafficAgentEntity> pageInfo = new PageInfo<>(trafficAgentEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage find(String id) {
        TrafficAgentEntity trafficAgentEntity = trafficAgentMapper.selectByPrimaryKey(id);
        if (trafficAgentEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
        }
        return ResponseMessage.defaultResponse().setData(trafficAgentEntity);
    }

    @Override
    public ResponseMessage create(TrafficAgentEntity trafficAgentEntity, User user, Long ruleId, Integer appCode) {
        //获取统一认证生成的code
        ResponseMessage responseMessageGetCode = coderServiceFeign.buildSerialByCode(ruleId, appCode, trafficAgentEntity.getJpin());
        if (responseMessageGetCode.getStatus() == 1 && responseMessageGetCode.getData() != null) {
            String id = UUIDUtils.getInstance().getId();
            trafficAgentEntity.setId(id);
            trafficAgentEntity.setFullSpell(PinyinUtil.getPingYin(trafficAgentEntity.getTitle()));
            trafficAgentEntity.setSimpleSpell(PinyinUtil.getPinYinHeadChar(trafficAgentEntity.getTitle()));
            trafficAgentEntity.setCode(responseMessageGetCode.getData().toString());
            trafficAgentEntity.setStatus(1);
            trafficAgentEntity.setWeight(0);
            trafficAgentEntity.setCreatedUser(user.getUsername());
            trafficAgentEntity.setCreatedDate(new Date());
            trafficAgentEntity.setDeptCode(user.getOrg().getCode());
            trafficAgentMapper.insert(trafficAgentEntity);
            return ResponseMessage.defaultResponse().setMsg("保存成功!").setData(id);
        }
        return responseMessageGetCode;
    }

    @Override
    public ResponseMessage update(String id, TrafficAgentEntity trafficAgentEntity, User user) {
        TrafficAgentEntity tEntity = trafficAgentMapper.selectByPrimaryKey(id);
        if (tEntity != null) {
            trafficAgentEntity.setId(tEntity.getId());
            trafficAgentEntity.setFullSpell(PinyinUtil.getPingYin(trafficAgentEntity.getTitle()));
            trafficAgentEntity.setSimpleSpell(PinyinUtil.getPinYinHeadChar(trafficAgentEntity.getTitle()));
            trafficAgentEntity.setCreatedUser(tEntity.getCreatedUser());
            trafficAgentEntity.setCreatedDate(tEntity.getCreatedDate());
            trafficAgentEntity.setStatus(1);
            trafficAgentEntity.setCode(tEntity.getCode());
            trafficAgentEntity.setDeptCode(tEntity.getDeptCode());
            trafficAgentEntity.setUpdatedUser(user.getUsername());
            trafficAgentEntity.setUpdatedDate(new Date());
            trafficAgentMapper.updateByPrimaryKey(trafficAgentEntity);
            return ResponseMessage.defaultResponse().setMsg("更新成功！");
        }
        return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
    }

    @Override
    public ResponseMessage delete(String id) {
        trafficAgentMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功！");
    }

    @Override
    public ResponseMessage checkTitle(String id, String title) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        if (StringUtils.isNotBlank(title)) {
            TrafficAgentEntity tEntity = trafficAgentMapper.checkTitle(title);
            if (tEntity != null) {
                if (!tEntity.getId().equals(id)) {
                    return responseMessage.setStatus(ResponseMessage.FAILED).setMsg("标题名称重复！");
                }
            }
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) {
        TrafficAgentEntity tEntity= trafficAgentMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if (tEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("暂无该休闲娱乐信息！");
        }
        String msg = "审核成功！";
        if (type == 1) {
            //上线
            if (auditLogEntity.getStatus() == 1 || auditLogEntity.getStatus() == 9) {
                msg = auditLogEntity.getStatus() == 1 ? "下线成功！" : "上线成功！";
            } else {
                return ResponseMessage.validFailResponse().setMsg("上下线状态错误！");
            }
        }
        tEntity.setStatus(auditLogEntity.getStatus());
        tEntity.setUpdatedUser(user.getUsername());
        tEntity.setUpdatedDate(new Date());
        trafficAgentMapper.updateByPrimaryKey(tEntity);
        auditLogEntity.setType(type);
        auditLogService.create(auditLogEntity, user.getUsername());
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public void dataBind(String updatedUser, String updatedDate, DataBindModel model) {
        String deptCode = model.getDeptCode();
        List<String> ids = model.getIds();
        trafficAgentMapper.dataBind(updatedUser, updatedDate, deptCode, ids);
    }

    @Override
    public ResponseMessage getTrafficAgentList() {
        return ResponseMessage.defaultResponse().setData(trafficAgentMapper.getTrafficAgentList());
    }
}
