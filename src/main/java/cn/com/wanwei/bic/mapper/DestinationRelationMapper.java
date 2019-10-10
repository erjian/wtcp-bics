package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.DestinationRelationEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DestinationRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(DestinationRelationEntity record);

    DestinationRelationEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(DestinationRelationEntity record);
}