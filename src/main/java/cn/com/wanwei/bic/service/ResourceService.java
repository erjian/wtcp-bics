package cn.com.wanwei.bic.service;

import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

public interface ResourceService {
    /**
     * 获取饼状图数据
     * @param user 用户
     * @return
     */
    ResponseMessage findByPieChart(User user);

    /**
     * 获取柱状图
     * @param user 用户
     * @return
     */
    ResponseMessage findByHistogram(User user);
}
