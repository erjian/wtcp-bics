package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HallMapper extends BaseMapper<HallEntity, String> {

    long countByVenueId(@Param("venueId") String venueId);
    
}