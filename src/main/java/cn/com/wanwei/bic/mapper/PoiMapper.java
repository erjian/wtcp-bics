package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface PoiMapper {
    int deleteByPrimaryKey(String id);

    int insert(PoiEntity record);

    @DataScope
    PoiEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(PoiEntity record);

    /**
     * poi管理分页列表
     * @param filter
     * @return
     */
    @DataScope
    Page<PoiEntity> findByPage(Map<String, Object> filter);

    /**
     *标题重复校验
     * @param title
     * @return
     */
    PoiEntity checkTitle( String title);

    /**
     * 查询一级景点
     * @param type 景点
     * @param principalId 景区
     * @param id poiID
     * @return
     */
    List<PoiEntity> findScenicList(@Param(value = "type")String type,
                                   @Param(value = "principalId")String principalId,
                                   @Param(value = "id")String id);

    /**
     * 批量删除poi信息
     * @param ids
     */
    void batchDelete(@Param(value="ids") List<String> ids);

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
     * 查询POI信息
     * @param principalId
     * @param type
     * @return
     */
    List<PoiEntity> getList(@Param(value = "principalId")String principalId, @Param(value = "type")String type);
}