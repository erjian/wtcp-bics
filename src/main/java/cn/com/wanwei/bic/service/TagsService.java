package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.List;

public interface TagsService<T> {

    BaseTagsEntity selectByPrimaryKey(String id, Class<T> clazz);

    ResponseMessage findByPrincipalId(String principalId, Class<T> clazz);

    List<BaseTagsEntity> findListByPriId(String principalId, Class<T> clazz);

    int deleteByPrincipalId(String principalId, Class<T> clazz);

    int batchInsert(String principalId, List<BaseTagsEntity> entityList, User user, Class<T> clazz);

}
