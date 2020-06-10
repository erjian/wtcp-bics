package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.CateringEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * @author
 */
public interface CateringService {
    /**
     *  餐饮管理分页接口
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String,Object> filter);

    /**
     * 餐饮列表不分页
     * @param filter
     * @return
     */
    ResponseMessage findByList(Map<String,Object> filter);

    /**
     * 获取餐饮信息详情
     * @param id
     * @return
     */
    ResponseMessage findById(String id);

    /**
     * 删除
     * @param id
     * @return
     */
    ResponseMessage deleteById(String id);



    /**
     * 餐饮编辑
     * @param id
     * @param cateringModel
     * @param user
     * @return
     */
    ResponseMessage updateById(String id, EntityTagsModel<CateringEntity> cateringModel, User user);

    /**
     * 关联标签
     * @param tags
     * @param user
     * @return
     */
    ResponseMessage insertTagsBatch(Map<String,Object> tags, User user);

    /**
     * 上下线
     * @param id
     * @param status
     * @param user
     * @return
     */
    ResponseMessage updateStatus(String id, Integer status, String user);

    /**
     * 审核
     * @param id
     * @param auditStatus
     * @param msg
     * @param user
     * @return
     */
    ResponseMessage updateAuditStatus(String id, int auditStatus, String msg, User user);

    /**
     * 关联组织信息
     * @param username
     * @param model
     * @return
     */
    ResponseMessage updateDeptCode(String username, DataBindModel model);

    /**
     * 重命名校验
     * @param title
     * @param id
     * @return
     */
    ResponseMessage findByTitleAndIdNot(String title, String id);

    /**
     * 新增
     * @param cateringModel
     * @param user
     * @param ruleId
     * @param appCode
     * @return
     */
    ResponseMessage insert(EntityTagsModel<CateringEntity> cateringModel, User user, Long ruleId, Integer appCode);

    ResponseMessage findBySearchValue(String name, String ids);
}
