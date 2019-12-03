package cn.com.wanwei.bic.controller;


import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.DriveCampEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.DriveCampService;
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
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/driveCamp")
@Api(value = "自驾营地管理", tags = "自驾营地管理")
public class DriveCampController extends BaseController{

    @Autowired
    private DriveCampService driveCampService;

    @ApiOperation(value = "自驾营地管理分页列表",notes = "自驾营地管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('driveCamp:r')")
    @OperationLog(value = "wtcp-bics/自驾营地管理分页列表", operate = "r", module = "自驾营地管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request){
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return driveCampService.findByPage(page,size,filter);
    }

    @ApiOperation(value = "查询自驾营地信息", notes = "查询自驾营地信息")
    @ApiImplicitParam(name = "id", value = "自驾营地信息ID", required = true)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('driveCamp:r')")
    @OperationLog(value = "wtcp-bics/查询自驾营地信息", operate = "r", module = "自驾营地管理")
    public ResponseMessage find(@PathVariable("id") String id) {
        return driveCampService.find(id);
    }

    @ApiOperation(value = "新增自驾营地信息", notes = "新增自驾营地信息")
    @ApiImplicitParam(name = "driveCampModel", value = "自驾营地实体",dataType="EntityTagsModel", required = true)
    @PostMapping
    @PreAuthorize("hasAuthority('driveCamp:c')")
    @OperationLog(value = "wtcp-bics/新增自驾营地信息", operate = "c", module = "自驾营地管理")
    public ResponseMessage create(@RequestBody EntityTagsModel<DriveCampEntity> driveCampModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return driveCampService.create(driveCampModel,getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "编辑自驾营地信息", notes = "编辑自驾营地信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "自驾营地信息ID", required = true),
            @ApiImplicitParam(name = "driveCampModel", value = "自驾营地实体",dataType="EntityTagsModel", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('driveCamp:u')")
    @OperationLog(value = "wtcp-bics/编辑自驾营地信息", operate = "u", module = "自驾营地管理")
    public ResponseMessage edit(@PathVariable(value = "id") String id, @RequestBody EntityTagsModel<DriveCampEntity> driveCampModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return driveCampService.update(id,driveCampModel,getCurrentUser());
    }

    @ApiOperation(value = "删除自驾营地信息", notes = "删除自驾营地信息")
    @ApiImplicitParam(name = "id", value = "自驾营地信息ID", required = true)
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('driveCamp:d')")
    @OperationLog(value = "wtcp-bics/删除自驾营地信息", operate = "d", module = "自驾营地管理")
    public ResponseMessage delete(@PathVariable(value = "id") String id){
        return driveCampService.delete(id);
    }

    @ApiOperation(value = "权重更改", notes = "权重更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PutMapping(value = "/weight")
    @PreAuthorize("hasAuthority('driveCamp:w')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "自驾营地管理")
    public ResponseMessage goWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return driveCampService.goWeight(weightModel,getCurrentUser());
    }

    @ApiOperation(value = "标题重名校验", notes = "标题重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "自驾营地信息ID"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    @GetMapping(value = "/checkTitle")
    public ResponseMessage checkTitle(@RequestParam(value = "id",required = false) String id,
                                      @RequestParam(value = "title",required = false) String title){
        return driveCampService.checkTitle(id,title);
    }

    @ApiOperation(value = "自驾营地信息审核", notes = "自驾营地信息审核")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",required = true,dataType = "AuditLogEntity")
    @PutMapping(value = "/audit")
    @PreAuthorize("hasAuthority('driveCamp:a')")
    @OperationLog(value = "wtcp-bics/自驾营地信息审核", operate = "a", module = "自驾营地管理")
    public ResponseMessage audit(@RequestBody AuditLogEntity auditLogEntity , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return driveCampService.auditOrIssue(auditLogEntity,getCurrentUser(),0);
    }

    @ApiOperation(value = "自驾营地信息上下线", notes = "自驾营地信息上下线")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",required = true,dataType = "AuditLogEntity")
    @PutMapping(value = "/issue")
    @PreAuthorize("hasAuthority('driveCamp:s')")
    @OperationLog(value = "wtcp-bics/自驾营地信息上下线", operate = "u", module = "自驾营地管理")
    public ResponseMessage issue(@RequestBody AuditLogEntity auditLogEntity ,BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return driveCampService.auditOrIssue(auditLogEntity,getCurrentUser(),1);
    }

    @PreAuthorize("hasAuthority('driveCamp:g')")
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
        driveCampService.dataBind(updatedUser,updatedDate,model);

        return ResponseMessage.defaultResponse().setMsg("数据绑定成功");
    }

    @ApiOperation(value = "自驾营地信息标签关联", notes = "自驾营地信息标签关联")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "自驾营地信息ID", required = true)
    })
    @PutMapping(value = "relateTags/{id}")
    @PreAuthorize("hasAuthority('driveCamp:u')")
    @OperationLog(value = "wtcp-bics/自驾营地信息标签关联", operate = "u", module = "自驾营地管理")
    public ResponseMessage relateTags(@PathVariable(value = "id") String id, @RequestBody List<BaseTagsEntity> list , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return driveCampService.relateTags(id,list,getCurrentUser());
    }

    @ApiOperation(value = "获取自驾营地信息", notes = "获取自驾营地信息")
    @GetMapping(value = "/getDriveCampList")
    @PreAuthorize("hasAuthority('driveCamp:r')")
    @OperationLog(value = "wtcp-bics/获取自驾营地信息", operate = "r", module = "自驾营地管理")
    public ResponseMessage getDriveCampList() {
        return driveCampService.getDriveCampList();
    }
}
