package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.RentalCarEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface RentalCarMapper {
    int deleteByPrimaryKey(String id);

    int insert(RentalCarEntity record);

    RentalCarEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeyWithBLOBs(RentalCarEntity record);

    int updateByPrimaryKey(RentalCarEntity record);

    /**
     * 分页列表
     * @param filter 查询参数
     * @return Page<ScenicEntity>
     */
    @DataScope
    Page<RentalCarEntity> findByPage(Map<String, Object> filter);

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
     * 查询租车信息
     * @param title 搜索条件
     * @return
     */
    List<RentalCarEntity> getRentalCarInfo(@Param(value="title") String title);

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
     * 根据租车信息名称和主键验重
     * @param title
     * @param id
     * @return
     */
    List<RentalCarEntity> findByTitleAndIdNot(@Param(value="title")String title, @Param(value="id")String id);

    /**
     * 租车数量
     * @param code 组织机构编码
     * @return
     */
    Long findByDeptCode(String code);
}