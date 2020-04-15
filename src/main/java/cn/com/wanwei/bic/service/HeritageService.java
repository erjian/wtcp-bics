package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.HeritageEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.mybatis.service.BaseService;

import java.util.Map;

/**
 * wtcp-bics - HeritageService 非遗基础信息管理接口
 */
public interface HeritageService extends BaseService<HeritageEntity, String> {


    /**
     * 新增非遗
     * @param model
     * @param currentUser
     * @param ruleId
     * @param appCode
     * @return
     */
    ResponseMessage insert(EntityTagsModel<HeritageEntity> model, User currentUser, Long ruleId, Integer appCode);

    /**
     * 更新数据
     * @param id
     * @param heritageModel
     * @param currentUser
     * @return
     */
    ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HeritageEntity> heritageModel, User currentUser);

    /**
     * 上下线修改
     * @param id
     * @param status
     * @param username
     * @return
     */
    ResponseMessage updateOnlineStatus(String id, Integer status, String username);

    /**
     * 审核非遗信息
     * @param id
     * @param auditStatus
     * @param msg
     * @param currentUser
     * @return
     */
    ResponseMessage updateAuditStatus(String id, int auditStatus, String msg, User currentUser);

    /**
     * 查询非遗详细信息
     * @param id
     * @return
     */
    ResponseMessage findHeritageInfoById(String id);

    ResponseMessage findBySearchValue(String name, String ids);
}
