package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.model.GouldModel;
import cn.com.wanwei.bic.service.TrafficAgentService;
import cn.com.wanwei.common.model.ResponseMessage;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/search")
@Api(value = "C端高德搜索数据拉取", tags = "C端高德搜索数据拉取")
public class InternalSearchController extends BaseController {

    @Autowired
    private TrafficAgentService trafficAgentService;

    @ApiOperation(value = "高德搜索数据拉取", notes = "高德搜索数据拉取")
    @ApiImplicitParam(name = "GouldModel", value = "数据模型",required = true, dataType = "GouldModel")
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public ResponseMessage getOne(@RequestBody GouldModel gouldModel) throws Exception {
        //保存高德交通枢纽搜索数据
        if(!Strings.isNullOrEmpty(gouldModel.getType())){
            return trafficAgentService.saveGouldTrafficData(gouldModel, getCurrentUser(), ruleId, appCode);
        }
        return ResponseMessage.validFailResponse().setMsg("未指定搜索类型！！！");
    }
}

