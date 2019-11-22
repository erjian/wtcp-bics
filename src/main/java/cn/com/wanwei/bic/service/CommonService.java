package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

public interface CommonService<T> {

    ResponseMessage changeWeight(WeightModel weightModel, User user, Class<T> clazz) throws Exception;

}
