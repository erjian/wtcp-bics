package cn.com.wanwei.bic.service;

import cn.com.wanwei.common.model.ResponseMessage;

import java.util.Map;

public interface DataSyncService {

    ResponseMessage findTravelByPage(String category, Integer page, Integer size, Map<String, Object> filter);

}
