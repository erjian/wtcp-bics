package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MaterialService {

    ResponseMessage deleteByPrimaryKey(Long id);

    ResponseMessage deleteByPrincipalIds(List<Long> ids);

    ResponseMessage insert(MaterialEntity materialEntity, User user);

    ResponseMessage batchInsert(Long principalId, List<MaterialEntity> materialList, User user);

    ResponseMessage selectByPrimaryKey(Long id);

    ResponseMessage findByPrincipalId(Long principalId);

    ResponseMessage findByPidAndType(Long principalId, String type);

    ResponseMessage findByPidAndIdentify(Long principalId, Integer fileIdentify);

    ResponseMessage updateByPrimaryKey(Long id, MaterialEntity materialEntity, User user);

}
