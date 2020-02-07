package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.CateRelationEntity;
import cn.com.wanwei.bic.mapper.CateRelationMapper;
import cn.com.wanwei.bic.service.CateRelationService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CateRelationServiceImpl implements CateRelationService {

    @Autowired
    private CateRelationMapper cateRelationMapper;

    @Override
    public int deleteByPrimaryKey(String id) {
        return cateRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByCateringId(String cateringId) {
        return cateRelationMapper.deleteByCateringId(cateringId);
    }

    @Override
    public int deleteByCateId(String caterId) {
        return cateRelationMapper.deleteByCateId(caterId);
    }

    @Override
    public int insert(String cateringId, String caterId) {
        CateRelationEntity entity = new CateRelationEntity();
        entity.setId(UUIDUtils.getInstance().getId());
        entity.setCateId(caterId);
        entity.setCateringId(cateringId);
        return cateRelationMapper.insert(entity);
    }

    @Override
    public int batchInsert(List<String> cateIds, String cateringId) {
        List<CateRelationEntity> list = Lists.newArrayList();
        for (String itemId : cateIds) {
            CateRelationEntity cateRelation = new CateRelationEntity();
            cateRelation.setId(UUIDUtils.getInstance().getId());
            cateRelation.setCateId(itemId);
            cateRelation.setCateringId(cateringId);
            list.add(cateRelation);
        }
        return cateRelationMapper.batchInsert(list);
    }

    @Override
    public CateRelationEntity selectByPrimaryKey(String id) {
        return cateRelationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(CateRelationEntity entity) {
        return cateRelationMapper.updateByPrimaryKey(entity);
    }

    @Override
    public List<String> findByCateringId(String cateringId) {
        return cateRelationMapper.findByCateringId(cateringId);
    }

    @Override
    public List<String> findByCateId(String caterId) {
        return cateRelationMapper.findByCateId(caterId);
    }
}
