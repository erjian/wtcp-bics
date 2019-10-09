package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.EnterpriseEntity;
import cn.com.wanwei.bic.service.EnterpriseService;
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
@RequestMapping("/enterprise")
@Api(value = "企业信息管理", tags = "企业信息管理相关接口")
public class EnterpriseController extends BaseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @ApiOperation(value = "查询企业信息详情", notes = "根据ID查询企业信息详情")
    @ApiImplicitParam(name = "id", value = "企业信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('enterprise:v')")
    @OperationLog(value = "wtcp-bics/根据id查询企业信息详情", operate = "v", module = "企业信息管理")
    public ResponseMessage detail(@PathVariable("id") Long id) throws Exception {
        EnterpriseEntity entity = enterpriseService.selectByPrimaryKey(id);
        if (entity == null) {
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(entity);
    }

    @ApiOperation(value = "企业信息新增", notes = "企业信息新增")
    @ApiImplicitParam(name = "enterpriseEntity", value = "企业信息", required = true, dataType = "EnterpriseEntity")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('enterprise:c')")
    @OperationLog(value = "wtcp-bics/企业信息新增", operate = "c", module = "企业信息管理")
    public ResponseMessage save(@RequestBody EnterpriseEntity enterpriseEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return enterpriseService.save(enterpriseEntity,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "企业信息编辑", notes = "企业信息编辑")
    @ApiImplicitParam(name = "enterpriseEntity", value = "企业信息", required = true, dataType = "EnterpriseEntity")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('enterprise:u')")
    @OperationLog(value = "wtcp-bics/企业信息编辑", operate = "u", module = "企业信息管理")
    public ResponseMessage edit(@PathVariable("id") Long id, @RequestBody EnterpriseEntity enterpriseEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return enterpriseService.edit(id,enterpriseEntity,getCurrentUser().getUsername());
    }
}

