package cn.com.wanwei.bic.config;

import cn.com.wanwei.bic.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Order(Integer.MAX_VALUE)
@Slf4j
public class DataInitialize implements ApplicationRunner {

    @Autowired
    private CommonService commonService;

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
        List<Map<String, Object>> areaList;
        Object cachedAreaList = redisTemplate.opsForValue().get(Constant.AREA_LIST_CACHED_KEY);
        if (null != cachedAreaList) {
            areaList = commonService.getAreaListByPcode("", 6);
            if (null != areaList && CollectionUtils.isNotEmpty(areaList)) {
                redisTemplate.opsForValue().set(Constant.AREA_LIST_CACHED_KEY, areaList);
            }
        }
    }

}
