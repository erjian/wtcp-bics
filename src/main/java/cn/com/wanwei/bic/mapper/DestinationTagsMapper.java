package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.DestinationTagsEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DestinationTagsMapper {
    int deleteByPrimaryKey(String id);

    int insert(DestinationTagsEntity record);

    DestinationTagsEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(DestinationTagsEntity record);
}