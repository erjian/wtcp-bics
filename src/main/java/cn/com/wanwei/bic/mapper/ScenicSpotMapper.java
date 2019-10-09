package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ScenicSpotEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScenicSpotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ScenicSpotEntity record);

    ScenicSpotEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ScenicSpotEntity record);

    /**
     * 景点管理分页列表
     * @param filter
     * @return
     */
    Page<ScenicSpotEntity> findByPage(Map<String, Object> filter);

    /**
     *标题重复校验
     * @param id
     * @param title
     * @return
     */
    List<ScenicSpotEntity> checkTitle(@Param(value = "id") Long id, @Param(value = "title") String title);
}