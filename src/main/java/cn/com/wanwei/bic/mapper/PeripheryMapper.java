package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.PeripheryEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PeripheryMapper {
    int deleteByPrimaryKey(String id);

    int insert(PeripheryEntity record);

    @DataScope
    PeripheryEntity selectByPrimaryKey(String id);

    /**
     * 修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(PeripheryEntity record);

    /**
     * 查询分页
     * @param filter
     * @return
     */
    @DataScope
    Page<PeripheryEntity> findByPage(Map<String,Object> filter);

    /**
     * 标题重名校验
     * @param title
     * @return
     */
    PeripheryEntity checkTitle(String title);

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(@Param(value="ids") List<String> ids);

    /**
     * 数据绑定
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     */
    int dataBind(@Param(value="updatedUser") String updatedUser, @Param(value="updatedDate") String updatedDate,
                  @Param(value="deptCode") String deptCode, @Param(value="ids") List<String> ids);

    /**
     * 查询最大权重值
     * @return
     */
    Integer maxWeight();

    /**
     * 清空排序
     */
    void clearWeight();
}