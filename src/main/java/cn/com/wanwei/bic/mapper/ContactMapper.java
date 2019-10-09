package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ContactEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContactMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ContactEntity record);

    ContactEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ContactEntity record);
}