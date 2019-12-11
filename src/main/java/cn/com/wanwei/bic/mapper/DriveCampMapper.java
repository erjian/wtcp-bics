package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.DriveCampEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DriveCampMapper {
    int deleteByPrimaryKey(String id);

    int insert(DriveCampEntity record);

    DriveCampEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(DriveCampEntity record);

    /**
     *
     * @param filter
     * @return
     */
    @DataScope
    Page<DriveCampEntity> findByPage(Map<String, Object> filter);

    /**
     *获取最大的权重
     * @return
     */
    Integer maxWeight();

    /**
     *重置数据所有排序
     */
    Integer clearWeight();

    /**
     *标题重名校验
     * @param title
     * @return
     */
    DriveCampEntity checkTitle(String title);

    /**
     *数据绑定
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     */
    void dataBind(@Param(value="updatedUser") String updatedUser, @Param(value="updatedDate") String updatedDate,
                  @Param(value="deptCode") String deptCode, @Param(value="ids") List<String> ids);

    /**
     *获取自驾营地信息
     * @return
     */
    @DataScope
    List<DriveCampEntity> getDriveCampList();
}