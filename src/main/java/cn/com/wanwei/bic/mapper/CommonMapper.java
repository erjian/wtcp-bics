package cn.com.wanwei.bic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CommonMapper {

    Map<String, Object> findById(@Param("id") String id, @Param("tableName") String tableName);

    int updateById(@Param("params") Map<String, Object> params);

}
