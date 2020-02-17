package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.service.RentalCarService;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

/**
 * wtcp-bics/rentalCar  租车管理C端接口
 * @authorEric——527
 * @date
 */
@RestController
@RefreshScope
@RequestMapping("/public/rentalCar")
@Api(value = "C端租车管理", tags = "C端租车管理相关接口")
public class InternalRentalCarController {

    @Autowired
    private RentalCarService rentalCarService;

    @ApiOperation(value = "查询租车相关信息", notes = "查询租车相关信息（租车信息，企业信息，营业信息，通讯信息，素材信息）")
    @ApiImplicitParam(name = "id", value = "租车信息ID", required = true)
    @GetMapping(value = "/getRentalInfo/{id}")
    public ResponseMessage getRentalInfo(@PathVariable("id") String id) {
        return rentalCarService.getRentalInfo(id);
    }

}
