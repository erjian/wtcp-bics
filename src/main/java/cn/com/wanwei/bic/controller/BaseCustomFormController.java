package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.model.CustomFormModel;
import cn.com.wanwei.bic.service.DynamicFormService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/customForm")
@Api(value = "定制表单接口", tags = "定制表单接口")
public class BaseCustomFormController extends BaseController {

    @Autowired
    private DynamicFormService dynamicFormService;

    @ApiOperation(value = "保存表单数据", notes = "保存表单数据")
    @ApiImplicitParam(name = "customFormModel", value = "定制表单数据", required = true, dataType = "CustomFormModel")
    @OperationLog(value = "wtcp-bics/保存表单数据", module = "保存表单数据")
//    @PreAuthorize("hasAuthority('commonInfo:be') or hasAuthority('commonInfo:bo')")
    @PostMapping("/save")
    public ResponseMessage save(@RequestBody @Valid CustomFormModel customFormModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        dynamicFormService.deleteByPrincipalId(customFormModel.getPrincipalId());
        int num = dynamicFormService.batchInsert(customFormModel,getCurrentUser().getUsername());
        return responseMessage.setMsg("保存成功");
    }

    @ApiOperation(value = "根据关联主键获取数据", notes = "根据关联主键获取数据")
    @ApiImplicitParam(name = "principalId", value = "关联主键", required = true, dataType = "String")
    @OperationLog(value = "wtcp-bics/根据关联主键获取数据", module = "根据关联主键获取数据")
    @GetMapping("/find")
    public ResponseMessage find(@RequestParam String principalId) throws Exception {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        Map<String, Object> data = dynamicFormService.findByPrincipalId(principalId);
        return responseMessage.setData(data);
    }

    @ApiOperation(value = "根据关联主键获取数据", notes = "根据关联主键获取数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "principalId", value = "关联主键", required = true),
            @ApiImplicitParam(name = "field", value = "字段名", required = true)
    })
    @OperationLog(value = "wtcp-bics/根据关联主键获取数据", module = "根据关联主键获取数据")
    @GetMapping("/findValue")
    public ResponseMessage findValue(@RequestParam String principalId, @RequestParam String field) throws Exception {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        String val = dynamicFormService.findByPidAndField(principalId,field);
        return responseMessage.setData(val);
    }
}
