package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MaterialService {

    ResponseMessage deleteByPrimaryKey(String id);

    ResponseMessage deleteByPrincipalIds(List<String> ids);

    ResponseMessage insert(MaterialEntity materialEntity, User user);

    ResponseMessage batchInsert(String principalId, List<MaterialEntity> materialList, User user);

    ResponseMessage selectByPrimaryKey(String id);

    ResponseMessage findByPrincipalId(String principalId);

    ResponseMessage findByPidAndType(String principalId, String type);

    ResponseMessage findByPidAndIdentify(String principalId, Integer fileIdentify);

    ResponseMessage updateByPrimaryKey(String id, MaterialEntity materialEntity, User user);

}
