package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.service.PoiService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping("/rpc/poi")
@Api(value = "POI管理Feign接口", tags = "POI管理Feign接口")
public class RpcPoiController {

    @Autowired
    private PoiService poiService;

    @ApiOperation(value = "获取POI分页列表", notes = "获取POI分页列表")
    @GetMapping(value = "/page")
    @OperationLog(value = "wtcp-bics/获取POI分页列表", operate = "r", module = "poi管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        String tokenKey = "access_token";
        if (filter.containsKey(tokenKey) && StringUtils.isNotEmpty(tokenKey)) {
            request.setAttribute(tokenKey, filter.get(tokenKey));
        }
        // 只返回上线的数据
        filter.put("status", 9);
        return poiService.findByPage(page, size, filter);
    }

    @ApiOperation(value = "根据ID获取POI详情", notes = "根据ID获取POI详情")
    @ApiImplicitParam(name = "id", value = "POI主键", required = true, dataType = "String")
    @GetMapping(value = "/{id}")
    public ResponseMessage getOne(@PathVariable("id") String id) {
        return poiService.getOne(id);
    }

}
