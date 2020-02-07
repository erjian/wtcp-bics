package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.CateRelationEntity;

import java.util.List;

public interface CateRelationService {

    int deleteByPrimaryKey(String id);

    int deleteByCateringId(String cateringId);

    int deleteByCateId(String caterId);

    int insert(String cateringId, String caterId);

    int batchInsert(List<String> cateIds, String cateringId);

    CateRelationEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(CateRelationEntity record);

    List<String> findByCateringId(String cateringId);

    List<String> findByCateId(String caterId);

}
