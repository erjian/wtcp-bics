package cn.com.wanwei.bic.controller.internal;


import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.ExhibitsService;
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

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/exhibits")
@Api(value = "C端休展品管理", tags = "C端休展品管理")
public class InternalExhibitsController extends BaseController {

    @Autowired
    private ExhibitsService exhibitsService;

    @ApiOperation(value = "查询展品相关信息", notes = "查询展品相关信息（基础信息，企业信息，通讯信息，素材信息）")
    @ApiImplicitParam(name = "id", value = "休闲娱乐信息ID", required = true)
    @GetMapping(value = "/getOne/{id}")
    public ResponseMessage findInfoById(@PathVariable("id") String id) {
        return exhibitsService.findInfoById(id);
    }

    @ApiOperation(value = "展品管理分页列表", notes = "展品管理分页列表")
    @GetMapping(value = "/page")
    @OperationLog(value = "wtcp-bics/展品管理分页列表", operate = "r", module = "展品管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return exhibitsService.findByPage(page, size, filter);
    }
}
