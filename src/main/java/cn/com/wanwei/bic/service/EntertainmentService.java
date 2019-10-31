package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.EntertainmentEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

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
     * @param entertainmentEntity
     * @param user
     * @return
     */
    ResponseMessage create(EntertainmentEntity entertainmentEntity, User user);

    /**
     * 编辑休闲娱乐信息
     * @param id
     * @param entertainmentEntity
     * @param user
     * @return
     */
    ResponseMessage update(String id, EntertainmentEntity entertainmentEntity, User user);

//    /**
//     * 休闲娱乐信息排序
//     * @param id
//     * @param weight
//     * @param user
//     * @return
//     */
//    ResponseMessage goWeight(String id, Float weight, User user);

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
    int dataBind(String updatedUser, String updatedDate, DataBindModel model);
}
