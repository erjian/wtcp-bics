package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.RentalCarEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 *wtcp-bics/汽车租赁相关接口
 */
public interface RentalCarService{

    /**
     * 汽车租赁分页列表
     * @param page 页数
     * @param size 每页数量
     * @param filter 查询参数
     * @return 分页列表
     */
    ResponseMessage findByPage(Integer page, Integer size, Map<String,Object> filter);

    /**
     * 根据ID汽车租赁详情
     * @param id 租车Id
     * @return 详情
     */
    ResponseMessage detail(String id);

    /**
     * 根据id删除租车信息
     * @param id 租车信息id
     * @return
     */
    ResponseMessage deleteByPrimaryKey(String id);

    /**
     * 新增租车信息
     * @param rentalCarModel 租车model
     * @param user 用户信息
     * @param ruleId  ruleId
     * @param appCode appCode
     * @return
     */
    ResponseMessage save(EntityTagsModel<RentalCarEntity> rentalCarModel, User user, Long ruleId, Integer appCode);

    /**
     * 编辑租车
     * @param id 租车id
     * @param rentalCarModel 租车model
     * @param user 用户信息
     * @return
     */
    ResponseMessage edit(String id, EntityTagsModel<RentalCarEntity> rentalCarModel, User user);

    /**
     * 租车信息关联标签
     * @param tags 标签map
     * @param user 用户信息
     * @return
     */
    ResponseMessage relateTags(Map<String,Object> tags, User user);

    /***
     * 权重更改
     * @param weightModel  权重model
     * @param user 用户信息
     * @return
     */
    ResponseMessage changeWeight(WeightModel weightModel, User user);

    /**
     * 上下线
     * @param id id
     * @param status 状态值
     * @param username 用户
     * @return
     */
    ResponseMessage changeStatus(String id, Integer status, String username);

    /**
     * 租车信息审核
     * @param id 租车id
     * @param auditStatus 审核状态
     * @param msg 审核信息
     * @param user 用户信息
     * @return
     */
    ResponseMessage examineRental(String id, int auditStatus, String msg, User user);

    /**
     * 关联机构
     * @param username 用户名
     * @param model 数据
     * @return
     */
    ResponseMessage dataBind(String username, DataBindModel model);

    /**
     * 校验租车名称
     * @param title  名称
     * @param id id
     * @return
     */
    ResponseMessage findByTitleAndIdNot(String title, String id);

    /**
     * 获取租车信息列表
     * @param title 名称、全拼、简拼
     * @return
     */
    ResponseMessage getRentalCarInfo(String title);

    /**
     * C端  查询租车相关信息（旅行社信息，企业信息，通讯信息，素材信息）
     * @param id 租车id
     * @return
     */
    ResponseMessage getRentalInfo(String id);

    /**
     * 租车列表
     * @param searchValue 条件
     * @return
     */
    ResponseMessage findBySearchValue(String searchValue);
}
