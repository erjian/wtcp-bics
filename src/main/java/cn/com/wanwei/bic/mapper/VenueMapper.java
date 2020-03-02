package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.VenueEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VenueMapper {
    int deleteByPrimaryKey(String id);

    int insert(VenueEntity record);

    VenueEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(VenueEntity record);
}