package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.PoiService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
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
import java.util.List;
import java.util.Map;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/poi")
@Api(value = "poi管理", tags = "poi管理相关接口")
public class PoiController extends BaseController {

    @Autowired
    private PoiService poiService;

    @ApiOperation(value = "poi管理分页列表", notes = "poi管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('poi:r')")
    @OperationLog(value = "wtcp-bics/poi管理分页列表", operate = "r", module = "poi管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return poiService.findByPage(page, size, filter);
    }

    @ApiOperation(value = "查询poi信息", notes = "根据ID查询poi信息")
    @ApiImplicitParam(name = "id", value = "poi信息ID", required = true)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('poi:r')")
    @OperationLog(value = "wtcp-bics/查询poi信息", operate = "r", module = "poi管理")
    public ResponseMessage find(@PathVariable("id") String id) {
        return poiService.find(id);
    }

    @ApiOperation(value = "新增poi信息", notes = "新增poi信息")
    @ApiImplicitParam(name = "poiModel", value = "poi管理model", dataType = "EntityTagsModel", required = true)
    @PostMapping
    @PreAuthorize("hasAuthority('poi:c')")
    @OperationLog(value = "wtcp-bics/新增poi信息", operate = "c", module = "poi管理")
    public ResponseMessage create(@RequestBody EntityTagsModel<PoiEntity> poiModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return poiService.create(poiModel, getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "编辑poi信息", notes = "编辑poi信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "poi信息ID", required = true),
            @ApiImplicitParam(name = "poiModel", value = "poi管理model", dataType = "EntityTagsModel", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('poi:u')")
    @OperationLog(value = "wtcp-bics/编辑poi信息", operate = "u", module = "poi管理")
    public ResponseMessage edit(@PathVariable(value = "id") String id, @RequestBody EntityTagsModel<PoiEntity> poiModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return poiService.update(id, poiModel, getCurrentUser());
    }

    @ApiOperation(value = "删除poi信息", notes = "删除poi信息")
    @ApiImplicitParam(name = "id", value = "poi信息ID", required = true)
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('poi:d')")
    @OperationLog(value = "wtcp-bics/删除poi信息", operate = "d", module = "poi管理")
    public ResponseMessage delete(@PathVariable(value = "id") String id) {
        return poiService.delete(id);
    }

    @ApiOperation(value = "权重更改", notes = "权重更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "poi信息ID", required = true),
            @ApiImplicitParam(name = "weight", value = "权重", required = true)
    })
    @GetMapping(value = "/weight/{id}")
    @PreAuthorize("hasAuthority('poi:q')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "poi管理")
    public ResponseMessage goWeight(@PathVariable(value = "id") String id, @RequestParam Integer weight) throws Exception {
        return poiService.goWeight(id, weight, getCurrentUser());
    }

    @ApiOperation(value = "标题重名校验", notes = "标题重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "poi信息ID"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    @GetMapping(value = "/checkTitle")
    public ResponseMessage checkTitle(@RequestParam(value = "id", required = false) String id,
                                      @RequestParam(value = "title", required = false) String title) {
        return poiService.checkTitle(id, title);
    }

    @ApiOperation(value = "poi信息审核", notes = "poi信息审核")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体", required = true, dataType = "AuditLogEntity")
    @PutMapping(value = "/audit")
    public ResponseMessage audit(@RequestBody AuditLogEntity auditLogEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return poiService.auditOrIssue(auditLogEntity, getCurrentUser(), 0);
    }

    @ApiOperation(value = "poi信息上下线", notes = "poi信息上下线")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体", required = true, dataType = "AuditLogEntity")
    @PutMapping(value = "/issue")
    public ResponseMessage issue(@RequestBody AuditLogEntity auditLogEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return poiService.auditOrIssue(auditLogEntity, getCurrentUser(), 1);
    }

    @ApiOperation(value = "查询景点信息列表", notes = "查询景点信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型code"),
            @ApiImplicitParam(name = "principalId", value = "景区id"),
            @ApiImplicitParam(name = "id", value = "poiID"),
    })
    @GetMapping("/findScenicList")
    public ResponseMessage findScenicList(@RequestParam String type,@RequestParam String principalId,@RequestParam String id) {
        return poiService.findScenicList(type,principalId,id);
    }


    @ApiOperation(value = "批量删除poi管理信息", notes = "根据ID批量删除poi管理信息")
    @RequestMapping(value = "/batchDelete", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('poi:bd')")
    public ResponseMessage batchDelete(@RequestBody List<String> ids, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return poiService.batchDelete(ids);
    }


    @ApiOperation(value = "POI管理信息关联标签", notes = "POI管理信息关联标签")
    @ApiImplicitParam(name = "tags", value = "关联标签", required = true, dataType = "Map")
    @RequestMapping(value = "/relateTags", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('poi:rt')")
    public ResponseMessage relateTags(@RequestBody Map<String, Object> tags, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return poiService.relateTags(tags, getCurrentUser());
    }

    @PreAuthorize("hasAuthority('poi:g')")
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
        poiService.dataBindById(updatedUser, model);

        return ResponseMessage.defaultResponse().setMsg("数据绑定成功");
    }

}
