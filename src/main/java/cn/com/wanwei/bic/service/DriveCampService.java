package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.DriveCampEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.List;
import java.util.Map;

public interface DriveCampService {

    /**
     * 自驾营地管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询自驾营地信息
     * @param id
     * @return
     */
    ResponseMessage find(String id);

    /**
     * 新增自驾营地信息
     * @param driveCampModel
     * @param currentUser
     * @param ruleId
     * @param appCode
     * @return
     */
    ResponseMessage create(EntityTagsModel<DriveCampEntity> driveCampModel, User currentUser, Long ruleId, Integer appCode);

    /**
     * 编辑自驾营地信息
     * @param id
     * @param driveCampModel
     * @param currentUser
     * @return
     */
    ResponseMessage update(String id, EntityTagsModel<DriveCampEntity> driveCampModel, User currentUser);

    /**
     * 删除自驾营地信息
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
     * 自驾营地信息审核或自驾营地信息上下线
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
     * 自驾营地信息标签关联
     * @param id
     * @param list
     * @param currentUser
     * @return
     */
    ResponseMessage relateTags(String id, List<BaseTagsEntity> list, User currentUser);

    /**
     * 获取自驾营地信息
     * @return
     */
    ResponseMessage getDriveCampList();

    /**
     * 查询自驾营地相关信息（自驾营地信息，企业信息，通讯信息，素材信息）
     * @param id
     * @return
     */
    ResponseMessage getDriveCampInfo(String id);

    /**
     * 自驾营地列表
     * @param searchValue 搜索条件（标题 or 简拼 or 全拼）
     * @return
     */
    ResponseMessage findBySearchValue(String searchValue);
}
