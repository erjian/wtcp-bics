package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/scenic")
@Api(value = "C端景区管理", tags = "C端景区管理相关接口")
public class InternalScenicController extends BaseController {

    @Autowired
    private ScenicService scenicService;

    @ApiOperation(value = "C端查询景区详情", notes = "C端根据ID查询景区详情")
    @ApiImplicitParam(name = "id", value = "景区ID", required = true)
    @RequestMapping(value = "/getOne/{id}", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/根据id查询景区详情", operate = "r", module = "景区管理")
    public ResponseMessage getOne(@PathVariable("id") String id) throws Exception {
        return scenicService.getOne(id);
    }
}

