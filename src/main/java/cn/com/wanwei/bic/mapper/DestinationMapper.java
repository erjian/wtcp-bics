package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.DestinationEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface DestinationMapper {
    int deleteByPrimaryKey(String id);

    int insert(DestinationEntity record);

    DestinationEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(DestinationEntity record);

    /**
     * 分页查询目的地信息
     * @param filter
     * @return
     */
    Page<DestinationEntity> findByPage(Map<String, Object> filter);

}