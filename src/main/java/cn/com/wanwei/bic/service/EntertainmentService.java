package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.EntertainmentEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - EntertainmentService 休闲娱乐管理接口
 */
public interface EntertainmentService {

    /**
     * 休闲娱乐管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询休闲娱乐信息
     * @param id
     * @return
     */
    ResponseMessage find(String id);

    /**
     * 新增休闲娱乐信息
     * @param entertainmentModel
     * @param user
     * @param ruleId
     * @param appCode
     * @return
     */
    ResponseMessage create(EntityTagsModel<EntertainmentEntity> entertainmentModel, User user, Long ruleId, Integer appCode);

    /**
     * 编辑休闲娱乐信息
     * @param id
     * @param entertainmentModel
     * @param user
     * @return
     */
    ResponseMessage update(String id, EntityTagsModel<EntertainmentEntity> entertainmentModel, User user);

    /**
     * 休闲娱乐信息排序
     * @param weightModel
     * @param user
     * @return
     */
    ResponseMessage goWeight(WeightModel weightModel, User user);

    /**
     * 标题重名校验
     * @param id
     * @param title
     * @return
     */
    ResponseMessage checkTitle(String id, String title);

    /**
     * 休闲娱乐信息审核或上下线
     * @param auditLogEntity
     * @param user
     * @param i
     * @return
     */
    ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int i);

    /**
     * 删除休闲娱乐信息
     * @param id
     * @return
     */
    ResponseMessage delete(String id);

    /**
     * 数据绑定
     * @param updatedUser
     * @param updatedDate
     * @param model
     */
    void dataBind(String updatedUser, String updatedDate, DataBindModel model);

    /**
     * 休闲娱乐信息标签关联
     * @param id
     * @param list
     * @param user
     * @return
     */
    ResponseMessage relateTags(String id, List<BaseTagsEntity> list, User user);

    /**
     * 获取休闲娱乐信息
     * @return
     */
    ResponseMessage getEnterList();

    /**
     * 查询休闲娱乐相关信息（基础信息，企业信息，通讯信息，素材信息）
     * @param id
     * @return
     */
    ResponseMessage getEnterInfo(String id);

    /**
     * 农家乐列表
     * @param type code
     * @param searchValue 条件
     * @return
     */
    ResponseMessage findBySearchValue(String type, String searchValue);
}
