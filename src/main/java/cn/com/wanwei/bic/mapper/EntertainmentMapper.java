package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.EntertainmentEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EntertainmentMapper {

    int deleteByPrimaryKey(String id);

    int insert(EntertainmentEntity record);

    EntertainmentEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(EntertainmentEntity record);

    /**
     * 标题重名校验
     * @param title
     * @return
     */
    List<EntertainmentEntity> checkTitle(String title);

    /**
     * 休闲娱乐管理分页列表
     * @param filter
     * @return
     */
    @DataScope
    Page<EntertainmentEntity> findByPage(Map<String, Object> filter);

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
     * 重置数据所有排序
     * @return
     */
    int clearWeight();

    /**
     * 获取最大的权重
     */
    Integer maxWeight();

    /**
     * 获取休闲娱乐信息
     * @return
     */
    List<EntertainmentEntity> getEnterList();

    /**
     * 农家乐列表
     * @param name 条件
     * @return
     */
    List<EntertainmentEntity> findBySearchValue(@Param("name") String name, @Param("ids") List<String> ids);

    /**
     * 根据区域获取农家乐列表
     * @param filter 参数
     * @return 农家乐列表
     */
    Page<EntertainmentEntity> agritainmentsPageNew(Map<String,Object> filter);

    /**
     * 根据ids获取列表
     * @param ids ids
     * @param status 上下线
     * @return
     */
    List<EntertainmentEntity> findPageIds(@Param("ids")List<String> ids, @Param("status")String status);

    /**
     * 休闲娱乐数量
     * @param code 组织机构编码
     * @return
     */
    Long findByDeptCode(String code);
}