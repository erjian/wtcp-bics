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

    @ApiOperation(value = "根据关联主键查询营业信息详情", notes = "根据关联主键查询营业信息详情")
    @ApiImplicitParam(name = "principalId", value = "关联主键ID", required = true)
    @RequestMapping(value = "/{principalId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('business:v')")
    @OperationLog(value = "wtcp-bics/根据关联主键查询营业信息详情", operate = "v", module = "营业信息管理")
    public ResponseMessage findByPrincipalId(@PathVariable("principalId") String principalId) throws Exception {
        BusinessEntity entity = businessService.findByPrincipalId(principalId);
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
    public ResponseMessage insertOrUpdateById(@RequestBody BusinessEntity businessEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        if(null == businessEntity.getId() || businessEntity.getId().trim().equals("")){
            return businessService.insert(businessEntity,getCurrentUser().getUsername());
        }else{
            return businessService.updateById(businessEntity.getId().trim(),businessEntity,getCurrentUser().getUsername());
        }
    }
}

