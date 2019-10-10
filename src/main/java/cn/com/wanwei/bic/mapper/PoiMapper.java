package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.PoiEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PoiMapper {
    int deleteByPrimaryKey(String id);

    int insert(PoiEntity record);

    PoiEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(PoiEntity record);

    /**
     * poi管理分页列表
     * @param filter
     * @return
     */
    Page<PoiEntity> findByPage(Map<String, Object> filter);

    /**
     *标题重复校验
     * @param id
     * @param title
     * @return
     */
    List<PoiEntity> checkTitle(@Param(value = "id") String id, @Param(value = "title") String title);
}