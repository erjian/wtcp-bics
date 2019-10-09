package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ScenicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ScenicEntity record);

    ScenicEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeyWithBLOBs(ScenicEntity record);

    int updateByPrimaryKey(ScenicEntity record);

    /**
     * 分页列表
     * @param filter 查询参数
     * @return Page<ScenicEntity>
     */
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
                             @Param(value="deptCode") String deptCode, @Param(value="ids") List<Long> ids);
}