package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.ResourceConfigEntity;
import cn.com.wanwei.bic.mapper.ResourceConfigMapper;
import cn.com.wanwei.bic.mapper.ScenicMapper;
import cn.com.wanwei.bic.service.ResourceService;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.utils.DataScopeUtils;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author **
 */
@Service
@Slf4j
@RefreshScope
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ScenicMapper scenicMapper;

    @Autowired
    private ResourceConfigMapper resourceConfigMapper;

    private final String prefix = "t_bic_";

    @Override
    public ResponseMessage findByPieChart(User user, Map<String, Object> queryModel) {

        //1、获取查询参数
        List<Map<String, Object>> resourceList = (List<Map<String, Object>>)queryModel.get("resource_list");

        //2、循环查询数据并进行相应的赋值
        if(CollectionUtils.isNotEmpty(resourceList)){
            for(Map<String, Object> map:resourceList){
                map.put("deptCode", DataScopeUtils.getDataScope());
                String table = String.valueOf(map.get("table"));
                if(table.indexOf(prefix) == -1){
                    map.put("table", prefix.concat(table));
                }
                Long count = scenicMapper.getCount(map);
                if(!count.equals(0)){
                    map.put("value", count);
                }
            }
        }
        return ResponseMessage.defaultResponse().setData(resourceList);
    }

    @Override
    public ResponseMessage initPieByCode(User currentUser, String code, Integer size) {
        //1、从资源配置表中获取当前资源code相关的配置
        ResourceConfigEntity entity = resourceConfigMapper.selectByCode(code);
        //2、判断当前资源相关配置是否为空
        if(null == entity){
            return ResponseMessage.validFailResponse().setMsg("当前资源没有相关配置");
        }
        //3、判断当前资源的下级资源是否全部配置
        List<ResourceConfigEntity> entities = resourceConfigMapper.selectByParentCode(code);
        if(CollectionUtils.isEmpty(entities) || (entity.getQueryWay().equals("0") && entities.size() < size)){
            return ResponseMessage.validFailResponse().setMsg("当前资源的下级资源没有配置完全");
        }

        //4、判断当前编码配置是单表查询还是多表查询：queryWay 1 单表 0 多表
        List<Map<String, Object>> list = Lists.newArrayList();
        for(ResourceConfigEntity configEntity:entities){
            Map<String, Object> map = Maps.newHashMap();
            map.put("name", configEntity.getName());
            map.put("code", configEntity.getCode());
            map.put("deptCode", DataScopeUtils.getDataScope());
            if(StringUtil.isNotEmpty(entity.getQueryWay()) && entity.getQueryWay().equals("1")){ //单表查询
                map.put("table",entity.getTableName());
                map.put("column", entity.getColumnName());
                map.put("typeVal", configEntity.getCode());
            }
            if(StringUtil.isNotEmpty(entity.getQueryWay()) && entity.getQueryWay().equals("0")){ //多表查询
                map.put("table",configEntity.getTableName());
            }
            Long count = scenicMapper.getCount(map);
            map.put("value", (null == count || count.equals(0))?0:count);
            list.add(map);
        }
        return ResponseMessage.defaultResponse().setData(list);
    }

    @Override
    public ResponseMessage findByBarChart(User user, Map<String, Object> queryModel) {
        //1、从queryModel中取出所有的参数
        List<Map<String, Object>> barSeries = (List<Map<String, Object>>)queryModel.get("barSeries");
        List<Map<String, Object>> barXAxisDataRel = (List<Map<String, Object>>)queryModel.get("barXAxisDataRel");
//        List<String> barXAxisData = (List<String>)queryModel.get("barXAxisData");

        //2、循环barSeries查询并初始化数据
        if(CollectionUtils.isNotEmpty(barSeries) && CollectionUtils.isNotEmpty(barSeries)) {
            for (Map map : barSeries) {
                map.put("deptCode", DataScopeUtils.getDataScope());
                String table = String.valueOf(map.get("table"));
                if (table.indexOf(prefix) == -1) {
                    map.put("table", prefix.concat(table));
                }
                List<Long> data = Lists.newArrayList();
                for (Map map1 : barXAxisDataRel) {
                    map.put("region", String.valueOf(map1.get("areaCode")));
                    Long count = scenicMapper.getCount(map);
                    data.add((null == count || count.equals(0))?0:count);
                }
                map.put("data", data);
            }
        }
        return ResponseMessage.defaultResponse().setData(barSeries);
    }

}
