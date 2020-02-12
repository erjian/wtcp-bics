package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.model.DataSyncModel;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface DataSyncMapper {

    Page<DataSyncModel> findScenicByPage(Map<String, Object> filter);


}
