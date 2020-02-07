package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.model.DataType;
import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.common.model.ResponseMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * wtcp-bics/catalogues
 *
 * @author
 */

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/rpc/catalogues")
@Api(value = "各类型资源信息Feign接口", tags = "各类型资源信息Feign接口")
public class RpcCataloguesController {

    @Autowired
    private ScenicService scenicService;

    @Autowired
    private EntertainmentService entertainmentService;

    @Autowired
    private TravelAgentService travelAgentService;

    @Autowired
    private RentalCarService rentalCarService;

    @Autowired
    private PeripheryService peripheryService;

    @Autowired
    private DriveCampService driveCampService;

    @Autowired
    private TrafficAgentService trafficAgentService;

    @ApiOperation(value = "根据资源编码获取资源", notes = "根据资源编码获取资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "资源编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "资源名称", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "用逗号分隔的主键字符串", dataType = "String"),
    })
    @GetMapping("/getDataByType")
    public ResponseMessage getDataByType(@RequestParam String type, String name, String ids) throws Exception {
        ResponseMessage responseMessage;
        if(type.equals(DataType.SCENIC_TYPE.getKey()) || type.equals(DataType.TOUR_VILLAGE_TYPE.getKey())){
            responseMessage = scenicService.findBySearchValue(type,name,ids);
        }else if(type.equals(DataType.TRAVEL_TYPE.getKey())){
            responseMessage =  travelAgentService.findBySearchValue(name,ids);
        }else if(type.equals(DataType.FOOD_TYPE.getKey()) || type.equals(DataType.SHOPPING_TYPE.getKey())){
            responseMessage =  peripheryService.findBySearchValue(type, name,ids);
        }else if(type.equals(DataType.AGRITAINMENT_TYPE.getKey())){
            responseMessage =  entertainmentService.findBySearchValue(type, name,ids);
        }else if(type.equals(DataType.RENTAL_CAR_TYPE.getKey())){
            responseMessage =  rentalCarService.findBySearchValue(name,ids);
        }else if(type.equals(DataType.TRAFFIC_AGENT_TYPE.getKey())){
            responseMessage =  trafficAgentService.findBySearchValue(name,ids);
        }else if(type.equals(DataType.DRIVE_CAMP_TYPE.getKey())){
            responseMessage =  driveCampService.findBySearchValue(name,ids);
        }else {
            responseMessage =  ResponseMessage.validFailResponse().setMsg("获取失败");
        }
        return responseMessage;
    }

    @ApiOperation(value = "获取资源类型", notes = "获取资源类型")
    @GetMapping("/getDataType")
    public ResponseMessage getDataType(){
        return ResponseMessage.defaultResponse().setData(DataType.list());
    }
}
