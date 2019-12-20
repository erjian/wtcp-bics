package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.model.FindStatusModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CommonMapper {

    FindStatusModel findById(@Param("id") String id, @Param("tableName") String tableName);

    int updateById(Map<String, Object> params);

}
