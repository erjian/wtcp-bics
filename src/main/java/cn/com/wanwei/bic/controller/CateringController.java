package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.CateringEntity;
import cn.com.wanwei.bic.entity.CelebrityEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CateringService;
import cn.com.wanwei.bic.service.CommonService;
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
import java.util.Map;

/**
 * @author
 */

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/catering")
@Api(value = "餐饮管理", tags = "餐饮管理相关接口")
public class CateringController extends BaseController {
    @Autowired
    private CateringService cateringService;

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "餐饮管理分页列表", notes = "餐饮管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('catering:r')")
    @OperationLog(value = "wtcp-bics/餐饮管理分页列表", operate = "r", module = "餐饮管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return cateringService.findByPage(page, size, filter);
    }

    @ApiOperation(value = "获取餐饮列表（不分页）", notes = "获取餐饮列表（不分页）")
    @PreAuthorize("hasAuthority('catering:r')")
    @RequestMapping(value = "/findByList", method = RequestMethod.GET)
    public ResponseMessage findByList(HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return cateringService.findByList(filter);
    }

    @ApiOperation(value = "餐饮管理详情", notes = "根据ID查询餐饮信息详情")
    @ApiImplicitParam(name = "id", value = "餐饮ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('catering:v')")
    @OperationLog(value = "wtcp-bics/根据ID查询餐饮信息详情", operate = "v", module = "餐饮管理")
    public ResponseMessage findById(@PathVariable("id") String id) throws Exception {
        return cateringService.findById(id);
    }

    @ApiOperation(value = "删除餐饮管理", notes = "根据ID删除餐饮管理")
    @ApiImplicitParam(name = "id", value = "餐饮ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('catering:d')")
    @OperationLog(value = "wtcp-bics/根据ID删除餐饮管理", operate = "d", module = "餐饮管理")
    public ResponseMessage deleteById(@PathVariable("id") String id) throws Exception {
        return cateringService.deleteById(id);
    }

    @ApiOperation(value = "餐饮新增", notes = "餐饮新增")
    @ApiImplicitParam(name = "cateringModel", value = "餐饮model", required = true, dataType = "EntityTagsModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('catering:c')")
    @OperationLog(value = "wtcp-bics/餐饮新增", operate = "c", module = "餐饮管理")
    public ResponseMessage insert(@RequestBody EntityTagsModel<CateringEntity> cateringModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return cateringService.insert(cateringModel, getCurrentUser());
    }

    @ApiOperation(value = "餐饮编辑", notes = "餐饮编辑")
    @ApiImplicitParam(name = "cateringModel", value = "餐饮model", required = true, dataType = "EntityTagsModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('catering:u')")
    @OperationLog(value = "wtcp-bics/餐饮编辑", operate = "u", module = "餐饮管理")
    public ResponseMessage updateById(@PathVariable("id") String id, @RequestBody EntityTagsModel<CateringEntity> cateringModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return cateringService.updateById(id, cateringModel, getCurrentUser());
    }


    @ApiOperation(value = "餐饮信息关联标签", notes = "餐饮信息关联标签")
    @ApiImplicitParam(name = "tags", value = "关联标签", required = true, dataType = "Map")
    @RequestMapping(value = "/relateTags", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('catering:rt')")
    @OperationLog(value = "wtcp-bics/餐饮信息关联标签", operate = "u", module = "餐饮管理")
    public ResponseMessage insertTagsBatch(@RequestBody Map<String, Object> tags, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return cateringService.insertTagsBatch(tags, getCurrentUser());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PreAuthorize("hasAuthority('catering:w')")
    @PutMapping(value = "/weight")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "餐饮管理")
    public ResponseMessage changeWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel, getCurrentUser(), CateringEntity.class);
    }

    @ApiOperation(value = "上下线状态变更", notes = "上下线状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "餐饮ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "上下线状态", required = true, dataType = "String")
    })
    @PreAuthorize("hasAuthority('catering:o')")
    @RequestMapping(value = "/changeStatus/{id}/{status}", method = RequestMethod.GET)
    public ResponseMessage updateStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) throws Exception {
        return cateringService.updateStatus(id, status, getCurrentUser().getUsername());
    }


    @ApiOperation(value = "餐饮信息审核", notes = "餐饮信息审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "餐饮信息ID", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态", required = true),
            @ApiImplicitParam(name = "msg", value = "审核意见")
    })
    @PreAuthorize("hasAuthority('catering:e')")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ResponseMessage updateAuditStatus(@RequestParam String id, @RequestParam int auditStatus, String msg) throws Exception {
        return cateringService.updateAuditStatus(id, auditStatus, msg, getCurrentUser());
    }

    @ApiOperation(value = "关联组织机构", notes = "关联组织机构")
    @PreAuthorize("hasAuthority('catering:b')")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "组织机构model", required = true, dataType = "DataBindModel")})
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage updateDeptCode(@RequestBody @Valid DataBindModel model, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return cateringService.updateDeptCode(getCurrentUser().getUsername(), model);
    }

    @ApiOperation(value = "标题重名校验", notes = "标题重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "餐饮信息ID"),
            @ApiImplicitParam(name = "title", value = "标题")
    })
    @GetMapping(value = "/checkTitle")
    public ResponseMessage checkTitle(@RequestParam(value = "id", required = false) String id,
                                      @RequestParam(value = "title", required = false) String title) {
        return cateringService.findByTitleAndIdNot(title, id != null ? id : "-1");
    }
}
