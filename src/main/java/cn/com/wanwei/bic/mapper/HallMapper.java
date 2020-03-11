package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface HallMapper extends BaseMapper<HallEntity, String> {

    long countByVenueId(@Param("venueId") String venueId);

    /**
     * 根据场馆id修改机构编码
     * @auth linjw 2020年3月11日08:44:20
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     * @return
     */
    int updateDeptCodeByVenueId(@Param("updatedUser") String updatedUser, @Param("updatedDate") Date updatedDate, @Param("deptCode") String deptCode, @Param("ids") List<String> ids);
}