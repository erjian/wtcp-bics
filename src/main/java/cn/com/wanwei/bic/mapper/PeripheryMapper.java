package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.PeripheryEntity;
import cn.com.wanwei.common.annotation.DataScope;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface PeripheryMapper {
    int deleteByPrimaryKey(String id);

    int insert(PeripheryEntity record);

    @DataScope
    PeripheryEntity selectByPrimaryKey(String id);

    int updateByPrimaryKey(PeripheryEntity record);

    /**
     * 查询分页
     * @param filter
     * @return
     */
    @DataScope
    Page<PeripheryEntity> findByPage(Map<String,Object> filter);

    /**
     * 标题重名校验
     * @param title
     * @return
     */
    PeripheryEntity checkTitle(String title);
}