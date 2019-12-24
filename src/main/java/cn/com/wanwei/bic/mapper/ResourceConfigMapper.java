package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ResourceConfigEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询表信息
     * @author linjw 2019年12月20日17:03:42
     * @return
     */
    List<Map<String, Object>> selectTableInfo();

    /**
     * 根据表名查询对应的字段信息
     * @author linjw 2019年12月20日17:04:18
     * @param tableName
     * @return
     */
    List<Map<String, Object>> selectColumnInfo(String tableName);

    /**
     * 根据父级编码获取所有的子级配置
     * @author linjw 2019年12月23日16:26:25
     * @param code
     * @return
     */
    List<ResourceConfigEntity> selectByParentCode(String code);
}