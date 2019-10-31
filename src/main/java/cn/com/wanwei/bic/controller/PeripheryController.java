package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.PeripheryEntity;
import cn.com.wanwei.bic.service.PeripheryService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/periphery")
@Api(value = "周边管理", tags = "周边管理相关接口")
public class PeripheryController extends BaseController {
    @Autowired
    private PeripheryService peripheryService;

    @ApiOperation(value = "周边管理分页列表",notes = "周边管理分页列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('perip:r')")
    @OperationLog(value = "wtcp-bics/周边管理分页列表", operate = "r", module = "周边管理")
    public ResponseMessage findPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                    HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return peripheryService.findByPage(page,size,filter);
    }

    @ApiOperation(value = "查询周边管理信息", notes = "查询周边管理信息")
    @ApiImplicitParam(name = "id", value = "周边管理信息Id", required = true)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('perip:r')")
    @OperationLog(value = "wtcp-bics/查询周边管理信息", operate = "r", module = "周边管理")
    public ResponseMessage find(@PathVariable("id") String id) {
        return peripheryService.find(id);
    }


    @ApiOperation(value = "新增周边信息", notes = "新增周边信息")
    @ApiImplicitParam(name = "peripheryEntity", value = "周边信息实体",dataType="PeripheryEntity", required = true)
    @PostMapping
//    @PreAuthorize("hasAuthority('perip:c')")
    @OperationLog(value = "wtcp-bics/新增周边信息", operate = "c", module = "周边管理")
    public ResponseMessage create(@RequestBody PeripheryEntity peripheryEntity, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return peripheryService.save(peripheryEntity,getCurrentUser(),ruleId,appCode);
    }


    @ApiOperation(value = "编辑周边信息", notes = "编辑周边信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "周边信息ID", required = true),
            @ApiImplicitParam(name = "peripheryEntity", value = "周边信息实体",dataType="PeripheryEntity", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('perip:u')")
    @OperationLog(value = "wtcp-bics/编辑周边信息", operate = "u", module = "周边管理")
    public ResponseMessage edit(@PathVariable(value = "id") String id, @RequestBody PeripheryEntity peripheryEntity, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return peripheryService.edit(id,peripheryEntity,getCurrentUser());
    }


    @ApiOperation(value = "删除周边信息", notes = "删除周边信息")
    @ApiImplicitParam(name = "id", value = "周边信息ID", required = true)
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('perip:d')")
    @OperationLog(value = "wtcp-bics/删除周边信息", operate = "d", module = "周边管理")
    public ResponseMessage delete(@PathVariable(value = "id") String id){
        return peripheryService.delete(id);
    }

    @ApiOperation(value = "周边信息审核", notes = "周边信息审核")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",required = true,dataType = "AuditLogEntity")
    @PutMapping(value = "/audit")
    public ResponseMessage audit(@RequestBody AuditLogEntity auditLogEntity , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return peripheryService.auditOrIssue(auditLogEntity,getCurrentUser(),0);
    }

    @ApiOperation(value = "周边信息上下线", notes = "周边信息上下线")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",required = true,dataType = "AuditLogEntity")
    @PutMapping(value = "/issue")
    public ResponseMessage issue(@RequestBody AuditLogEntity auditLogEntity ,BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return peripheryService.auditOrIssue(auditLogEntity,getCurrentUser(),1);
    }

    @ApiOperation(value = "标题重名校验", notes = "标题重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "周边信息ID"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    @GetMapping(value = "/checkTitle")
    public ResponseMessage checkTitle(@RequestParam(value = "id",required = false) String id,
                                      @RequestParam(value = "title",required = false) String title){
        return peripheryService.checkTitle(id,title);
    }

}
