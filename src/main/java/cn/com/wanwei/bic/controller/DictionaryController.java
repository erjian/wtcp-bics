package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.DictionaryEntity;
import cn.com.wanwei.bic.service.DictionaryService;
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
@RequestMapping("/dictionary")
@Api(value = "字典信息管理", tags = "字典信息管理相关接口")
public class DictionaryController extends BaseController{

    @Autowired(required = false)
    private DictionaryService dictionaryService;

    @ApiOperation(value = "字典信息管理新增", notes = "字典信息管理新增")
    @ApiImplicitParam(name = "dictionaryEntity", value = "字典信息", required = true, dataType = "DictionaryEntity")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('dictionart:c')")
    @OperationLog(value = "wtcp-bics/字典信息管理新增", operate = "c", module = "字典信息管理新增")
    public ResponseMessage save(@RequestBody DictionaryEntity dictionaryEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return dictionaryService.save(dictionaryEntity,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "字典信息管理编辑", notes = "字典信息管理编辑")
    @ApiImplicitParam(name = "dictionaryEntity", value = "字典信息", required = true, dataType = "DictionaryEntity")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('dictionart:u')")
    @OperationLog(value = "wtcp-bics/字典信息管理编辑", operate = "c", module = "字典信息管理编辑")
    public ResponseMessage edit(@PathVariable("id") Long id,@RequestBody DictionaryEntity entity, BindingResult bindingResult) throws Exception{
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return dictionaryService.edit(id,entity,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "字典信息管理删除", notes = "字典信息管理删除")
    @ApiImplicitParam(name = "id", value = "字典信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('dictionart:d')")
    @OperationLog(value = "wtcp-bics/根据字典信息ID删除信息", operate = "c", module = "根据字典信息ID删除信息")
    public ResponseMessage delete(@PathVariable("id") Long id)throws Exception{
        return dictionaryService.deleteByPrimaryKey(id);
    }



}
