package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.ScenicService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    @ApiOperation(value = "获取景区列表", notes = "根据区域获取景区列表(ids != null时，为不包含ids的信息)")
    @GetMapping(value = "/pageNew")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页号", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10"),
            @ApiImplicitParam(name = "regionFullCode", value = "区域编码", required = true, dataType = "String")
    })
    @OperationLog(value = "wtcp-bics/获取景区列表", operate = "r", module = "景区管理")
    public ResponseMessage agritainmentsPageNew(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                HttpServletRequest request) throws Exception{
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return scenicService.scenicPageNew(page, size, filter);
    }

    @ApiOperation(value = "根据ids串获取景区列表", notes = "根据ids串获取景区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids字符串", required = true),
            @ApiImplicitParam(name = "status", value = "是否上线（1下线  9 上线）")
    })
    @GetMapping("/pageByIds")
    public ResponseMessage findPageByIds(@RequestParam String ids,
                                         @RequestParam(value = "status", required = false) String status) throws Exception{
        return scenicService.findPageIds(ids,status);
    }

}

