package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.common.model.ResponseMessage;
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

    private static final String SCENIC_TYPE = "125001001";        //普通景区
    private static final String TOUR_VILLAGE_TYPE = "125001002";  //旅游示范村
    private static final String TRAVEL_TYPE = "125003001";        // 旅行社
    private static final String AGRITAINMENT_TYPE = "125005002";  // 农家乐
    private static final String FOOD_TYPE = "125006002";          //  餐饮
    private static final String DRIVE_CAMP_TYPE = "125005001";    // 自驾营地
    private static final String SHOPPING_TYPE = "125006004";      // 购物
    private static final String RENTAL_CAR_TYPE = "125004001";    // 租车
    private static final String TRAFFIC_AGENT_TYPE = "125004002"; // 交通枢纽

    @ApiOperation(value = "根据资源编码获取资源", notes = "根据资源编码获取资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "资源编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "资源名称", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "用逗号分隔的主键字符串", dataType = "String"),
    })
    @GetMapping("/getDataByType")
    public ResponseMessage getDataByType(@RequestParam String type, String name, String ids) throws Exception {
        ResponseMessage responseMessage;
        switch (type) {
            case SCENIC_TYPE:
            case TOUR_VILLAGE_TYPE:
                responseMessage = scenicService.findBySearchValue(type,name,ids); //旅游示范村
                break;
            case TRAVEL_TYPE:
                responseMessage =  travelAgentService.findBySearchValue(name,ids);  // 旅行社
                break;
            case FOOD_TYPE:
            case SHOPPING_TYPE:
                responseMessage =  peripheryService.findBySearchValue(type, name,ids);  // 购物
                break;
            case AGRITAINMENT_TYPE:
                responseMessage =  entertainmentService.findBySearchValue(type, name,ids);  // 农家乐
                break;
            case RENTAL_CAR_TYPE:
                responseMessage =  rentalCarService.findBySearchValue(name,ids);  // 租车
                break;
            case TRAFFIC_AGENT_TYPE:
                responseMessage =  trafficAgentService.findBySearchValue(name,ids);  // 交通枢纽
                break;
            case DRIVE_CAMP_TYPE:
                responseMessage =  driveCampService.findBySearchValue(name,ids);  // 自驾营地
                break;
            default:
                responseMessage =  ResponseMessage.validFailResponse().setMsg("获取失败");
                break;
        }
        return responseMessage;
    }
}
