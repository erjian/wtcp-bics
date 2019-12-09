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
@Api(value = "资源目录", tags = "资源目录")
@SuppressWarnings("all")
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

    private static final String SCENIC_TYPE = "3001";      //景区
    private static final String HOTEL_TYPE = "3002";        // 酒店
    private static final String TRAVEL_TYPE = "3003";        // 旅行社
    private static final String AGRITAINMENT_TYPE = "3004";   // 农家乐
    private static final String FOOD_TYPE = "3005";            //  餐饮
    private static final String ENTERTAINMENT_TYPE = "3006"; // 娱乐
    private static final String SHOPPING_TYPE = "3007";     // 购物
    private static final String CAR_TYPE = "3008";         // 车辆

    @ApiOperation(value = "根据资源编码获取资源", notes = "根据资源编码获取资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "资源编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "检索条件", dataType = "String")
    })
    @GetMapping("/getDataByType")
    public ResponseMessage getDataByType(@RequestParam String type, String searchValue) throws Exception {
        switch (type) {
            case SCENIC_TYPE:
                return scenicService.findBySearchValue(searchValue);
            case HOTEL_TYPE:
                return ResponseMessage.defaultResponse().setData("暂无数据");
            case TRAVEL_TYPE:
                return travelAgentService.findBySearchValue(searchValue);
            case FOOD_TYPE:
                return peripheryService.findBySearchValue(type, searchValue);
            case SHOPPING_TYPE:
                return peripheryService.findBySearchValue(type, searchValue);
            case AGRITAINMENT_TYPE:
                return entertainmentService.findBySearchValue(type, searchValue);
            case CAR_TYPE:
                return rentalCarService.findBySearchValue(searchValue);
            case ENTERTAINMENT_TYPE:
                return ResponseMessage.defaultResponse().setData("暂无数据");
            default:
                return ResponseMessage.validFailResponse().setMsg("获取失败");
        }
    }
}
