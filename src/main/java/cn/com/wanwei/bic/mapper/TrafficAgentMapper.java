package cn.com.wanwei.bic.mapper;
import cn.com.wanwei.bic.entity.TrafficAgentEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TrafficAgentMapper {
    int deleteByPrimaryKey(String id);

    int insert(TrafficAgentEntity record);

    @DataScope
    TrafficAgentEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(TrafficAgentEntity record);

    /**
     * 交通枢纽管理分页列表
     * @param filter
     * @return
     */
    @DataScope
    Page<TrafficAgentEntity> findByPage(Map<String, Object> filter);

    /**
     * 标题重名校验
     * @param title
     * @return
     */
    TrafficAgentEntity checkTitle(String title);

    /**
     * 数据绑定
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     */
    void dataBind(String updatedUser, String updatedDate, String deptCode, List<String> ids);

    /**
     * 交通枢纽管理列表
     * @return
     */
    @DataScope
    List<TrafficAgentEntity> getTrafficAgentList();

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
     * 交通枢纽列表
     * @param searchValue 搜索条件（标题  or 全拼  or 简拼）
     * @return
     */
    List<TrafficAgentEntity> findBySearchValue(String searchValue);

    /**
     * 根据高德数据id查询数据列表
     * @param gouldId
     * @return
     */
    List<TrafficAgentEntity> findByGouldId(String gouldId);
}