package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.model.FindStatusModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

@Mapper
public interface CommonMapper {

    FindStatusModel findById(@Param("id") String id, @Param("tableName") String tableName);

    int updateById(Map<String, Object> params);

    /**
     * 统一查询最大权重
     * @param tableName
     * @return
     */
    Integer commonMaxWeight(@Param(value = "tableName") String tableName);

    /**
     * 统一重置权重值
     * @param tableName
     * @return
     */
    int commonClearWeight(@Param(value = "tableName") String tableName);

    /**
     *
     * @param id
     * @param weight
     * @param updatedUser
     * @param updatedDate
     * @param tableName
     * @return
     */
    int commonUpdateWeight(@Param(value = "id") String id, @Param(value = "weight") int weight, @Param(value = "updatedUser") String updatedUser, @Param(value = "updatedDate") Date updatedDate, @Param(value = "tableName") String tableName);

}
