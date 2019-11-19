package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.DestinationModel;
import cn.com.wanwei.bic.model.WeightModel;
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
     * @param destinationModel
     * @param user
     * @return
     */
    ResponseMessage save(DestinationModel destinationModel, User user) throws Exception;

    /**
     * 目的地基础信息编辑
     * @param id  主键id
     * @param destinationModel
     * @param user
     * @return
     */
    ResponseMessage edit(String id, DestinationModel destinationModel, User user) throws Exception;

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
     * 目的地信息记录审核
     * @param id
     * @param username
     * @param type
     * @return
     * @throws Exception
     */
    ResponseMessage changeStatus(String id,String username, int type) throws Exception;

    /**
     * 校验目的地名称唯一性
     * @param id
     * @param regionFullCode
     * @return
     */
    ResponseMessage checkRegionFullName(String id, String regionFullCode);

    /**
     * 关联标签
     * @param tags
     * @param user
     * @return
     */
    ResponseMessage relateTags(Map<String, Object> tags, User user);

    /**
     * 数据绑定
     * @param username
     * @param model
     * @return
     */
    ResponseMessage dataBind(String username, DataBindModel model);

    /**
     * 权重修改
     * @param weightModel
     * @param currentUser
     * @return
     */
    ResponseMessage changeWeight(WeightModel weightModel, User currentUser);

    /**
     * 根据areaCode获取目的地详情
     * @param areaCode
     * @return
     */
    ResponseMessage getDestinationDetail(String areaCode);

    /**
     * 目的地基础信息管理分页列表
     */
    ResponseMessage getDestinationList(Integer page, Integer size, User user, Map<String, Object> filter);
}
