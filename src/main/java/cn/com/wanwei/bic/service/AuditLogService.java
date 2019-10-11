package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * wtcp-bics - AuditLogService 审核记录管理接口
 */
public interface AuditLogService {
    /**
     * 审核记录管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 审核记录新增
     * @param auditLogEntity
     * @param user
     * @return
     */
    ResponseMessage create(AuditLogEntity auditLogEntity ,User user);
}
