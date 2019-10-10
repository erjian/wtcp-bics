package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.EnterpriseEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnterpriseMapper {
    int deleteByPrimaryKey(String id);

    int insert(EnterpriseEntity record);

    EnterpriseEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(EnterpriseEntity record);
}