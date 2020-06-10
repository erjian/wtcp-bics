package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.ExhibitsEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.ExhibitsService;
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
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/exhibits")
@Api(value = "展品管理", tags = "展品管理")
public class ExhibitsController extends BaseController{

    @Autowired
    private ExhibitsService exhibitsService;

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "展品管理分页列表", notes = "展品管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('exhibits:r')")
    @OperationLog(value = "wtcp-bics/展品管理分页列表", operate = "r", module = "展品管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return exhibitsService.findByPage(page, size, filter);
    }

    @ApiOperation(value = "查询展品信息", notes = "查询展品信息")
    @ApiImplicitParam(name = "id", value = "展品信息ID", required = true)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('exhibits:r')")
    @OperationLog(value = "wtcp-bics/查询展品信息", operate = "r", module = "展品管理")
    public ResponseMessage find(@PathVariable("id") String id) {
        return exhibitsService.find(id);
    }

    @ApiOperation(value = "新增展品信息", notes = "新增展品信息")
    @ApiImplicitParam(name = "exhibitsModel", value = "展品实体", dataType = "EntityTagsModel", required = true)
    @PostMapping
    @PreAuthorize("hasAuthority('exhibits:c')")
    @OperationLog(value = "wtcp-bics/新增展品信息", operate = "c", module = "展品管理")
    public ResponseMessage create(@RequestBody EntityTagsModel<ExhibitsEntity> exhibitsModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return exhibitsService.create(exhibitsModel, getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "编辑展品信息", notes = "编辑展品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "展品信息ID", required = true),
            @ApiImplicitParam(name = "exhibitsModel", value = "展品实体", dataType = "EntityTagsModel", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('exhibits:u')")
    @OperationLog(value = "wtcp-bics/编辑展品信息", operate = "u", module = "展品管理")
    public ResponseMessage edit(@PathVariable(value = "id") String id, @RequestBody EntityTagsModel<ExhibitsEntity> exhibitsModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return exhibitsService.update(id, exhibitsModel, getCurrentUser());
    }

    @ApiOperation(value = "删除展品信息", notes = "删除展品信息")
    @ApiImplicitParam(name = "id", value = "展品信息ID", required = true)
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('exhibits:d')")
    @OperationLog(value = "wtcp-bics/删除展品信息", operate = "d", module = "展品管理")
    public ResponseMessage delete(@PathVariable(value = "id") String id) {
        return exhibitsService.delete(id);
    }

    @ApiOperation(value = "权重更改", notes = "权重更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PutMapping(value = "/weight")
    @PreAuthorize("hasAuthority('exhibits:w')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "展品管理")
    public ResponseMessage goWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel, getCurrentUser(), ExhibitsEntity.class);
    }

    @ApiOperation(value = "标题重名校验", notes = "标题重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "展品信息ID"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    @GetMapping(value = "/checkTitle")
    public ResponseMessage checkTitle(@RequestParam(value = "id", required = false) String id,
                                      @RequestParam(value = "title", required = false) String title) {
        return exhibitsService.checkTitle(id, title);
    }

    @ApiOperation(value = "展品信息审核", notes = "展品信息审核")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体", required = true, dataType = "AuditLogEntity")
    @PutMapping(value = "/audit")
    @PreAuthorize("hasAuthority('exhibits:a')")
    @OperationLog(value = "wtcp-bics/展品信息审核", operate = "a", module = "展品管理")
    public ResponseMessage audit(@RequestBody AuditLogEntity auditLogEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return exhibitsService.auditOrIssue(auditLogEntity, getCurrentUser(), 0);
    }

    @ApiOperation(value = "展品信息上下线", notes = "展品信息上下线")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体", required = true, dataType = "AuditLogEntity")
    @PutMapping(value = "/issue")
    @PreAuthorize("hasAuthority('exhibits:s')")
    @OperationLog(value = "wtcp-bics/展品信息上下线", operate = "u", module = "展品管理")
    public ResponseMessage issue(@RequestBody AuditLogEntity auditLogEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return exhibitsService.auditOrIssue(auditLogEntity, getCurrentUser(), 1);
    }

    @PreAuthorize("hasAuthority('exhibits:g')")
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
        exhibitsService.dataBind(updatedUser, model);

        return ResponseMessage.defaultResponse().setMsg("数据绑定成功");
    }

    @ApiOperation(value = "展品信息标签关联", notes = "展品信息标签关联")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "展品信息ID", required = true)
    })
    @PutMapping(value = "relateTags/{id}")
    @PreAuthorize("hasAuthority('exhibits:u')")
    @OperationLog(value = "wtcp-bics/展品信息标签关联", operate = "u", module = "展品管理")
    public ResponseMessage relateTags(@PathVariable(value = "id") String id, @RequestBody List<BaseTagsEntity> list, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return exhibitsService.relateTags(id, list, getCurrentUser());
    }

    @ApiOperation(value = "获取展品信息", notes = "获取展品信息")
    @GetMapping(value = "/getExhibitsList")
    @PreAuthorize("hasAuthority('exhibits:r')")
    @OperationLog(value = "wtcp-bics/获取展品信息", operate = "r", module = "展品管理")
    public ResponseMessage getExhibitsList() {
        return exhibitsService.getExhibitsList();
    }
}
