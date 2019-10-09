package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ScenicEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScenicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ScenicEntity record);

    ScenicEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeyWithBLOBs(ScenicEntity record);

    int updateByPrimaryKey(ScenicEntity record);
}