package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.TravelAgentEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.List;
import java.util.Map;

public interface TravelAgentService {
    /**
     * 旅行社管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询旅行社信息
     * @param id
     * @return
     */
    ResponseMessage find(String id);

    /**
     * 新增旅行社信息
     * @param travelAgentModel
     * @param currentUser
     * @param ruleId
     * @param appCode
     * @return
     */
    ResponseMessage create(EntityTagsModel<TravelAgentEntity> travelAgentModel, User currentUser, Long ruleId, Integer appCode);

    /**
     * 编辑旅行社信息
     * @param id
     * @param travelAgentModel
     * @param currentUser
     * @return
     */
    ResponseMessage update(String id, EntityTagsModel<TravelAgentEntity> travelAgentModel, User currentUser);

    /**
     * 删除旅行社信息
     * @param id
     * @return
     */
    ResponseMessage delete(String id);

    /**
     * 权重更改
     * @param weightModel
     * @param currentUser
     * @return
     */
    ResponseMessage goWeight(WeightModel weightModel, User currentUser);

    /**
     * 标题重名校验
     * @param id
     * @param title
     * @return
     */
    ResponseMessage checkTitle(String id, String title);

    /**
     * 旅行社信息审核和上下线操作
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
     * 旅行社信息标签关联
     * @param id
     * @param list
     * @param user
     * @return
     */
    ResponseMessage relateTags(String id, List<BaseTagsEntity> list, User user);

    /**
     * 获取旅行社信息
     * @return
     */
    ResponseMessage getTravelAgentList();

    /**
     * 查询旅行社相关信息（旅行社信息，企业信息，通讯信息，素材信息）
     * @param id
     * @return
     */
    ResponseMessage getTravelAgentInfo(String id);

    /**
     * 查询旅行社列表
     * @param name
     * @param ids
     * @return
     */
    ResponseMessage findBySearchValue(String name, String ids);
}
