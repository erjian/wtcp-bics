package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.BusinessEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessMapper {

    int insert(BusinessEntity record);

    BusinessEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(BusinessEntity record);

    BusinessEntity selectByPrincipalId(String principalId);
}