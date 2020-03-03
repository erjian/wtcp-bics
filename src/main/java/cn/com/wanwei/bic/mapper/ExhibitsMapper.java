package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.entity.ExhibitsEntity;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface ExhibitsMapper {
    int deleteByPrimaryKey(String id);

    int insert(ExhibitsEntity record);

    int insertSelective(ExhibitsEntity record);

    ExhibitsEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ExhibitsEntity record);

    int updateByPrimaryKeyWithBLOBs(ExhibitsEntity record);

    int updateByPrimaryKey(ExhibitsEntity record);

    ExhibitsEntity checkTitle(String title);

    void dataBind(String updatedUser, String updatedDate, String deptCode, List<String> ids);

    Integer maxWeight();

    void clearWeight();

    Page<ExhibitsEntity> findByPage(Map<String, Object> filter);
}