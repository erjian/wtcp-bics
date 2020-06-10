package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ExhibitsEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ExhibitsMapper {
    int deleteByPrimaryKey(String id);

    int insert(ExhibitsEntity record);

    ExhibitsEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(ExhibitsEntity record);

    /**
     * 标题重名校验
     * @param title
     * @return
     */
    List<ExhibitsEntity> checkTitle(String title);

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
     * 重置数据所有排序
     * @return
     */
    int clearWeight();

    /**
     * 获取最大的权重
     */
    Integer maxWeight();
    /**
     * 展品管理分页列表
     * @param filter
     * @return
     */
    @DataScope
    Page<ExhibitsEntity> findByPage(Map<String, Object> filter);


    /**
     * 获取展品信息
     * @return
     */
    List<ExhibitsEntity> getExhibitsList();

    List<ExhibitsEntity> findBySearchValue(@Param("searchValue") String searchValue, @Param("ids") List<String> ids);
}