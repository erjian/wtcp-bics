package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ExtendEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ExtendMapper {
    int deleteByPrimaryKey(String id);

    int insert(ExtendEntity record);

    ExtendEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeyWithBLOBs(ExtendEntity record);

    int updateByPrimaryKey(ExtendEntity record);

    /**
     * 扩展信息列表查询
     * @param filter
     * @return
     */
    Page<ExtendEntity> findByPage(Map<String, Object> filter);
}