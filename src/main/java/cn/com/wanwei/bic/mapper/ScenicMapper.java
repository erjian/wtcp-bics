package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ScenicEntity;

public interface ScenicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ScenicEntity record);

    ScenicEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeyWithBLOBs(ScenicEntity record);

    int updateByPrimaryKey(ScenicEntity record);
}