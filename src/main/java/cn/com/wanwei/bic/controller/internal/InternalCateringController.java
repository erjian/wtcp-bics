package cn.com.wanwei.bic.controller.internal;


import cn.com.wanwei.bic.service.CateringService;
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
 * C端相关接口
 * @author
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/catering")
@Api(value = "C端餐饮管理", tags = "C端餐饮管理相关接口")
public class InternalCateringController {

    @Autowired
    private CateringService cateringService;

    @ApiOperation(value = "C端餐饮管理详情", notes = "根据ID查询餐饮信息详情")
    @ApiImplicitParam(name = "id", value = "餐饮ID", required = true)
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseMessage findById(@PathVariable("id") String id) throws Exception {
        return cateringService.findById(id);
    }

    @ApiOperation(value = "C端获取餐饮列表", notes = "获取餐饮列表")
    @RequestMapping(value = "/findByList", method = RequestMethod.GET)
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return cateringService.findByPage(page, size, filter);
    }
}
