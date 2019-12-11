package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.DriveCampService;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/driveCamp")
@Api(value = "C端自驾营地管理", tags = "C端自驾营地管理相关接口")
public class OpenDriveCampController extends BaseController {

    @Autowired
    private DriveCampService  driveCampService;

    @ApiOperation(value = "查询自驾营地相关信息", notes = "查询自驾营地相关信息（基础信息，企业信息，通讯信息，素材信息）")
    @ApiImplicitParam(name = "id", value = "自驾营地信息ID", required = true)
    @GetMapping(value = "/getOne/{id}")
    public ResponseMessage getEnterInfo(@PathVariable("id") String id) {
        return driveCampService.getDriveCampInfo(id);
    }
}
