package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.CateRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CateRelationMapper {

    int deleteByPrimaryKey(@Param("id") String id);

    int deleteByCateringId(@Param("cateringId") String cateringId);

    int deleteByCateId(@Param("caterId") String caterId);

    int insert(CateRelationEntity record);

    int batchInsert(@Param("list") List<CateRelationEntity> list);

    CateRelationEntity selectByPrimaryKey(@Param("id") String id);

    int updateByPrimaryKey(CateRelationEntity record);

    List<String> findByCateringId(@Param("cateringId") String cateringId);

    List<String> findByCateId(@Param("caterId") String caterId);
}