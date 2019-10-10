package cn.com.wanwei.bic.service;


import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * wtcp-bics - PoiService poi管理接口
 */
public interface PoiService {

    /**
     * poi管理分页列表
     * @param page
     * @param size
     * @param filter
     * @return
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    /**
     * 查询poi信息
     * @param id
     * @return
     */
    ResponseMessage find(String id);

    /**
     * 新增poi信息
     * @param poiEntity
     * @param user
     * @return
     */
    ResponseMessage create(PoiEntity poiEntity, User user);

    /**
     * 编辑poi信息
     * @param id
     * @param poiEntity
     * @param user
     * @return
     */
    ResponseMessage update(String id, PoiEntity poiEntity, User user);

    /**
     * 删除poi信息
     * @param id
     * @return
     */
    ResponseMessage delete(String id);

    /**
     * 权重更改
     * @param id
     * @param weight
     * @return
     */
    ResponseMessage goWeight(String id, Float weight, User user);

    /**
     * 标题重名校验
     * @param id
     * @param title
     * @return
     */
    ResponseMessage checkTitle(String id, String title);
}
