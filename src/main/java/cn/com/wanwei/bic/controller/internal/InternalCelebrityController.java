package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.service.CelebrityService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * C端名人管理接口
 * @author
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/celebrity")
@Api(value = "C端名人管理", tags = "C端名人管理相关接口")
public class InternalCelebrityController {

    @Autowired
    private CelebrityService celebrityService;

    @ApiOperation(value = "C端名人管理详情", notes = "根据ID查询名人详情")
    @ApiImplicitParam(name = "id", value = "名人ID", required = true)
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseMessage findById(@PathVariable("id") String id) throws Exception {
        return celebrityService.findById(id);
    }

    @ApiOperation(value = "获取名人列表", notes = "获取名人列表")
    @PreAuthorize("hasAuthority('celebrity:r')")
    @RequestMapping(value = "/findByList", method = RequestMethod.GET)
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return celebrityService.findByPage(page, size, filter);
    }
}
