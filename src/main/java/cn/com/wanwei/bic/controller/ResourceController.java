package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.feign.AuthServiceFeign;
import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.common.log.annotation.OperationLog;
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

    @Autowired
    private AuthServiceFeign authServiceFeign;

    @ApiOperation(value = "饼状图（第一次初始化调用的接口）", notes = "饼状图（第一次初始化调用的接口）")
    @RequestMapping(value = "/findByPieChart", method = RequestMethod.POST)
    public ResponseMessage findByPieChart(@RequestBody Map<String, Object> queryModel)throws Exception{
        return resourceService.findByPieChart(getCurrentUser(), queryModel);
    }

    @ApiOperation(value = "根据资源编码Code获取饼状图的数据", notes = "根据资源编码Code获取饼状图的数据")
    @RequestMapping(value = "/initPieByCode", method = RequestMethod.POST)
    public ResponseMessage initPieByCode(@RequestBody Map<String, Object> queryParams) throws Exception{
        return resourceService.initPieByCode(getCurrentUser(), queryParams);
    }

    @ApiOperation(value = "根据资源编码Code获取柱状图的数据", notes = "根据资源编码Code获取柱状图的数据")
    @RequestMapping(value = "/initBarByCode", method = RequestMethod.POST)
    public ResponseMessage initBarByCode(@RequestBody Map<String, Object> queryParams) throws Exception{
        return resourceService.initBarByCode(getCurrentUser(), queryParams);
    }

    @ApiOperation(value = "根据所属区划编码获取子级组织机构", notes = "根据所属区划编码获取子级组织机构")
    @ApiImplicitParam(name = "areaCode", value = "所属区划编码", required = true)
    @GetMapping(value = "/findChildByAreaCode")
    @OperationLog(value = "wtcp-auth/根据行政区划编码获取子级组织机构", module = "组织机构管理")
    public ResponseMessage findChildByAreaCode(@RequestParam String areaCode) throws Exception{
        return authServiceFeign.findChildByAreaCode(areaCode);
    }

    @ApiOperation(value = "柱状图（第一次初始化调用的接口）", notes = "柱状图（第一次初始化调用的接口）")
    @RequestMapping(value = "/findByBarChart", method = RequestMethod.POST)
    public ResponseMessage findByBarChart(@RequestBody Map<String, Object> queryBarModel) throws Exception{
        return resourceService.findByBarChart(getCurrentUser(), queryBarModel);
    }

}
