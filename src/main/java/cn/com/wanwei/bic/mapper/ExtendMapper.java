package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ExtendEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExtendMapper {
    int deleteByPrimaryKey(String id);

    int insert(ExtendEntity record);

    ExtendEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(ExtendEntity record);

    /**
     * 扩展信息列表查询
     * @param filter
     * @return
     */
    @DataScope
    Page<ExtendEntity> findByPage(Map<String, Object> filter);

    /**
     * 标题重名校验
     * @param title
     * @return
     */
    ExtendEntity checkTitle(String title);

    /**
     * 扩展信息组织关联
     * @param updatedUser
     * @param updatedDate
     * @param deptCode
     * @param ids
     */
    void dataBindExtend(@Param(value="updatedUser") String updatedUser, @Param(value="updatedDate") String updatedDate,
                        @Param(value="deptCode") String deptCode, @Param(value="ids") List<String> ids);

    /**
     * 获取扩展信息列表
     * @param principalId
     * @param type
     * @return
     */
    List<ExtendEntity> getList( @Param(value="principalId")String principalId,@Param(value="type") Integer type);
}