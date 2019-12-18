package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = "/findByPieChart", method = RequestMethod.GET)
    public ResponseMessage findByPieChart()throws Exception{
        return resourceService.findByPieChart(getCurrentUser());
    }

    @ApiOperation(value = "柱状图", notes = "柱状图")
    @RequestMapping(value = "/findByHistogram", method = RequestMethod.GET)
    public ResponseMessage findByHistogram() throws Exception{
        return resourceService.findByHistogram(getCurrentUser());
    }

}
