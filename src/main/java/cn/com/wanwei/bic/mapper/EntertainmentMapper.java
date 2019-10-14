package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.EntertainmentEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface EntertainmentMapper {
    int deleteByPrimaryKey(String id);

    int insert(EntertainmentEntity record);

    @DataScope
    EntertainmentEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(EntertainmentEntity record);

    /**
     * 标题重名校验
     * @param title
     * @return
     */
    EntertainmentEntity checkTitle(String title);

    /**
     * 休闲娱乐管理分页列表
     * @param filter
     * @return
     */
    @DataScope
    Page<EntertainmentEntity> findByPage(Map<String, Object> filter);
}