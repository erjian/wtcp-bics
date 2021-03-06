package cn.com.wanwei.bic.service;


import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
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

    ResponseMessage findByPageForFeign(Integer page, Integer size, Map<String, Object> filter);

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
    ResponseMessage create(EntityTagsModel<PoiEntity> poiModel, User user, Long ruleId, Integer appCode);

    /**
     * 编辑poi信息
     * @param id
     * @param poiModel
     * @param user
     * @return
     */
    ResponseMessage update(String id, EntityTagsModel<PoiEntity> poiModel, User user);

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
    ResponseMessage findScenicList(String type,String principalId,String id);

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

    /**
     * C端根据id获取POI详情的接口
     * @param id
     * @return
     */
    ResponseMessage getOne(String id);

    /**
     * C端根据景区ID和type获取poi信息
     * @return
     */
    ResponseMessage getList(Map<String, Object> filter);

    /**
     * 关联机构根据POI的id
     * @param updatedUser
     * @param model
     */
    void dataBindById(String updatedUser, DataBindModel model);

    /**
     * 查询是否在景区的POI数据
     * @author linjw 2020年1月10日17:12:53
     * @param insideScenic
     * @return
     */
}
