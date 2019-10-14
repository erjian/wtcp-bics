package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.EntertainmentEntity;
import cn.com.wanwei.bic.service.EntertainmentService;
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
@RestController
@RefreshScope
@RequestMapping("/entertainment")
@Api(value = "休闲娱乐管理", tags = "休闲娱乐管理")
public class EntertainmentController extends BaseController{

    @Autowired
    private EntertainmentService entertainmentService;

    @ApiOperation(value = "休闲娱乐管理分页列表",notes = "休闲娱乐管理分页列表")
    @ApiImplicitParam(name = "type", value = "类型（1：农家乐）",required = true)
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('entertainment:r')")
    @OperationLog(value = "wtcp-bics/休闲娱乐管理分页列表", operate = "r", module = "休闲娱乐管理")
    public ResponseMessage findByPage(@RequestParam(value = "type") Integer type,
                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request){
        Map<String, Object> filter = RequestUtil.getParameters(request);
        filter.put("type",type);
        return entertainmentService.findByPage(page,size,filter);
    }

    @ApiOperation(value = "查询休闲娱乐信息", notes = "查询休闲娱乐信息")
    @ApiImplicitParam(name = "id", value = "休闲娱乐信息ID", required = true)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('entertainment:r')")
    @OperationLog(value = "wtcp-bics/查询休闲娱乐信息", operate = "r", module = "休闲娱乐管理")
    public ResponseMessage find(@PathVariable("id") String id) {
        return entertainmentService.find(id);
    }

    @ApiOperation(value = "新增休闲娱乐信息", notes = "新增休闲娱乐信息")
    @ApiImplicitParam(name = "entertainmentEntity", value = "休闲娱乐实体",dataType="EntertainmentEntity", required = true)
    @PostMapping
    @PreAuthorize("hasAuthority('entertainment:c')")
    @OperationLog(value = "wtcp-bics/新增休闲娱乐信息", operate = "c", module = "休闲娱乐管理")
    public ResponseMessage create(@RequestBody EntertainmentEntity entertainmentEntity, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return entertainmentService.create(entertainmentEntity,getCurrentUser());
    }

    @ApiOperation(value = "编辑休闲娱乐信息", notes = "编辑休闲娱乐信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "休闲娱乐信息ID", required = true),
            @ApiImplicitParam(name = "entertainmentEntity", value = "休闲娱乐实体",dataType="EntertainmentEntity", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('entertainment:u')")
    @OperationLog(value = "wtcp-bics/编辑休闲娱乐信息", operate = "u", module = "休闲娱乐管理")
    public ResponseMessage edit(@PathVariable(value = "id") String id, @RequestBody EntertainmentEntity entertainmentEntity, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return entertainmentService.update(id,entertainmentEntity,getCurrentUser());
    }

    @ApiOperation(value = "删除休闲娱乐信息", notes = "删除休闲娱乐信息")
    @ApiImplicitParam(name = "id", value = "休闲娱乐信息ID", required = true)
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('entertainment:d')")
    @OperationLog(value = "wtcp-bics/删除休闲娱乐信息", operate = "d", module = "休闲娱乐管理")
    public ResponseMessage delete(@PathVariable(value = "id") String id){
        return entertainmentService.delete(id);
    }

    @ApiOperation(value = "权重更改", notes = "权重更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "休闲娱乐信息ID", required = true),
            @ApiImplicitParam(name = "weight", value = "权重", required = true)
    })
    @GetMapping(value = "/weight/{id}")
    @PreAuthorize("hasAuthority('entertainment:q')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "休闲娱乐管理")
    public ResponseMessage goWeight(@PathVariable(value = "id") String id,@RequestParam Float weight) throws Exception {
        return entertainmentService.goWeight(id,weight,getCurrentUser());
    }

    @ApiOperation(value = "标题重名校验", notes = "标题重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "休闲娱乐信息ID"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    @GetMapping(value = "/checkTitle")
    public ResponseMessage checkTitle(@RequestParam(value = "id",required = false) String id,
                                      @RequestParam(value = "title",required = false) String title){
        return entertainmentService.checkTitle(id,title);
    }

    @ApiOperation(value = "休闲娱乐信息审核", notes = "休闲娱乐信息审核")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",required = true,dataType = "AuditLogEntity")
    @PutMapping(value = "/audit")
    public ResponseMessage audit(@RequestBody AuditLogEntity auditLogEntity , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return entertainmentService.auditOrIssue(auditLogEntity,getCurrentUser(),0);
    }

    @ApiOperation(value = "休闲娱乐信息上下线", notes = "休闲娱乐信息上下线")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",required = true,dataType = "AuditLogEntity")
    @PutMapping(value = "/issue")
    public ResponseMessage issue(@RequestBody AuditLogEntity auditLogEntity ,BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return entertainmentService.auditOrIssue(auditLogEntity,getCurrentUser(),1);
    }

}
