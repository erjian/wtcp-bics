package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.MaterialEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialMapper {

    int deleteByPrimaryKey(String id);

    int deleteOneByPidAndId(@Param("principalId")String principalId, @Param("id")String id);

    int deleteByPrincipalIds(@Param("ids") List<String> ids);

    int insert(MaterialEntity materialEntity);

    int batchInsert(@Param("list") List<MaterialEntity> list);

    MaterialEntity selectByPrimaryKey(String id);

    List<MaterialEntity> findByPrincipalId(String principalId);

    List<MaterialEntity> findByPidAndType(@Param("principalId") String principalId, @Param("type")String type);

    List<MaterialEntity> findByPidAndIdentify(@Param("principalId")String principalId, @Param("fileIdentify")String fileIdentify);

    int updateByPrimaryKey(MaterialEntity materialEntity);

    MaterialEntity findByIdAndPid(@Param("id")String id, @Param("principalId")String principalId);

    int deleteByPrincipalId(String principalId);

    int batchUpdateByPrincipalId(@Param("principalId") String principalId,@Param("timeId") String timeId);

    /**
     * 根据ids获取素材列表
     * @param ids ids
     * @return
     */
    List<MaterialEntity> findByIds(@Param("ids")List<String> ids);
}