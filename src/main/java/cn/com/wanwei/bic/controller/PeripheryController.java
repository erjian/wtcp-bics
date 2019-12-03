package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.PeripheryEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.PeripheryService;
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
@RequestMapping("/periphery")
@Api(value = "周边管理", tags = "周边管理相关接口")
public class PeripheryController extends BaseController {
    @Autowired
    private PeripheryService peripheryService;

    @Autowired
    private CommonService commonService;

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
    @ApiImplicitParam(name = "peripheryModel", value = "周边信息model",dataType="EntityTagsModel", required = true)
    @PostMapping
    @PreAuthorize("hasAuthority('perip:c')")
    @OperationLog(value = "wtcp-bics/新增周边信息", operate = "c", module = "周边管理")
    public ResponseMessage create(@RequestBody EntityTagsModel<PeripheryEntity> peripheryModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return peripheryService.save(peripheryModel,getCurrentUser(),ruleId,appCode);
    }


    @ApiOperation(value = "编辑周边信息", notes = "编辑周边信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "周边信息ID", required = true),
            @ApiImplicitParam(name = "peripheryModel", value = "周边信息model",dataType="EntityTagsModel", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('perip:u')")
    @OperationLog(value = "wtcp-bics/编辑周边信息", operate = "u", module = "周边管理")
    public ResponseMessage edit(@PathVariable(value = "id") String id, @RequestBody EntityTagsModel<PeripheryEntity> peripheryModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return peripheryService.edit(id,peripheryModel,getCurrentUser());
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


    @ApiOperation(value = "批量删除周边管理信息", notes = "根据ID批量删除周边管理信息")
    @RequestMapping(value = "/batchDelete", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('perip:bd')")
    public ResponseMessage batchDelete(@RequestBody List<String> ids, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return peripheryService.batchDelete(ids);
    }


    @PreAuthorize("hasAuthority('perip:g')")
    @ApiOperation(value = "数据绑定", notes = "数据绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "model", value = "数据绑定实体", required = true, dataType = "DataBindModel")
    })
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage dataBind(@RequestBody @Valid DataBindModel model, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        String updatedDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        String updatedUser = getCurrentUser().getUsername();
        peripheryService.dataBind(updatedUser,updatedDate,model);
        return ResponseMessage.defaultResponse().setMsg("数据绑定成功");
    }

    @ApiOperation(value = "权重更改", notes = "权重更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PutMapping(value = "/weight")
    @PreAuthorize("hasAuthority('perip:w')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "周边管理")
    public ResponseMessage goWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel,getCurrentUser(),PeripheryEntity.class);
    }


    @ApiOperation(value = "周边管理信息关联标签", notes = "周边管理信息关联标签")
    @ApiImplicitParam(name = "tags", value = "关联标签", required = true, dataType = "Map")
    @RequestMapping(value = "/relateTags", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('perip:rt')")
    public ResponseMessage relateTags(@RequestBody Map<String, Object> tags, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return peripheryService.relateTags(tags,getCurrentUser());
    }

}
