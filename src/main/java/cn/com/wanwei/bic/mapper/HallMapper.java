package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.common.annotation.DataScope;
import cn.com.wanwei.common.model.ResponseMessage;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface HallMapper {

    int deleteByPrimaryKey(String id);

    int insert(HallEntity hallEntity);

    HallEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(HallEntity hallEntity);

    @DataScope
    Page<HallEntity> findByPage(Map<String, Object> filter);

    Page<HallEntity> findByPageForFeign(Map<String, Object> filter);

    List<HallEntity> existsByTitleAndIdNot(@Param("title") String title, @Param("id") String id);

    ResponseMessage findByList(Map<String, Object> filter);

    int dataBind(@Param(value="updatedUser") String updatedUser, @Param(value="updatedDate") Date updatedDate,
                 @Param(value="deptCode") String deptCode, @Param(value="ids") List<String> ids);

    long countByVenueId(@Param("venueId") String venueId);
}