package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AuditLogMapper {

    int insert(AuditLogEntity record);

    /**
     * 审核记录管理分页列表
     * @param filter
     * @return
     */
    Page<AuditLogEntity> findByPage(Map<String, Object> filter);
}