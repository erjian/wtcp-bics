package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author
 * @date 2019年12月17日09:57:14
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/resource")
@Api(value = "资源管理", tags = "资源管理相关接口")
public class ResourceController extends BaseController{

    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "饼状图", notes = "饼状图")
    @RequestMapping(value = "/findByPieChart", method = RequestMethod.POST)
    public ResponseMessage findByPieChart(@RequestBody Map<String, Object> queryModel)throws Exception{
        return resourceService.findByPieChart(getCurrentUser(), queryModel);
    }

    @ApiOperation(value = "根据资源编码Code获取饼状图的数据", notes = "根据资源编码Code获取饼状图的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "资源编码", required = true),
            @ApiImplicitParam(name = "size", value = "资源子级资源数量", required = true)
    })
    @RequestMapping(value = "/initPieByCode", method = RequestMethod.GET)
    public ResponseMessage initPieByCode(@RequestParam String code, @RequestParam Integer size) throws Exception{
        return resourceService.initPieByCode(getCurrentUser(), code, size);
    }

//    @ApiOperation(value = "柱状图", notes = "柱状图")
//    @RequestMapping(value = "/findByHistogram", method = RequestMethod.GET)
//    public ResponseMessage findByHistogram() throws Exception{
//        return resourceService.findByHistogram(getCurrentUser());
//    }

}
