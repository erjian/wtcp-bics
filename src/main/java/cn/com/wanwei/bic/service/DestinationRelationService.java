package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.DestinationRelationEntity;
import cn.com.wanwei.common.model.ResponseMessage;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/11 11:29:29
 * @desc wtcp-bics - DestinationRelationService 目的地关联信息业务层管理接口.
 */
public interface DestinationRelationService {

    /**
     * 根据目的地id获取关联信息
     * @param id
     * @param destinationRelationEntityClass
     * @return
     */
    ResponseMessage findPrincipalByDestinationId(String id, Class<DestinationRelationEntity> destinationRelationEntityClass) throws Exception;

    /**
     * 目的地关联信息新增
     * @param destinationRelationEntity
     * @param username
     * @return
     */
    ResponseMessage save(DestinationRelationEntity destinationRelationEntity, String username) throws Exception;
}
