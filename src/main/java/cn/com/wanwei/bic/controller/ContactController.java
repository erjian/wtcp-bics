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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/contact")
@Api(value = "通讯信息管理", tags = "通讯信息管理相关接口")
public class ContactController extends BaseController {

    @Autowired
    private ContactService contactService;

    @ApiOperation(value = "查询通讯信息详情", notes = "根据ID查询通讯信息详情")
    @ApiImplicitParam(name = "id", value = "通讯信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('contact:v')")
    @OperationLog(value = "wtcp-bics/根据id查询通讯信息详情", operate = "v", module = "通讯信息管理")
    public ResponseMessage detail(@PathVariable("id") Long id) throws Exception {
        ContactEntity entity = contactService.selectByPrimaryKey(id);
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
        return contactService.save(contactEntity,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "通讯信息编辑", notes = "通讯信息编辑")
    @ApiImplicitParam(name = "contactEntity", value = "通讯信息", required = true, dataType = "ContactEntity")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('contact:u')")
    @OperationLog(value = "wtcp-bics/通讯信息编辑", operate = "u", module = "通讯信息管理")
    public ResponseMessage edit(@PathVariable("id") Long id, @RequestBody ContactEntity contactEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return contactService.edit(id,contactEntity,getCurrentUser().getUsername());
    }
}

