package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.TrafficAgentEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.TrafficAgentService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/trafficAgent")
@Api(value = "交通枢纽管理", tags = "交通枢纽管理")
public class TrafficAgentController extends BaseController{

    @Autowired
    private TrafficAgentService trafficAgentService;

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "交通枢纽管理分页列表", notes = "交通枢纽管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('trafficAgent:r')")
    @OperationLog(value = "wtcp-bics/交通枢纽管理分页列表", operate = "r", module = "交通枢纽管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return trafficAgentService.findByPage(page, size, filter);
    }

    @ApiOperation(value = "查询交通枢纽信息", notes = "查询交通枢纽信息")
    @ApiImplicitParam(name = "id", value = "交通枢纽信息ID", required = true)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('trafficAgent:r')")
    @OperationLog(value = "wtcp-bics/查询交通枢纽信息", operate = "r", module = "交通枢纽管理")
    public ResponseMessage find(@PathVariable("id") String id) {
        return trafficAgentService.find(id);
    }

    @ApiOperation(value = "新增交通枢纽信息", notes = "新增交通枢纽信息")
    @ApiImplicitParam(name = "trafficAgentEntity", value = "交通枢纽实体", dataType = "TrafficAgentEntity", required = true)
    @PostMapping
    @PreAuthorize("hasAuthority('trafficAgent:c')")
    @OperationLog(value = "wtcp-bics/新增交通枢纽信息", operate = "c", module = "交通枢纽管理")
    public ResponseMessage create(@RequestBody  TrafficAgentEntity trafficAgentEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return trafficAgentService.create(trafficAgentEntity, getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "编辑交通枢纽信息", notes = "编辑交通枢纽信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "交通枢纽信息ID", required = true),
            @ApiImplicitParam(name = "trafficAgentEntity", value = "交通枢纽实体", dataType = "TrafficAgentEntity", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('trafficAgent:u')")
    @OperationLog(value = "wtcp-bics/编辑交通枢纽信息", operate = "u", module = "交通枢纽管理")
    public ResponseMessage edit(@PathVariable(value = "id") String id, @RequestBody TrafficAgentEntity trafficAgentEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return trafficAgentService.update(id, trafficAgentEntity, getCurrentUser());
    }

    @ApiOperation(value = "删除交通枢纽信息", notes = "删除交通枢纽信息")
    @ApiImplicitParam(name = "id", value = "交通枢纽信息ID", required = true)
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('trafficAgent:d')")
    @OperationLog(value = "wtcp-bics/删除交通枢纽信息", operate = "d", module = "交通枢纽管理")
    public ResponseMessage delete(@PathVariable(value = "id") String id) {
        return trafficAgentService.delete(id);
    }

    @ApiOperation(value = "权重更改", notes = "权重更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PutMapping(value = "/weight")
    @PreAuthorize("hasAuthority('trafficAgent:w')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "交通枢纽管理")
    public ResponseMessage goWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel, getCurrentUser(), TrafficAgentEntity.class);
    }

    @ApiOperation(value = "标题重名校验", notes = "标题重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "交通枢纽信息ID"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    @GetMapping(value = "/checkTitle")
    public ResponseMessage checkTitle(@RequestParam(value = "id", required = false) String id,
                                      @RequestParam(value = "title", required = false) String title) {
        return trafficAgentService.checkTitle(id, title);
    }

    @ApiOperation(value = "交通枢纽信息上下线", notes = "交通枢纽信息上下线")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体", required = true, dataType = "AuditLogEntity")
    @PutMapping(value = "/issue")
    @PreAuthorize("hasAuthority('trafficAgent:s')")
    @OperationLog(value = "wtcp-bics/交通枢纽信息上下线", operate = "u", module = "交通枢纽管理")
    public ResponseMessage issue(@RequestBody AuditLogEntity auditLogEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return trafficAgentService.auditOrIssue(auditLogEntity, getCurrentUser(), 1);
    }

    @PreAuthorize("hasAuthority('trafficAgent:g')")
    @ApiOperation(value = "数据绑定", notes = "数据绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "model", value = "数据绑定实体", required = true, dataType = "DataBindModel")
    })
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage dataBind(@RequestBody @Valid DataBindModel model, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        String updatedUser = getCurrentUser().getUsername();
        String updatedDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        trafficAgentService.dataBind(updatedUser, updatedDate, model);

        return ResponseMessage.defaultResponse().setMsg("数据绑定成功");
    }

    @ApiOperation(value = "获取交通枢纽信息", notes = "获取交通枢纽信息")
    @GetMapping(value = "/getTrafficAgentList")
    @PreAuthorize("hasAuthority('trafficAgent:r')")
    @OperationLog(value = "wtcp-bics/获取交通枢纽信息", operate = "r", module = "交通枢纽管理")
    public ResponseMessage getTrafficAgentList() {
        return trafficAgentService.getTrafficAgentList();
    }
}
