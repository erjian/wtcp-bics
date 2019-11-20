package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/rpc/scenic")
@Api(value = "景区管理feign接口", tags = "景区管理相关feign接口")
public class RpcScenicController extends BaseController {

    @Autowired
    private ScenicService scenicService;

    @ApiOperation(value = "查询景区信息的feign接口", notes = "查询景区信息的feign接口")
    @RequestMapping(value = "/getScenicInfo", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/查询景区信息的feign接口", operate = "v", module = "景区管理")
    public ResponseMessage getScenicInfo(String title) throws Exception {
        title = title == null ? "" : title;
        return scenicService.getScenicInfo(title.trim().toLowerCase());
    }
}

