package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.config.Constant;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/region")
@Api(value = "C端行政区划", tags = "C端行政区划相关接口")
public class OpenRegionController {

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "获取行政区划（省市县）树形列表", notes = "获取行政区划（省市县）树形列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseMessage list(){
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<Map<String, Object>> areaList = null;
        areaList = (List<Map<String, Object>>) redisTemplate.opsForValue().get(Constant.AREA_LIST_CACHED_KEY);
        if(null != areaList){
            responseMessage.setData(areaList);
        }
        return responseMessage;
    }

}
