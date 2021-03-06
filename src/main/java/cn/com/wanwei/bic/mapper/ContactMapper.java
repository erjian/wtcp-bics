package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ContactEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContactMapper {

    int insert(ContactEntity record);

    ContactEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(ContactEntity record);

    ContactEntity selectByPrincipalId(String principalId);
}