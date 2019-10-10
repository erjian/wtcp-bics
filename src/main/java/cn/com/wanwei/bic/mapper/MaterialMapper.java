package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialMapper {

    int deleteByPrimaryKey(String id);

    int deleteByPrincipalIds(@Param(value = "ids") List<String> ids);

    int insert(MaterialEntity materialEntity);

    int batchInsert(@Param(value = "list") List<MaterialEntity> list);

    MaterialEntity selectByPrimaryKey(String id);

    List<MaterialEntity> findByPrincipalId(String principalId);

    List<MaterialEntity> findByPidAndType(@Param(value = "principalId") String principalId, @Param(value = "type")String type);

    List<MaterialEntity> findByPidAndIdentify(@Param(value = "principalId")String principalId, @Param(value = "fileIdentify")Integer fileIdentify);

    int updateByPrimaryKey(MaterialEntity materialEntity);

}