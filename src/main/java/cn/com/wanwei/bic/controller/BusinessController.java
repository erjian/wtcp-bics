package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.BusinessEntity;
import cn.com.wanwei.bic.service.BusinessService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/business")
@Api(value = "营业信息管理", tags = "营业信息管理相关接口")
public class BusinessController extends BaseController {

    @Autowired
    private BusinessService businessService;

    @ApiOperation(value = "查询营业信息详情", notes = "根据ID查询营业信息详情")
    @ApiImplicitParam(name = "id", value = "营业信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('business:v')")
    @OperationLog(value = "wtcp-bics/根据id查询营业信息详情", operate = "v", module = "营业信息管理")
    public ResponseMessage detail(@PathVariable("id") String id) throws Exception {
        BusinessEntity entity = businessService.selectByPrimaryKey(id);
        if (entity == null) {
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(entity);
    }

    @ApiOperation(value = "营业信息新增", notes = "营业信息新增")
    @ApiImplicitParam(name = "businessEntity", value = "营业信息", required = true, dataType = "BusinessEntity")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('business:c')")
    @OperationLog(value = "wtcp-bics/营业信息新增", operate = "c", module = "营业信息管理")
    public ResponseMessage save(@RequestBody BusinessEntity businessEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return businessService.save(businessEntity,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "营业信息编辑", notes = "营业信息编辑")
    @ApiImplicitParam(name = "businessEntity", value = "营业信息", required = true, dataType = "BusinessEntity")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('business:u')")
    @OperationLog(value = "wtcp-bics/营业信息编辑", operate = "u", module = "营业信息管理")
    public ResponseMessage edit(@PathVariable("id") String id, @RequestBody BusinessEntity businessEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return businessService.edit(id,businessEntity,getCurrentUser().getUsername());
    }
}

