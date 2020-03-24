package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.model.CustomFormModel;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.form.service.DynamicFormExtendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/customForm")
@Api(value = "定制表单接口", tags = "定制表单接口")
public class BaseCustomFormController extends BaseController {

    @Autowired
    private DynamicFormExtendService dynamicFormExtendService;

    @ApiOperation(value = "保存表单数据", notes = "保存表单数据")
    @ApiImplicitParam(name = "customFormModel", value = "定制表单数据", required = true, dataType = "CustomFormModel")
    @OperationLog(value = "wtcp-bics/保存表单数据", module = "保存表单数据")
    @PostMapping("/saveOrUpdate")
    public ResponseMessage saveOrUpdate(@RequestBody @Valid CustomFormModel customFormModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return dynamicFormExtendService.saveOrUpdate(customFormModel.getForm(), customFormModel.getPrincipalId());
    }

    @ApiOperation(value = "根据关联主键获取数据", notes = "根据关联主键获取数据")
    @ApiImplicitParam(name = "principalId", value = "关联主键", required = true, dataType = "String")
    @OperationLog(value = "wtcp-bics/根据关联主键获取数据", module = "根据关联主键获取数据")
    @GetMapping("/find")
    public ResponseMessage find(@RequestParam String principalId) throws Exception {
        return dynamicFormExtendService.findByPrincipalId(principalId);
    }

    @ApiOperation(value = "根据关联主键删除表单", notes = "根据关联主键删除表单")
    @ApiImplicitParam(name = "principalId", value = "关联信息ID", required = true)
    @RequestMapping(value = "/{principalId}", method = RequestMethod.DELETE)
    @OperationLog(value = "wtcp-bics/根据关联主键删除表单", operate = "d", module = "根据关联主键删除表单")
    public ResponseMessage delete(@PathVariable("principalId") String principalId) throws Exception {
        return dynamicFormExtendService.deleteByPrincipalId(principalId);
    }
}
