package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.HotelEntity;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.mybatis.service.BaseService;

import java.util.Map;

/**
 * wtcp-bics - HotelService 酒店基础信息管理接口
 */
public interface HotelService extends BaseService<HotelEntity, String> {


    /**
     * 新增酒店
     * @param model
     * @param currentUser
     * @param ruleId
     * @param appCode
     * @return
     */
    ResponseMessage insert(EntityTagsModel<HotelEntity> model, User currentUser, Long ruleId, Integer appCode);

    /**
     * 更新数据
     * @param id
     * @param hotelModel
     * @param currentUser
     * @return
     */
    ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HotelEntity> hotelModel, User currentUser);

    /**
     * 上下线修改
     * @param id
     * @param status
     * @param username
     * @return
     */
    ResponseMessage updateOnlineStatus(String id, Integer status, String username);

    /**
     * 审核酒店信息
     * @param id
     * @param auditStatus
     * @param msg
     * @param currentUser
     * @return
     */
    ResponseMessage updateAuditStatus(String id, int auditStatus, String msg, User currentUser);

    /**
     * 批量插入酒店标签
     * @param tags
     * @param currentUser
     * @return
     */
    ResponseMessage insertTags(Map<String, Object> tags, User currentUser);

    /**
     * 查询酒店详细信息
     * @param id
     * @return
     */
    ResponseMessage findHotelInfoById(String id);

    ResponseMessage findBySearchValue(String name, String ids);

    /**
     * 查询酒店下拉列表数据
     * @param areaCode
     * @return
     */
    ResponseMessage findByAreaCode(String areaCode);

    /**
     * 根据id查询酒店详情数据
     * @param id
     * @return
     */
    ResponseMessage findInfoById(String id);
}
