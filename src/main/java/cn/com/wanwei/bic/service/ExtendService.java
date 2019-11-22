package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.ExtendEntity;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/18 10:32:32
 * @desc 扩展信息管理业务层接口
 */
public interface ExtendService {

    /**
     * 扩展信息管理分页列表
     * @param page
     * @param size
     * @param currentUser
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, User currentUser, Map<String, Object> filter) throws Exception;

    /**
     * 扩展信息新增
     * @param extendModel
     * @param user
     * @param ruleId
     * @param appCode
     * @return
     * @throws Exception
     */
    ResponseMessage save(EntityTagsModel<ExtendEntity> extendModel, User user, Long ruleId, Integer appCode) throws Exception;

    /**
     * 扩展信息编辑
     * @param id
     * @param extendModel
     * @param user
     * @return
     * @throws Exception
     */
    ResponseMessage edit(String id, EntityTagsModel<ExtendEntity> extendModel, User user) throws Exception;

    /**
     * 根据扩展信息id查询扩展信息详情
     * @param id
     * @return
     */
    ResponseMessage selectByPrimaryKey(String id) throws Exception;

    /**
     * 扩展信息审核/上线
     * @param auditLogEntity
     * @param user
     * @param type
     * @return
     */
    ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type) throws Exception;

    /**
     * 根据id删除扩展信息
     * @param id
     * @return
     */
    ResponseMessage deleteByPrimaryKey(String id) throws Exception;

    /**
     * 扩展信息权重修改
     * @param id
     * @param weight
     * @param username
     * @return
     */
    ResponseMessage changeWeight(String id, Float weight, String username) throws Exception;

    /**
     * 扩展信息关联标签
     * @param tags
     * @param currentUser
     * @return
     */
    ResponseMessage relateTags(Map<String, Object> tags, User currentUser) throws Exception;

    /**
     * 标题重名校验
     * @param id
     * @param title
     * @return
     */
    ResponseMessage checkTitle(String id, String title);

    /**
     * 获取扩展信息列表
     *
     * @param principalId
     * @param type
     * @return
     */
    ResponseMessage getList(String principalId, Integer type);

    /**
     * 查询扩展信息详情
     * @param id
     * @return
     */
    ResponseMessage getExtendInfo(String id);
}
