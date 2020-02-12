package cn.com.wanwei.bic.mapper;

import cn.com.wanwei.bic.model.DataSyncModel;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface DataSyncMapper {

    Page<DataSyncModel> findScenicByPage(Map<String, Object> filter);

    /**
     * 获取周边的分页数据，并返回指定的字段
     * @author linjw 2020年2月12日16:14:39
     * @param filter
     * @return
     */
    Page<DataSyncModel> findPeripheryByPage(Map<String, Object> filter);

    /**
     * 获取农家乐的分页数据，并返回指定的字段
     * @author linjw 2020年2月12日16:30:46
     * @param filter
     * @return
     */
    Page<DataSyncModel> findEntertainmentByPage(Map<String, Object> filter);

    /**
     * 获取自驾营地的分页数据，并返回指定的字段
     * @author linjw 2020年2月12日16:30:46
     * @param filter
     * @return
     */
    Page<DataSyncModel> findDriveCampByPage(Map<String, Object> filter);

    /**
     * 获取自驾营地的分页数据，并返回指定的字段
     * @author linjw 2020年2月12日16:30:46
     * @param filter
     * @return
     */
    Page<DataSyncModel> findTrafficAgentByPage(Map<String, Object> filter);
}
