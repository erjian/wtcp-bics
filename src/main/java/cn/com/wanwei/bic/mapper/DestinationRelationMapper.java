package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.DestinationRelationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DestinationRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(DestinationRelationEntity record);

    DestinationRelationEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(DestinationRelationEntity record);

    /**
     * 根据目的地id查询关联信息
     * @param id
     * @return
     */
    List<DestinationRelationEntity> findPrincipalByDestinationId(String id);
}