package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.mapper.ScenicMapper;
import cn.com.wanwei.bic.service.ResourceService;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
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

    private final String prefix = "t_bic_";

    @Override
    public ResponseMessage findByPieChart(User user, Map<String, Object> queryModel) {

        //1、获取查询参数
        List<Map<String, Object>> resourceList = (List<Map<String, Object>>)queryModel.get("resource_list");

        //2、循环查询数据并进行相应的赋值
        if(CollectionUtils.isNotEmpty(resourceList)){
            for(Map<String, Object> map:resourceList){
                if(user.getDsType() != 1){
                    map.put("deptCode", user.getOrg().getCode());
                }
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
//        ResourceConfigEntity entity = resourceConfigMapper.selectByCode(code);
        //2、判断当前资源相关配置是否为空
//        if(null == entity){
//            return ResponseMessage.validFailResponse().setMsg("当前资源没有相关配置");
//        }
        //3、判断当前资源的下级资源是否全部配置
//        List<ResourceConfigEntity> entities = resourceConfigMapper.selectByParentCode(code);
//        if(CollectionUtils.isEmpty(entities) || entities.size() < size){
//            return ResponseMessage.validFailResponse().setMsg("当前资源的下级资源没有配置完全");
//        }
        /**
         * 假设当前code就是景区且已经进行了相关配置：单表查询1，表名 t_bic_scenic,类型字段名 category
         * 类型对应关系{name: '普通景区', code: '125001001'},{name: '旅游示范村', code: '125001002'}
         */
        List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map1 = Maps.newHashMap();
        map1.put("name","普通景区");
        map1.put("code","125001001");
        map1.put("table","t_bic_scenic");
        map1.put("column","category");
        map1.put("typeVal","125001001");
        Map<String, Object> map2 = Maps.newHashMap();
        map2.put("name","旅游示范村");
        map2.put("code","125001002");
        map2.put("table","t_bic_scenic");
        map2.put("column","category");
        map2.put("typeVal","125001002");
        list.add(map1);
        list.add(map2);

        for(Map<String, Object> map:list){
            if(currentUser.getDsType() != 1){
                map.put("deptCode", currentUser.getOrg().getCode());
            }
            Long count = scenicMapper.getCount(map);
            map.put("value", (null == count || count.equals(0))?0:count);
        }
        return ResponseMessage.defaultResponse().setData(list);
    }

}
