package cn.com.wanwei.bic.controller;


import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@RestController
@RefreshScope
@RequestMapping("/audit")
@Api(value = "审核记录管理", tags = "审核记录管理")
public class AuditLogController extends BaseController {

    @Autowired
    private AuditLogService auditLogService;

    @ApiOperation(value = "审核记录管理分页列表",notes = "审核记录管理分页列表（根据关联信息principalId获取）")
    @ApiImplicitParam(name = "principalId", value = "关联信息ID", required = true)
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('audit:r')")
    @OperationLog(value = "wtcp-bics/审核记录管理分页列表", operate = "r", module = "审核记录管理")
    public ResponseMessage findByPage(@RequestParam(value = "principalId") String principalId,
                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request){
        Map<String, Object> filter = RequestUtil.getParameters(request);
        filter.put("principalId",principalId);
        return auditLogService.findByPage(page,size,filter);
    }

    @ApiOperation(value = "审核记录新增",notes = "审核记录新增")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",dataType = "AuditLogEntity", required = true)
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('audit:c')")
    @OperationLog(value = "wtcp-bics/审核记录新增", operate = "r", module = "审核记录管理")
    public ResponseMessage create(@RequestBody AuditLogEntity auditLogEntity, BindingResult bindingResult) throws Exception {
       if(bindingResult.hasErrors()){
           return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
       }
        return auditLogService.create(auditLogEntity,getCurrentUser());
    }
}
