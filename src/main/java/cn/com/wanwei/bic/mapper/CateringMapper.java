package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.CateringEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface CateringMapper {
    int deleteById(String id);

    int insert(CateringEntity record);

    CateringEntity findById(String id);

    int updateById(CateringEntity record);

    /**
     * 分页列表
     * @param filter
     * @return
     */
    @DataScope
    Page<CateringEntity> findByPage(Map<String,Object> filter);

    /**
     * 不分页列表
     * @param filter
     * @return
     */
    @DataScope
    List<CateringEntity> findByList(Map<String,Object> filter);

    /**
     * 关联组织
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     */
    int updateDeptCode(@Param(value = "updatedUser") String updatedUser, @Param(value = "updatedDate") Date updatedDate,
                       @Param(value = "deptCode") String deptCode, @Param(value = "ids") List<String> ids);

    /**
     * 重命名校验
     * @param title
     * @param id
     * @return
     */
    List<CateringEntity> findByTitleAndIdNot(@Param(value="title")String title, @Param(value="id")String id);
}