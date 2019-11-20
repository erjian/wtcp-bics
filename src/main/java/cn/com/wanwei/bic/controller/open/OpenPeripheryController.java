package cn.com.wanwei.bic.controller.open;

import cn.com.wanwei.bic.service.PeripheryService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * C端周边管理相关接口
 * @author
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/periphery")
@Api(value = "C端周边管理", tags = "C端周边管理相关接口")
public class OpenPeripheryController {

    @Autowired
    private PeripheryService peripheryService;


    @ApiOperation(value = "根据周边管理类别查询列表", notes = "根据周边管理类别查询列表")
    @GetMapping("/findByPage")
    @ApiImplicitParam(name = "category", value = "周边类别code", dataType = "String", required = true)
    @OperationLog(value = "wtcp-bics/C端周边管理列表", operate = "r", module = "周边管理")
    public ResponseMessage getPeripheryByCategory(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                  @RequestParam(value = "category") String category,
                                                  HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        filter.put("category", category);
        return peripheryService.findByPage(page, size, filter);
    }

    @ApiOperation(value = "C端查询周边管理信息", notes = "C端查询周边管理信息")
    @ApiImplicitParam(name = "id", value = "周边管理信息Id", required = true)
    @GetMapping(value = "/{id}")
    @OperationLog(value = "wtcp-bics/C端查询周边管理信息", operate = "r", module = "周边管理")
    public ResponseMessage findById(@PathVariable("id") String id) throws Exception{
        return peripheryService.findById(id);
    }
}
