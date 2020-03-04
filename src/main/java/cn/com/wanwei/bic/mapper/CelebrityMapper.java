package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.CelebrityEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface CelebrityMapper {
    int deleteByPrimaryKey(String id);

    int insert(CelebrityEntity record);

    int insertSelective(CelebrityEntity record);

    CelebrityEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CelebrityEntity record);

    int updateByPrimaryKeyWithBLOBs(CelebrityEntity record);

    int updateByPrimaryKey(CelebrityEntity record);

    Page<CelebrityEntity> findByPage(Map<String,Object> filter);

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
}