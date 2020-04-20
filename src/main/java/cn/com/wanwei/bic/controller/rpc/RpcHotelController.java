package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.service.HotelService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping("/rpc/hotel")
@Api(value = "酒店管理Feign接口", tags = "酒店管理Feign接口")
public class RpcHotelController {

    @Autowired
    private HotelService hotelService;

    @ApiOperation(value = "获取酒店信息列表", notes = "获取酒店信息列表")
    @GetMapping(value = "/getHotelInfo")
    @ApiImplicitParam(name = "title", value = "酒店名称", defaultValue = "")
    @OperationLog(value = "wtcp-bics/获取酒店信息列表", operate = "r", module = "酒店管理")
    public ResponseMessage getHotelInfo(@RequestParam(value = "title", defaultValue = "",required = false) String title) {
        return hotelService.getHotelInfo(title);
    }
}
