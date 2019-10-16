package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.ContactEntity;
import cn.com.wanwei.bic.service.ContactService;
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
@RequestMapping("/contact")
@Api(value = "通讯信息管理", tags = "通讯信息管理相关接口")
public class ContactController extends BaseController {

    @Autowired
    private ContactService contactService;

    @ApiOperation(value = "根据关联主键查询通讯信息详情", notes = "根据关联主键ID查询通讯信息详情")
    @ApiImplicitParam(name = "principalId", value = "关联主键ID", required = true)
    @RequestMapping(value = "/{principalId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('contact:v')")
    @OperationLog(value = "wtcp-bics/根据关联主键查询通讯信息详情", operate = "v", module = "通讯信息管理")
    public ResponseMessage detail(@PathVariable("principalId") String principalId) throws Exception {
        ContactEntity entity = contactService.selectByPrincipalId(principalId);
        if (entity == null) {
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(entity);
    }

    @ApiOperation(value = "通讯信息新增", notes = "通讯信息新增")
    @ApiImplicitParam(name = "contactEntity", value = "通讯信息", required = true, dataType = "ContactEntity")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('contact:c')")
    @OperationLog(value = "wtcp-bics/通讯信息新增", operate = "c", module = "通讯信息管理")
    public ResponseMessage save(@RequestBody ContactEntity contactEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        if(null == contactEntity.getId() || contactEntity.getId().trim().equals("")){
            return contactService.save(contactEntity,getCurrentUser().getUsername());
        }else{
            return contactService.edit(contactEntity.getId().trim(),contactEntity,getCurrentUser().getUsername());
        }
    }
}

