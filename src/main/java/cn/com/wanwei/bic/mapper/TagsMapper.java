package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.BaseTagsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface TagsMapper {

    int deleteByPrincipalId(@Param(value = "principalId") String principalId, @Param(value = "tableName") String tableName);

    int batchInsert(Map<String, Object> params);

    BaseTagsEntity selectByPrimaryKey(@Param(value = "id") String id, @Param(value = "tableName") String tableName);

}