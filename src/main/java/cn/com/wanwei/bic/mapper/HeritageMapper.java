package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.HeritageEntity;
import cn.com.wanwei.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HeritageMapper extends BaseMapper<HeritageEntity, String> {
    List<HeritageEntity> findBySearchValue(@Param("searchValue") String searchValue, @Param("ids") List<String> ids);
}