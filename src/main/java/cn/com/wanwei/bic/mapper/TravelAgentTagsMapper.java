package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.TravelAgentTagsEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TravelAgentTagsMapper {
    int deleteByPrimaryKey(String id);

    int insert(TravelAgentTagsEntity record);

    TravelAgentTagsEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(TravelAgentTagsEntity record);
}