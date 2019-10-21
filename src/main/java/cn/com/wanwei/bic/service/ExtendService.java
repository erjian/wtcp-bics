package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.ExtendEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/18 10:32:32
 * @desc 扩展信息管理业务层接口
 */
public interface ExtendService {

    /**
     * 扩展信息管理分页列表
     * @param page
     * @param size
     * @param currentUser
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, User currentUser, Map<String, Object> filter) throws Exception;

    /**
     * 扩展信息新增
     * @param extendEntity
     * @param username
     * @return
     */
    ResponseMessage save(ExtendEntity extendEntity, String username) throws Exception;

    /**
     * 扩展信息编辑
     * @param id
     * @param extendEntity
     * @param username
     * @return
     */
    ResponseMessage edit(String id, ExtendEntity extendEntity, String username) throws Exception;

    /**
     * 根据扩展信息id查询扩展信息详情
     * @param id
     * @return
     */
    ResponseMessage selectByPrimaryKey(String id) throws Exception;

    /**
     * 扩展信息审核/上线
     * @param id
     * @param username
     * @param i
     * @return
     */
    ResponseMessage auditOrIssue(String id, String username, int type) throws Exception;

    /**
     * 根据id删除扩展信息
     * @param id
     * @return
     */
    ResponseMessage deleteByPrimaryKey(String id) throws Exception;

    /**
     * 扩展信息权重修改
     * @param id
     * @param weight
     * @param username
     * @return
     */
    ResponseMessage changeWeight(String id, Float weight, String username) throws Exception;
}
