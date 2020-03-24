package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.HotelService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/hotel")
@Api(value = "C端酒店管理", tags = "C端酒店管理相关接口")
public class InternalHotelController extends BaseController {

    @Autowired
    private HotelService hotelService;

    @ApiOperation(value = "C端查询酒店详情", notes = "C端根据ID查询酒店详情")
    @ApiImplicitParam(name = "id", value = "酒店ID", required = true)
    @RequestMapping(value = "/findHotelInfoById/{id}", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/根据id查询酒店详情", operate = "r", module = "酒店管理")
    public ResponseMessage findHotelInfoById(@PathVariable("id") String id) throws Exception {
        return hotelService.findHotelInfoById(id);
    }
}

