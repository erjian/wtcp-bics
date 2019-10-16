package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.DestinationEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/10 9:26:26
 * @desc wtcp-bics - DestinationService 目的地基础信息业务层管理接口
 */
public interface DestinationService {

    /**
     * 获取目的地分页列表
     * @param page  页数
     * @param size  条数
     * @param user  用户信息
     * @param filter   查询参数
     * @return
     * @throws Exception
     */
    ResponseMessage findByPage(Integer page, Integer size, User user, Map<String, Object> filter) throws Exception;

    /**
     * 目的地基础信息新增
     * @param destinationEntity  目的地基础信息实体
     * @param username  用户名
     * @return
     */
    ResponseMessage save(DestinationEntity destinationEntity, String username) throws Exception;

    /**
     * 目的地基础信息编辑
     * @param id  主键id
     * @param destinationEntity  目的地基础信息实体
     * @param username  用户名
     * @return
     */
    ResponseMessage edit(String id, DestinationEntity destinationEntity, String username) throws Exception;

    /**
     * 根据目的地id查询目的地信息
     * @param id
     * @return
     */
    ResponseMessage selectByPrimaryKey(String id) throws Exception;

    /**
     * 删除目的地信息
     * @param id
     * @return
     * @throws Exception
     */
    ResponseMessage deleteByPrimaryKey(String id) throws Exception;

    /**
     * 目的地权重修改
     * @param id  主键ID
     * @param weightNum   权重
     * @param username
     * @return
     */
    ResponseMessage changeWeight(String id, Float weightNum, String username) throws Exception;

    /**
     * 目的地信息记录审核
     * @param id
     * @param status
     * @param username
     * @param type
     * @return
     * @throws Exception
     */
    ResponseMessage changeStatus(String id, Integer status, String username, int type) throws Exception;

    /**
     * 校验目的地名称唯一性
     * @param id
     * @param regionFullName
     * @return
     */
    ResponseMessage checkRegionFullName(String id, String regionFullName);
}
