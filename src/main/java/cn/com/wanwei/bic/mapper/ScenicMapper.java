package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ScenicMapper {
    int deleteByPrimaryKey(String id);

    int insert(ScenicEntity record);

    ScenicEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeyWithBLOBs(ScenicEntity record);

    int updateByPrimaryKey(ScenicEntity record);

    /**
     * 分页列表
     * @param filter 查询参数
     * @return Page<ScenicEntity>
     */
    @DataScope
    Page<ScenicEntity> findByPage(Map<String, Object> filter);

    /**
     * 关联机构
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     * @return
     */
    int dataBind(@Param(value="updatedUser") String updatedUser, @Param(value="updatedDate") Date updatedDate,
                             @Param(value="deptCode") String deptCode, @Param(value="ids") List<String> ids);

    /**
     * 查询景区信息
     * @param title
     * @return
     */
    List<ScenicEntity> getScenicInfo(@Param(value="title") String title);

    /**
     * 获取最大权重
     * @return
     */
    Integer maxWeight();

    /**
     * 重置权重
     * @return
     */
    int clearWeight();

    /**
     * 根据景区名称和主键验重
     * @param title
     * @param id
     * @return
     */
    List<ScenicEntity> findByTitleAndIdNot(@Param(value="title")String title, @Param(value="id")String id);

    /**
     * 统一查询最大权重
     * @param tableName
     * @return
     */
    Integer commonMaxWeight(@Param(value = "tableName") String tableName);

    /**
     * 统一重置权重值
     * @param tableName
     * @return
     */
    int commonClearWeight(@Param(value = "tableName") String tableName);

    /**
     *
     * @param id
     * @param weight
     * @param updatedUser
     * @param updatedDate
     * @param tableName
     * @return
     */
    int commonUpdateWeight(@Param(value = "id") String id, @Param(value = "weight") int weight, @Param(value = "updatedUser") String updatedUser, @Param(value = "updatedDate") Date updatedDate, @Param(value = "tableName") String tableName);

    /**
     * 景区列表
     * @param searchValue 搜索条件
     * @return
     */
    List<Map<String,Object>> findBySearchValue(String searchValue);
}