package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.PoiEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AuditLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(AuditLogEntity record);

    AuditLogEntity selectByPrimaryKey(String id);

    /**
     * 审核记录管理分页列表
     * @param filter
     * @return
     */
    Page<PoiEntity> findByPage(Map<String, Object> filter);
}