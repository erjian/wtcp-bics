package cn.com.wanwei.bic.service;


import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.model.PoiModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - PoiService poi管理接口
 */
public interface PoiService {

    /**
     * poi管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询poi信息
     * @param id
     * @return
     */
    ResponseMessage find(String id);

    /**
     * 新增poi信息
     * @param poiModel
     * @param user
     * @return
     */
    ResponseMessage create(PoiModel poiModel, User user, Long ruleId, Integer appCode);

    /**
     * 编辑poi信息
     * @param id
     * @param poiModel
     * @param user
     * @return
     */
    ResponseMessage update(String id, PoiModel poiModel, User user);

    /**
     * 删除poi信息
     * @param id
     * @return
     */
    ResponseMessage delete(String id);

    /**
     * 权重更改
     * @param id
     * @param weight
     * @return
     */
    ResponseMessage goWeight(String id, Integer weight, User user);

    /**
     * 标题重名校验
     * @param id
     * @param title
     * @return
     */
    ResponseMessage checkTitle(String id, String title);

    /**
     * poi信息审核操作和上下线
     * @param auditLogEntity
     * @param user
     * @param type (0：审核操作，1：上下线操作)
     * @return
     */
    ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int type);

    /**
     * 查询一级景点
     * @param type
     * @return list
     */
    ResponseMessage findScenicList(String type,String principalId);

    /**
     * 批量删除poi管理信息
     * @param ids
     * @return
     */
    ResponseMessage batchDelete(List<String> ids);

    /**
     * poi关联标签
     * @param tags
     * @param user
     * @return
     */
    ResponseMessage relateTags(Map<String,Object> tags, User user);
}
