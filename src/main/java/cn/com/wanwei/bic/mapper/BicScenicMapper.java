package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.BicScenicEntity;

public interface BicScenicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BicScenicEntity record);

    BicScenicEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeyWithBLOBs(BicScenicEntity record);

    int updateByPrimaryKey(BicScenicEntity record);
}