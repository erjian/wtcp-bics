package cn.com.wanwei.bic.service;


import cn.com.wanwei.bic.entity.ScenicSpotEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * wtcp-bics - ScenicSpotService 景点管理接口
 */
public interface ScenicSpotService {

    /**
     * 景点管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询景点信息
     * @param id
     * @return
     */
    ResponseMessage find(Long id);

    /**
     * 新增景点信息
     * @param scenicSpotEntity
     * @param user
     * @return
     */
    ResponseMessage create(ScenicSpotEntity scenicSpotEntity, User user);

    /**
     * 编辑景点信息
     * @param id
     * @param scenicSpotEntity
     * @param currentUser
     * @return
     */
    ResponseMessage update(Long id, ScenicSpotEntity scenicSpotEntity, User currentUser);

    /**
     * 删除景点信息
     * @param id
     * @return
     */
    ResponseMessage delete(Long id);

    /**
     * 权重更改
     * @param id
     * @param weight
     * @return
     */
    ResponseMessage goWeight(Long id, Float weight);
}
