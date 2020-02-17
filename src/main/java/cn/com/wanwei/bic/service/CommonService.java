package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.model.BatchAuditModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface CommonService<T> {

    ResponseMessage changeWeight(WeightModel weightModel, User user, Class<T> clazz) throws Exception;

    ResponseMessage batchChangeStatus(BatchAuditModel batchAuditModel, User user, Class<T> clazz);

    List<Map<String, Object>> getAreaListByPcode(String pcode, int length);

    ResponseMessage getDataByType(String type, String name, String ids);

}
