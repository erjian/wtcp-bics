package cn.com.wanwei.bic.service;

import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

public interface ResourceService {
    /**
     * 获取饼状图数据
     * @param user 用户
     * @param queryModel 查询参数
     * @return
     */
    ResponseMessage findByPieChart(User user, Map<String, Object> queryModel);

    /**
     * 根据资源code和当前登录用户获取饼状图数据
     * @author linjw 2019年12月20日09:05:47
     * @param currentUser
     * @param code
     * @return
     */
    ResponseMessage initPieByCode(User currentUser, String code, Integer size);

    /**
     * 获取柱状图数据
     * @param user 用户
     * @param queryModel 查询参数
     * @return
     */
    ResponseMessage findByBarChart(User user, Map<String, Object> queryModel);

}
