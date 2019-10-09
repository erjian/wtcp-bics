package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ScenicEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ScenicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ScenicEntity record);

    ScenicEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeyWithBLOBs(ScenicEntity record);

    int updateByPrimaryKey(ScenicEntity record);

    /**
     * 分页列表
     * @param filter 查询参数
     * @return Page<ScenicEntity>
     */
    Page<ScenicEntity> findByPage(Map<String, Object> filter);
}