package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.TravelAgentEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TravelAgentMapper {
    int deleteByPrimaryKey(String id);

    int insert(TravelAgentEntity record);

    TravelAgentEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(TravelAgentEntity record);

    /**
     * 旅行社管理分页列表
     * @param filter
     * @return
     */
    Page<TravelAgentEntity> findByPage(Map<String, Object> filter);

    /**
     * 获取最大的权重
     * @return
     */
    Integer maxWeight();

    /**
     *  重置数据所有排序
     * @return
     */
    int clearWeight();

    /**
     * 标题重名校验
     * @param title
     * @return
     */
    TravelAgentEntity checkTitle(String title);

    /**
     * 数据绑定
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     * @return
     */
    int dataBind(@Param(value="updatedUser") String updatedUser, @Param(value="updatedDate") String updatedDate,
                 @Param(value="deptCode") String deptCode, @Param(value="ids") List<String> ids);

    /**
     * 获取休闲娱乐信息
     * @return
     */
    List<TravelAgentEntity> getTravelAgentList();
}