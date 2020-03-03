package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.HeritageEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface HeritageMapper {

    @DataScope
    Page<HeritageEntity> findByPage(Map<String, Object> filter);

    int deleteByPrimaryKey(String id);

    int insert(HeritageEntity record);

    HeritageEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(HeritageEntity record);
    
    int dataBind(@Param(value="updatedUser") String updatedUser, @Param(value="updatedDate") Date updatedDate,
                 @Param(value="deptCode") String deptCode, @Param(value="ids") List<String> ids);

    List<HeritageEntity> existsByTitleAndIdNot(@Param("title") String title, @Param("id") String id);

}