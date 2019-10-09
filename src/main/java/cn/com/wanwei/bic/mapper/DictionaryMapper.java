package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.DictionaryEntity;

public interface DictionaryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictionaryEntity record);

    DictionaryEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(DictionaryEntity record);
}