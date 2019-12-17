package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.service.*;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private ScenicService scenicService;

    @Autowired
    private PeripheryService peripheryService;

    @Autowired
    private TravelAgentService travelAgentService;

    @Autowired
    private RentalCarService rentalCarService;

    @Autowired
    private TrafficAgentService trafficAgentService;

    @Autowired
    private DriveCampService driveCampService;

    @Autowired
    private EntertainmentService entertainmentService;


    public ResponseMessage findByPieChart()throws Exception{
        return null;
    }

}
