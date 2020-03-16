package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.entity.VenueEntity;
import cn.com.wanwei.bic.service.VenueService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
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
@RequestMapping("/public/venue")
@SuppressWarnings("all")
@Api(value = "C端场馆管理", tags = "C端场馆管理相关接口")
public class InternalVenueController {

    @Autowired
    private VenueService venueService;

    @ApiOperation(value = "C端场馆管理分页列表", notes = "C端场馆管理分页列表")
    @GetMapping(value = "/page")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return venueService.findByPage(page,size,filter);
    }

    @ApiOperation(value = "C端查询场馆详情", notes = "C端根据ID查询场馆详情")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseMessage findById(@PathVariable("id") String id) throws Exception {
        return venueService.findById(id);
    }

}
