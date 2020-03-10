package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.HotelEntity;
import cn.com.wanwei.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelMapper extends BaseMapper<HotelEntity, String> {
}