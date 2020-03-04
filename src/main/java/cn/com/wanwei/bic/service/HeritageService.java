package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.HeritageEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * wtcp-bics - HeritageService 非遗基础信息管理接口
 */
public interface HeritageService {

    /**
     * 分页查询列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询详情
     * @param id
     * @return
     */
    HeritageEntity selectByPrimaryKey(String id);

    /**
     * 根据id删除非遗
     * @param id
     * @return
     */
    ResponseMessage deleteByPrimaryKey(String id);

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
    ResponseMessage changeStatus(String id, Integer status, String username);

    /**
     * 组织机构切换
     * @param updatedUser
     * @param model
     * @return
     */
    ResponseMessage dataBind(String updatedUser, DataBindModel model);

    /**
     * 校验非遗名称是否重复
     * @param title
     * @param s
     * @return
     */
    ResponseMessage existsByTitleAndIdNot(String title, String s);

    /**
     * 审核非遗信息
     * @param id
     * @param auditStatus
     * @param msg
     * @param currentUser
     * @return
     */
    ResponseMessage examineHeritage(String id, int auditStatus, String msg, User currentUser);
}
