package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.HotelEntity;
import cn.com.wanwei.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HotelMapper extends BaseMapper<HotelEntity, String> {

    List<HotelEntity> getHotelInfo(@Param("title")String title);
}