package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.EnterpriseEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnterpriseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EnterpriseEntity record);

    EnterpriseEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(EnterpriseEntity record);
}