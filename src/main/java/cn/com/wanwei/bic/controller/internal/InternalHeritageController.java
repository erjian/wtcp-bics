package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.entity.HeritageEntity;
import cn.com.wanwei.bic.service.HeritageService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
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
@RequestMapping("/public/heritage")
@Api(value = "C端非遗管理", tags = "C端非遗管理相关接口")
public class InternalHeritageController extends BaseController {

    @Autowired
    private HeritageService heritageService;

    @ApiOperation(value = "C端查询非遗详情", notes = "C端根据ID查询非遗详情")
    @ApiImplicitParam(name = "id", value = "非遗ID", required = true)
    @RequestMapping(value = "/findHotelInfoById/{id}", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/根据id查询非遗详情", operate = "r", module = "非遗管理")
    public ResponseMessage findHotelInfoById(@PathVariable("id") String id) throws Exception {
        return heritageService.findHeritageInfoById(id);
    }

    @ApiOperation(value = "非遗管理c端分页列表", notes = "非遗管理c端分页列表")
    @GetMapping(value = "/findByList")
    public ResponseMessage findByList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                                              HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        EscapeCharUtils.escape(filter, "title");
        PageInfo<HeritageEntity> pageInfo = heritageService.findByPageWithDeptCode(page,size,filter);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }
}

