package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * wtcp-bics - AuditLogServiceImpl 审核记录管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private  AuditLogMapper auditLogMapper;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        try {
            Sort sort = Sort.by(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "created_date")});
            MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
            PageHelper.startPage(pageRequest.getPage(),pageRequest.getSize(),pageRequest.getOrders());
            Page<AuditLogEntity> auditLogEntityies = auditLogMapper.findByPage(filter);
            PageInfo<AuditLogEntity> pageInfo = new PageInfo<>(auditLogEntityies, pageRequest);
            return ResponseMessage.defaultResponse().setData(pageInfo);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("获取失败！");
        }
    }

    @Override
    public ResponseMessage create(AuditLogEntity auditLogEntity,String userName) {
        try {
            auditLogEntity.setId(UUIDUtils.getInstance().getId());
            auditLogEntity.setCreatedUser(userName);
            auditLogEntity.setCreatedDate(new Date());
            auditLogMapper.insert(auditLogEntity);
            return ResponseMessage.defaultResponse().setMsg("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("保存失败！");
        }

    }

}
