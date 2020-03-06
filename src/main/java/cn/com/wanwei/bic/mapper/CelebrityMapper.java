package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.CelebrityEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface CelebrityMapper {
    int deleteById(String id);

    int insert(CelebrityEntity record);

    CelebrityEntity findById(String id);

    int updateById(CelebrityEntity record);

    @DataScope
    Page<CelebrityEntity> findByPage(Map<String, Object> filter);

    /**
     * 关联机构
     *
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     * @return
     */
    int updateDeptCode(@Param(value = "updatedUser") String updatedUser, @Param(value = "updatedDate") Date updatedDate,
                 @Param(value = "deptCode") String deptCode, @Param(value = "ids") List<String> ids);

    /**
     * 获取名人列表
     * @return
     */
    List<CelebrityEntity> findByList();
}