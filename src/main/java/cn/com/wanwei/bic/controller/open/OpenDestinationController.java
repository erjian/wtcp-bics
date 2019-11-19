package cn.com.wanwei.bic.controller.open;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.DestinationService;
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
@RequestMapping("/public/destination")
@Api(value = "C端目的地管理", tags = "C端目的地管理相关接口")
public class OpenDestinationController extends BaseController {

    @Autowired
    private DestinationService destinationService;

    @ApiOperation(value = "目的地基础信息管理分页列表", notes = "目的地基础信息管理分页列表")
    @GetMapping(value = "/getDestinationList")
    public ResponseMessage getDestinationList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return destinationService.getDestinationList(page,size,getCurrentUser(),filter);
    }

    @ApiOperation(value = "根据areaCode获取目的地详情", notes = "根据areaCode获取目的地详情")
    @ApiImplicitParam(name = "areaCode", value = "目的地编码", required = true)
    @RequestMapping(value = "getDestDetailByAreaCode/{areaCode}", method = RequestMethod.GET)
    public ResponseMessage getDestinationDetailByAreaCode(@PathVariable("areaCode") String areaCode){
        return destinationService.getDestinationDetail(areaCode,null);
    }

    @ApiOperation(value = "根据id获取目的地详情", notes = "根据id获取目的地详情")
    @ApiImplicitParam(name = "id", value = "目的地基础信息ID", required = true)
    @RequestMapping(value = "getDestDetailById/{id}", method = RequestMethod.GET)
    public ResponseMessage getDestinationDetailById(@PathVariable("id") String id){
        return destinationService.getDestinationDetail(null,id);
    }
}
