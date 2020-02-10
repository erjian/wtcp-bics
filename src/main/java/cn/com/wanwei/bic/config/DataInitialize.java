package cn.com.wanwei.bic.config;

import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.common.model.ResponseMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Order(Integer.MAX_VALUE)
@Slf4j
public class DataInitialize implements ApplicationRunner {

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) {
        // 1、一次性初始化前三级行政区划编码
        this.initRegionData();

    }

    /**
     * 一次性初始化前三级行政区划编码,方便所属地区多选使用
     */
    private void initRegionData() {
        List<Map<String, Object>> areaList = getAreaListByPcode("", 6);
        if (null != areaList && CollectionUtils.isNotEmpty(areaList)) {
            redisTemplate.opsForValue().set(Constant.AREA_LIST_CACHED_KEY, areaList);
        }
    }

    /**
     * 根据父级行政区划获取子行政区域
     *
     * @param pcode  父级行政区域编码
     * @param length 获取叶子区域的级别， 如：6 代表仅获取到县级区域
     * @return List<Map < String, Object>>
     */
    private List<Map<String, Object>> getAreaListByPcode(String pcode, int length) {
        ResponseMessage responseMessageByCity = coderServiceFeign.areaList(pcode);
        if (responseMessageByCity.getStatus() == 1 && responseMessageByCity.getData() != null) {
            JsonArray citys = new JsonParser().parse(responseMessageByCity.getData().toString()).getAsJsonArray();
            List<Map<String, Object>> areaList = new ArrayList<>();
            citys.forEach(areaObject -> {
                Map<String, Object> areaMap = new HashMap<>();
                JsonObject area = areaObject.getAsJsonObject();
                String code = area.get("code").getAsString();
                String name = area.get("name").getAsString();
                areaMap.put(Constant.LABEL, name);
                areaMap.put(Constant.VALUE, name);
                areaMap.put("code", code);
                //此处设置仅查询县级及以上数据
                if (code.length() < length) {
                    areaMap.put(Constant.CHILDREN, getAreaListByPcode(code, length));
                }
                areaList.add(areaMap);
            });
            return areaList;
        }
        return null;
    }

}
