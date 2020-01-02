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
                map.put("pieRegion", String.valueOf(queryModel.get("pieRegion")));
                map.put("startDate", String.valueOf(queryModel.get("startDate")));
                map.put("endDate", String.valueOf(queryModel.get("endDate")));
                String table = String.valueOf(map.get("table"));
                if(table.indexOf(prefix) == -1){
                    map.put("table", prefix.concat(table));
                }
                Long count = scenicMapper.getCount(map);
                if(!count.equals(0L)){
                    map.put("value", count);
                }
            }
        }
        return ResponseMessage.defaultResponse().setData(resourceList);
    }

    @Override
    public ResponseMessage findByBarChart(User user, Map<String, Object> queryModel) {
        //1、从queryModel中取出所有的参数
        List<Map<String, Object>> barSeries = (List<Map<String, Object>>)queryModel.get("barSeries");
        List<Map<String, Object>> barXAxisDataRel = (List<Map<String, Object>>)queryModel.get("barXAxisDataRel");

        //2、循环barSeries查询并初始化数据
        if(CollectionUtils.isNotEmpty(barSeries)) {
            for (Map map : barSeries) {
                map.put("deptCode", DataScopeUtils.getDataScope());
                map.put("startDate", String.valueOf(queryModel.get("startDate")));
                map.put("endDate", String.valueOf(queryModel.get("endDate")));
                String table = String.valueOf(map.get("table"));
                if (table.indexOf(prefix) == -1) {
                    map.put("table", prefix.concat(table));
                }
                List<Long> data = Lists.newArrayList();
                for (Map map1 : barXAxisDataRel) {
                    map.put("region", String.valueOf(map1.get("areaCode")));
                    Long count = scenicMapper.getCount(map);
                    data.add((null == count || count.equals(0L))?0:count);
                }
                map.put("data", data);
            }
        }
        return ResponseMessage.defaultResponse().setData(barSeries);
    }

    @Override
    public ResponseMessage initPieByCode(User currentUser, Map<String, Object> queryParams) {

        //1、从queryParams中获取相关查询参数
        String code = String.valueOf(queryParams.get("resourceCode"));
        Integer size = (Integer)queryParams.get("rChildLength");
        String pieRegion = String.valueOf(queryParams.get("pieRegion"));

        //2、从资源配置表中获取当前资源code相关的配置
        ResourceConfigEntity entity = resourceConfigMapper.selectByCode(code);
        List<ResourceConfigEntity> entities = resourceConfigMapper.selectByParentCode(code);

        //3、判断当前资源相关配置是否为空
        if(null == entity){
            return ResponseMessage.validFailResponse().setMsg("当前资源没有相关配置");
        }

        //4、判断当前资源的下级资源是否全部配置
        if(CollectionUtils.isEmpty(entities) || (entity.getQueryWay().equals("0") && entities.size() < size)){
            return ResponseMessage.validFailResponse().setMsg("当前资源的下级资源没有配置完全");
        }
        //5、返回数据
        return initData("pie",entity,entities,null,queryParams,null,pieRegion);
    }

    private ResponseMessage initData(String flag,ResourceConfigEntity entity, List<ResourceConfigEntity> entities,List<Map<String, Object>> barXAxisDataRel, Map<String, Object> queryParams, Map<String, Object> label, String pieRegion){
        String startDate = String.valueOf(queryParams.get("startDate"));
        String endDate = String.valueOf(queryParams.get("endDate"));

        List<Map<String, Object>> list = Lists.newArrayList();
        for(ResourceConfigEntity configEntity:entities){
            Map<String, Object> map = Maps.newHashMap();
            map.put("name", configEntity.getName());
            map.put("code", configEntity.getCode());
            map.put("deptCode", DataScopeUtils.getDataScope());

            map.put("startDate", startDate);
            map.put("endDate", endDate);
            if(StringUtil.isNotEmpty(entity.getQueryWay()) && entity.getQueryWay().equals("1")){ //单表查询
                map.put("table",entity.getTableName());
                map.put("column", entity.getColumnName());
                map.put("typeVal", configEntity.getCode());
            }
            if(StringUtil.isNotEmpty(entity.getQueryWay()) && entity.getQueryWay().equals("0")){ //多表查询
                map.put("table",configEntity.getTableName());
            }
            if(flag.equals("bar")){
                map.put("type", "bar");
                map.put("stack", "总量");
                map.put("barMaxWidth", 50);
                if(null != label){
                    map.put("label", label);
                }
                List<Long> data = Lists.newArrayList();
                for (Map map1 : barXAxisDataRel) {
                    map.put("region", String.valueOf(map1.get("areaCode")));
                    Long count = scenicMapper.getCount(map);
                    data.add((null == count || count.equals(0L))?0:count);
                }
                map.put("data", data);
            }
            if(flag.equals("pie")){
                if(StringUtil.isNotEmpty(pieRegion)){
                    map.put("pieRegion", pieRegion);
                }
                Long count = scenicMapper.getCount(map);
                map.put("value", (null == count || count.equals(0L))?0:count);
            }
            list.add(map);
        }
        return ResponseMessage.defaultResponse().setData(list);
    }

    @Override
    public ResponseMessage initBarByCode(User currentUser, Map<String, Object> queryParams) {

        //1、初始化各种参数
        Map<String, Object> label = Maps.newHashMap();
        label.put("show", true);
        String code = String.valueOf(queryParams.get("resourceCode"));
        Integer size = (Integer)queryParams.get("rChildLength");
        List<Map<String, Object>> barXAxisDataRel = (List<Map<String, Object>>)queryParams.get("barXAxisDataRel");

        //2、从资源配置表中获取当前资源code相关的配置
        ResourceConfigEntity entity = resourceConfigMapper.selectByCode(code);
        List<ResourceConfigEntity> entities = resourceConfigMapper.selectByParentCode(code);

        //3、判断当前资源相关配置是否为空
        if(null == entity){
            return ResponseMessage.validFailResponse().setMsg("当前资源没有相关配置");
        }

        //4、判断当前资源的下级资源是否全部配置
        if(CollectionUtils.isEmpty(entities) || (entity.getQueryWay().equals("0") && entities.size() < size)){
            return ResponseMessage.validFailResponse().setMsg("当前资源的下级资源没有配置完全");
        }
        //5、返回数据
        return initData("bar",entity,entities,barXAxisDataRel,queryParams,label, "");
    }

}
