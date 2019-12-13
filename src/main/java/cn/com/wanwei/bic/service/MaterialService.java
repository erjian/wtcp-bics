package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.List;
import java.util.Map;

public interface MaterialService {

    ResponseMessage deleteByPrimaryKey(String id);

    ResponseMessage deleteOneByPidAndId(String principalId, String id);

    ResponseMessage deleteByPrincipalIds(List<String> ids);

    ResponseMessage deleteByPrincipalId(String principalId);

    ResponseMessage insert(MaterialEntity materialEntity, User user);

    ResponseMessage saveByDom(String content, String principalId, User user);

    ResponseMessage batchInsert(String principalId, List<MaterialEntity> materialList, User user);

    ResponseMessage selectByPrimaryKey(String id);

    ResponseMessage findByPrincipalId(String principalId);

    ResponseMessage findByPidAndType(String principalId, String type);

    ResponseMessage findByPidAndIdentify(String principalId, Integer fileIdentify);

    ResponseMessage updateByPrimaryKey(String id, MaterialEntity materialEntity, User user);

    ResponseMessage updateIdentify(String principalId, String id, Integer identify, User user);

    Map<String, Object> handleMaterial(String principalId);

    /**
     * 根据ids获取素材
     * @param ids ids
     * @param parameter 参数
     * @return
     */
    ResponseMessage findByIds(String ids,Integer parameter);
}
