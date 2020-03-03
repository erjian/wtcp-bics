package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.ExhibitsEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.List;
import java.util.Map;

public interface ExhibitsService {

    /**
     * 展品管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询展品信息
     * @param id
     * @return
     */
    ResponseMessage find(String id);

    /**
     * 新增展品信息
     * @param exhibitsModel
     * @param user
     * @param ruleId
     * @param appCode
     * @return
     */
    ResponseMessage create(EntityTagsModel<ExhibitsEntity> exhibitsModel, User user, Long ruleId, Integer appCode);

    /**
     * 编辑展品信息
     * @param id
     * @param exhibitsModel
     * @param user
     * @return
     */
    ResponseMessage update(String id, EntityTagsModel<ExhibitsEntity> exhibitsModel, User user);

    /**
     * 展品信息排序
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
     * 展品信息审核或上下线
     * @param auditLogEntity
     * @param user
     * @param i
     * @return
     */
    ResponseMessage auditOrIssue(AuditLogEntity auditLogEntity, User user, int i);

    /**
     * 删除展品信息
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
     * 展品信息标签关联
     * @param id
     * @param list
     * @param user
     * @return
     */
    ResponseMessage relateTags(String id, List<BaseTagsEntity> list, User user);

    /**
     * 获取展品信息
     * @return
     */
    ResponseMessage getExhibitsList();
}
