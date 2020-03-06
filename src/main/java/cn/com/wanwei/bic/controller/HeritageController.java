package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.HeritageEntity;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.HeritageService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import cn.com.wanwei.mybatis.model.DeptCodeBindModel;
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
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/heritage")
@Api(value = "非遗管理", tags = "非遗管理相关接口")
public class HeritageController extends BaseController{

    @Autowired
    private HeritageService heritageService;

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "非遗管理分页列表", notes = "非遗管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('heritage:r')")
    @OperationLog(value = "wtcp-bics/非遗管理分页列表", operate = "r", module = "景区管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        EscapeCharUtils.escape(filter, "title");
        return ResponseMessage.defaultResponse().setData(heritageService.findByPageWithDeptCode(page, size, filter));
    }

    @ApiOperation(value = "查询非遗详情", notes = "根据ID查询非遗详情")
    @ApiImplicitParam(name = "id", value = "非遗ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('heritage:v')")
    @OperationLog(value = "wtcp-bics/根据id查询非遗详情", operate = "v", module = "非遗管理")
    public ResponseMessage detail(@PathVariable("id") String id) throws Exception {
        HeritageEntity heritageEntity = heritageService.findById(id);
        if (heritageEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(heritageEntity);
    }

    @ApiOperation(value = "删除非遗", notes = "根据ID删除非遗")
    @ApiImplicitParam(name = "id", value = "非遗ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('heritage:d')")
    @OperationLog(value = "wtcp-bics/根据id删除非遗", operate = "d", module = "非遗管理")
    public ResponseMessage delete(@PathVariable("id") String id) throws Exception {
        return heritageService.deleteById(id);
    }

    @ApiOperation(value = "非遗新增", notes = "非遗新增")
    @ApiImplicitParam(name = "heritageModel", value = "非遗model", required = true, dataType = "EntityTagsModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('heritage:c')")
    @OperationLog(value = "wtcp-bics/非遗新增", operate = "c", module = "非遗管理")
    public ResponseMessage save(@RequestBody EntityTagsModel<HeritageEntity> heritageModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return heritageService.insert(heritageModel, getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "非遗编辑", notes = "非遗编辑")
    @ApiImplicitParam(name = "heritageModel", value = "非遗", required = true, dataType = "EntityTagsModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('heritage:u')")
    @OperationLog(value = "wtcp-bics/非遗编辑", operate = "u", module = "非遗管理")
    public ResponseMessage edit(@PathVariable("id") String id, @RequestBody EntityTagsModel<HeritageEntity> heritageModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return heritageService.updateByPrimaryKey(id, heritageModel, getCurrentUser());
    }

    @ApiOperation(value = "权重修改", notes = "非遗权重修改")
    @ApiImplicitParams({ @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")})
    @PutMapping(value = "/weight")
    @PreAuthorize("hasAuthority('heritage:w')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "w", module = "非遗管理")
    public ResponseMessage changeWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel, getCurrentUser(), HeritageEntity.class);
    }

    @ApiOperation(value = "上下线状态变更", notes = "上下线状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "非遗ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "上下线状态", required = true, dataType = "String")
    })
    @PreAuthorize("hasAuthority('heritage:o')")
    @RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/上下线修改", operate = "w", module = "非遗管理")

    public ResponseMessage changeStatus(@RequestParam String id, @RequestParam Integer status) throws Exception {
        return heritageService.updateOnlineStatus(id, status, getCurrentUser().getUsername());
    }

    @PreAuthorize("hasAuthority('heritage:e')")
    @ApiOperation(value = "非遗审核", notes = "非遗审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "非遗信息ID", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态", required = true),
            @ApiImplicitParam(name = "msg", value = "审核意见")
    })
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ResponseMessage audit(@RequestParam String id, @RequestParam int auditStatus, String msg) throws Exception {
        return heritageService.updateAuditStatus(id, auditStatus, msg, getCurrentUser());
    }

    @PreAuthorize("hasAuthority('heritage:b')")
    @ApiOperation(value = "组织机构切换", notes = "组织机构切换")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "组织机构切换model", required = true, dataType = "DataBindModel")})
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage dataBind(@RequestBody @Valid DeptCodeBindModel deptCodeBindModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        deptCodeBindModel.setCurrentUserName(getCurrentUser().getUsername());
        return heritageService.updateDeptCode(deptCodeBindModel);
    }

    @ApiOperation(value = "非遗名称是否重复", notes = "非遗名称是否重复(status:1 表示不重复，0表示重复)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "非遗id"),
            @ApiImplicitParam(name = "title", value = "非遗名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/existByTitle", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/检查非遗名称是否重复", operate = "", module = "非遗", frontCode = "", resource = "")
    public ResponseMessage existByTitle(@RequestParam String title, String id) {
        return heritageService.existsByValueAndIdNot(title, id, "该名称已存在");

    }
}
