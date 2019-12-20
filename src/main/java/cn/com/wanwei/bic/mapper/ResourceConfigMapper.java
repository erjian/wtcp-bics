package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ResourceConfigEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceConfigMapper {

    /**
     * 新增一条记录
     * @param record
     * @return
     */
    int insert(ResourceConfigEntity record);

    /**
     * 根据主键查询一条记录
     * @param id
     * @return
     */
    ResourceConfigEntity selectByPrimaryKey(String id);

    /**
     * 根据code查询一条记录
     * @author linjw 2019年12月20日11:05:46
     * @param code
     * @return
     */
    ResourceConfigEntity selectByCode(String code);

    /**
     * 更新一条记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ResourceConfigEntity record);
}