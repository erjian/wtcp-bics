package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.service.EntertainmentService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/rpc/entertainment")
@Api(value = "feign休闲娱乐接口", tags = "feign休闲娱乐相关接口")
public class RpcEntertainmentController {

    @Autowired
    private EntertainmentService entertainmentService;


    @ApiOperation(value = "获取农家乐列表", notes = "根据区域获取农家乐列表（ids != null时，为不包含ids的信息）")
    @GetMapping(value = "/pageNew")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页号", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10"),
    })
    @OperationLog(value = "wtcp-bics/获取农家乐列表", operate = "r", module = "休闲娱乐管理")
    public ResponseMessage agritainmentsPageNew(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return entertainmentService.agritainmentsPageNew(page, size, filter);
    }

    @ApiOperation(value = "根据ids串获取农家乐分页列表", notes = "根据ids串获取农家乐分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids字符串", required = true),
            @ApiImplicitParam(name = "status", value = "上下线")
    })
    @GetMapping("/pageByIds")
    public ResponseMessage findPageByIds(@RequestParam String ids,
                                         @RequestParam(value = "status", required = false) String status) throws Exception {
        return entertainmentService.findPageIds(ids, status);
    }

}
