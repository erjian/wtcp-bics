package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.CelebrityEntity;
import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

public interface CelebrityService {
    /**
     * 名人管理分页管理
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String,Object> filter);

    /**
     * 详情
     * @param id
     * @return
     */
    ResponseMessage findById(String id);

    /**
     * 删除名人数据
     * @param id
     * @return
     */
    ResponseMessage deleteById(String id);

    /**
     * 名人新增
     * @param celebrityModel
     * @param currentUser
     * @return
     */
    ResponseMessage insert(EntityTagsModel<CelebrityEntity> celebrityModel, User currentUser);

    /**
     * 名人编辑
     * @param id
     * @param celebrityModel
     * @param user
     * @return
     */
    ResponseMessage updateById(String id, EntityTagsModel<CelebrityEntity> celebrityModel, User user);

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
     * 关联组织结构
     * @param user
     * @param model
     * @return
     */
    ResponseMessage updateDeptCode(String user, DataBindModel model);

    /**
     * 获取名人列表
     * @return
     */
    ResponseMessage findByList();
}
