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
     *
     * @param filter 查询参数
     * @return Page<ScenicEntity>
     */
    @DataScope
    Page<ScenicEntity> findByPage(Map<String, Object> filter);

    Page<ScenicEntity> findByPageForFeign(Map<String, Object> filter);

    /**
     * 关联机构
     *
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     * @return
     */
    int dataBind(@Param(value = "updatedUser") String updatedUser, @Param(value = "updatedDate") Date updatedDate,
                 @Param(value = "deptCode") String deptCode, @Param(value = "ids") List<String> ids);

    /**
     * 查询景区信息
     */
    List<ScenicEntity> getScenicInfo(@Param("title") String title, @Param("status") Integer status, @Param("category") String category);

    /**
     * 根据景区名称和主键验重
     *
     * @param title
     * @param id
     * @return
     */
    List<ScenicEntity> findByTitleAndIdNot(@Param(value = "title") String title, @Param(value = "id") String id);

    /**
     * 根据区域获取景区列表
     *
     * @param filter 参数
     * @return 景区列表
     */
    Page<ScenicEntity> scenicPageNew(Map<String, Object> filter);

    /**
     * 根据ids查询景区列表
     *
     * @param ids id集合
     * @return
     */
    List<ScenicEntity> findListByIds(@Param("ids") List<String> ids, @Param("status") Integer status);

    /**
     * 景区列表
     *
     * @param type code
     * @param name 搜索条件（标题 or 全拼  or 简拼）
     * @return 普通景区  or 旅游示范村
     */
    List<ScenicEntity> findBySearchValue(@Param("type") String type, @Param("name") String name, @Param("ids") List<String> ids);

    /**
     * 获取统计数据
     *
     * @param map
     * @return
     * @author linjw 2019年12月19日15:15:15
     */
    Long getCount(Map<String, Object> map);

    /**
     * 景区电商-根据组织机构编码查询景区详情及标签
     * @param deptCode
     * @return
     */
    List<ScenicEntity> findByDeptCode(@Param(value = "deptCode") String deptCode);
}