package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.DestinationEntity;
import cn.com.wanwei.common.annotation.DataScope;
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
    @DataScope
    Page<DestinationEntity> findByPage(Map<String, Object> filter);

    /**
     * 校验目的地名称的唯一性
     * @param regionFullCode
     * @return
     */
    DestinationEntity checkRegionFullCode(String regionFullCode);

}