package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.service.TrafficAgentService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/traffic")
@Api(value = "C端交通枢纽相关接口", tags = "C端交通枢纽相关接口")
public class InternalTrafficController {

    @Autowired
    private TrafficAgentService trafficAgentService;

    @ApiOperation(value = "交通枢纽管理分页列表", notes = "交通枢纽管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('trafficAgent:r')")
    @OperationLog(value = "wtcp-bics/交通枢纽管理分页列表", operate = "r", module = "交通枢纽管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return trafficAgentService.findByPageToC(page, size, filter);
    }
}
