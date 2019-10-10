package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.BaseTagsEntity;

import java.util.List;

public interface TagsService<T> {

    BaseTagsEntity selectByPrimaryKey(String id, Class<T> clazz);

    int deleteByPrincipalId(String principalId, Class<T> clazz);

    int batchInsert(List<BaseTagsEntity> entityList);

}
