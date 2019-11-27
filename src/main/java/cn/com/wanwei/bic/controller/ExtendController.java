package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.ExtendEntity;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.ExtendService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
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

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/18 10:30:30
 * @desc 扩展信息管理控制层
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/extend")
@Api(value = " 扩展信息管理", tags = " 扩展信息管理相关接口")
public class ExtendController extends BaseController{

    @Autowired
    private ExtendService extendService;

    @ApiOperation(value = "扩展信息管理分页列表", notes = "扩展信息管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('extend:r')")
    @OperationLog(value = "wtcp-bics/扩展信息管理分页列表", operate = "r", module = "扩展信息管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return extendService.findByPage(page,size,getCurrentUser(),filter);
    }

    @ApiOperation(value = "扩展信息新增", notes = "扩展信息新增")
    @ApiImplicitParam(name = "extendModel", value = "扩展信息model", required = true, dataType = "EntityTagsModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('extend:c')")
    @OperationLog(value = "wtcp-bics/扩展信息新增", operate = "c", module = "扩展信息管理")
    public ResponseMessage save(@RequestBody EntityTagsModel<ExtendEntity> extendModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return extendService.save(extendModel,getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "扩展信息编辑", notes = "扩展信息编辑")
    @ApiImplicitParam(name = "extendModel", value = "扩展信息model", required = true, dataType = "EntityTagsModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('extend:u')")
    @OperationLog(value = "wtcp-bics/扩展信息编辑", operate = "u", module = "扩展信息管理")
    public ResponseMessage edit(@PathVariable("id") String id, @RequestBody EntityTagsModel<ExtendEntity> extendModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return extendService.edit(id,extendModel,getCurrentUser());
    }

    @ApiOperation(value = "查询扩展信息详情", notes = "根据ID查询扩展信息详情")
    @ApiImplicitParam(name = "id", value = "扩展信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('extend:v')")
    @OperationLog(value = "wtcp-bics/根据id查询扩展信息详情", operate = "v", module = "扩展信息管理")
    public ResponseMessage detail(@PathVariable("id") String id) throws Exception{
        return extendService.selectByPrimaryKey(id);
    }

    @ApiOperation(value = "扩展信息上线", notes = "扩展信息上线")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",required = true,dataType = "AuditLogEntity")
    @PutMapping(value = "/issue")
    @PreAuthorize("hasAuthority('extend:s')")
    public ResponseMessage issue(@RequestBody AuditLogEntity auditLogEntity , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return extendService.auditOrIssue(auditLogEntity,getCurrentUser(),1);
    }

    @ApiOperation(value = "扩展信息审核", notes = "扩展信息审核")
    @ApiImplicitParam(name = "auditLogEntity", value = "审核记录实体",required = true,dataType = "AuditLogEntity")
    @PutMapping(value = "/audit")
    @PreAuthorize("hasAuthority('extend:a')")
    public ResponseMessage audit(@RequestBody AuditLogEntity auditLogEntity , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return extendService.auditOrIssue(auditLogEntity,getCurrentUser(),0);
    }

    @ApiOperation(value = "删除扩展信息", notes = "根据ID删除扩展信息")
    @ApiImplicitParam(name = "id", value = "扩展信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('extend:d')")
    @OperationLog(value = "wtcp-bics/根据id删除扩展信息", operate = "d", module = "扩展信息管理")
    public ResponseMessage delete(@PathVariable("id") String id) throws Exception {
        return extendService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "扩展信息权重修改", notes = "扩展信息权重修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "扩展信息ID", required = true),
            @ApiImplicitParam(name = "weight", value = "权重", required = true)
    })
    @RequestMapping(value = "/weight/{id}}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('extend:q')")
    @OperationLog(value = "wtcp-bics/扩展信息权重修改", operate = "u", module = "扩展信息权重修改")
    public ResponseMessage weight(@PathVariable("id") String id, @PathVariable("weight") Float weight) throws Exception {
        return extendService.changeWeight(id,weight,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "扩展信息关联标签", notes = "扩展信息关联标签")
    @ApiImplicitParam(name = "tags", value = "关联标签", required = true, dataType = "Map")
    @RequestMapping(value = "/relateTags", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('extend:t')")
    @OperationLog(value = "wtcp-bics/扩展信息关联标签", operate = "u", module = "扩展信息")
    public ResponseMessage relateTags(@RequestBody Map<String, Object> tags, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return extendService.relateTags(tags,getCurrentUser());
    }

    @ApiOperation(value = "标题重名校验", notes = "标题重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "扩展信息信息ID"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    @GetMapping(value = "/checkTitle")
    public ResponseMessage checkTitle(@RequestParam(value = "id",required = false) String id,
                                      @RequestParam(value = "title",required = false) String title){
        return extendService.checkTitle(id,title);
    }

}
