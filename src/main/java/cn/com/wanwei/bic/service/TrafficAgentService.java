package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.TrafficAgentEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.GouldModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

public interface TrafficAgentService {

    /**
     * 交通枢纽管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询交通枢纽信息
     * @param id
     * @return
     */
    ResponseMessage find(String id);

    /**
     * 新增交通枢纽信息
     * @param trafficAgentEntity
     * @param currentUser
     * @param ruleId
     * @param appCode
     * @return
     */
    ResponseMessage create(TrafficAgentEntity trafficAgentEntity, User currentUser, Long ruleId, Integer appCode);

    /**
     * 编辑交通枢纽信息
     * @param id
     * @param trafficAgentEntity
     * @param currentUser
     * @return
     */
    ResponseMessage update(String id, TrafficAgentEntity trafficAgentEntity, User currentUser);

    /**
     * 删除交通枢纽信息
     * @param id
     * @return
     */
    ResponseMessage delete(String id);

    /**
     * 标题重名校验
     * @param id
     * @param title
     * @return
     */
    ResponseMessage checkTitle(String id, String title);

    /**
     * 交通枢纽信息上下线
     * @param auditLogEntity
     * @param currentUser
     * @param i
     * @return
     */
    ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User currentUser, int i);

    /**
     * 数据绑定
     * @param updatedUser
     * @param updatedDate
     * @param model
     */
    void dataBind(String updatedUser, String updatedDate, DataBindModel model);

    /**
     * 获取交通枢纽信息
     * @return
     */
    ResponseMessage getTrafficAgentList();

    /**
     * 交通枢纽列表
     * @param name 搜索条件（标题  or 全拼  or 简拼）
     * @return
     */
    ResponseMessage findBySearchValue(String name, String ids);

    /**
     * 保存高德交通枢纽搜索数据
     * @param gouldModel
     * @return
     */
    ResponseMessage saveGouldTrafficData(GouldModel gouldModel, User user, Long ruleId, Integer appCode);
}
