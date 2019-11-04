package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.PeripheryEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.List;
import java.util.Map;

public interface PeripheryService {
    /**
     * 周边管理分页查询
     * @param page 页
     * @param size 每页数量
     * @param filter 其它
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String,Object> filter);

    /**
     * 根据id获取周边管理信息
     * @param id 周边管理信息id
     * @return
     */
    ResponseMessage find(String id);

    /**
     * 新增周边管理信息
     * @param peripheryEntity 实体
     * @param user 用户信息
     * @return
     */
    ResponseMessage save(PeripheryEntity peripheryEntity, User user,Long ruleId,Integer appCode);

    /**
     * 拜
     * @param id 周边信息id
     * @param peripheryEntity 实体
     * @param user 用户信息
     * @return
     */
    ResponseMessage edit(String id, PeripheryEntity peripheryEntity, User user);

    /**
     * 通过id删除周边信息
     * @param id
     * @return
     */
    ResponseMessage delete(String id);

    /**
     * 审核周边信息 (0  审核    1 上下线)
     * @param auditLogEntity
     * @param user
     * @param audit
     * @return
     */
    ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int audit);

    /**
     * 标题重名校验
     * @param id
     * @param title
     * @return
     */
    ResponseMessage checkTitle(String id, String title);

    /**
     * 批量删除周边信息
     * @param ids
     * @return
     */
    ResponseMessage batchDelete(List<String> ids);
}
