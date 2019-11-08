package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.DestinationEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface DestinationMapper {
    int deleteByPrimaryKey(String id);

    int insert(DestinationEntity record);

    DestinationEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(DestinationEntity record);

    /**
     * 分页查询目的地信息
     * @param filter
     * @return
     */
    @DataScope
    Page<DestinationEntity> findByPage(Map<String, Object> filter);

    /**
     * 校验目的地名称的唯一性
     * @param regionFullCode
     * @return
     */
    DestinationEntity checkRegionFullCode(String regionFullCode);

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
     * 获取最大权重
     * @return
     */
    Integer maxWeight();

    /**
     * 重置权重
     * @return
     */
    int clearWeight();
}