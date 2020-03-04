package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.VenueEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface VenueMapper {
    int deleteById(String id);

    int insert(VenueEntity record);

    VenueEntity findById(String id);

    int updateById(VenueEntity record);

    /**
     * 获取分页列表
     * @param filter 查询参数
     * @return Page<VenueEntity>
     */
    @DataScope
    Page<VenueEntity> findByPage(Map<String, Object> filter);

    Page<VenueEntity> findByPageForFeign(Map<String, Object> filter);

    /**
     * 关联机构
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     * @return
     */
    int updateDataBind(@Param(value="updatedUser") String updatedUser, @Param(value="updatedDate") Date updatedDate,
                 @Param(value="deptCode") String deptCode, @Param(value="ids") List<String> ids);

    List<VenueEntity> findVenueInfo(@Param(value="title") String title, @Param(value="status") Integer status);

    List<VenueEntity> findByTitleAndIdNot(@Param(value="title")String title, @Param(value="id")String id);
}