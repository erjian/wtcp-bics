package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.CelebrityEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CelebrityService;
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
@RequestMapping("/celebrity")
@Api(value = "名人管理", tags = "名人管理相关接口")
public class CelebrityController extends BaseController {

    @Autowired
    private CelebrityService celebrityService;

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "名人管理分页列表", notes = "名人管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('celebrity:r')")
    @OperationLog(value = "wtcp-bics/名人管理分页列表", operate = "r", module = "名人管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return celebrityService.findByPage(page, size, filter);
    }

    @ApiOperation(value = "名人管理详情", notes = "根据ID查询名人详情")
    @ApiImplicitParam(name = "id", value = "名人ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('celebrity:v')")
    @OperationLog(value = "wtcp-bics/根据ID查询名人详情", operate = "v", module = "名人管理")
    public ResponseMessage findById(@PathVariable("id") String id) throws Exception {
        return celebrityService.findById(id);
    }

    @ApiOperation(value = "删除名人数据", notes = "根据ID删除名人数据")
    @ApiImplicitParam(name = "id", value = "名人ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('celebrity:d')")
    @OperationLog(value = "wtcp-bics/根据ID删除名人数据", operate = "d", module = "名人管理")
    public ResponseMessage deleteById(@PathVariable("id") String id) throws Exception {
        return celebrityService.deleteById(id);
    }

    @ApiOperation(value = "名人新增", notes = "名人新增")
    @ApiImplicitParam(name = "celebrityModel", value = "名人model", required = true, dataType = "EntityTagsModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('celebrity:c')")
    @OperationLog(value = "wtcp-bics/名人新增", operate = "c", module = "名人管理")
    public ResponseMessage insert(@RequestBody EntityTagsModel<CelebrityEntity> celebrityModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return celebrityService.insert(celebrityModel, getCurrentUser());
    }

    @ApiOperation(value = "名人编辑", notes = "名人编辑")
    @ApiImplicitParam(name = "celebrityModel", value = "名人model", required = true, dataType = "EntityTagsModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('celebrity:u')")
    @OperationLog(value = "wtcp-bics/名人编辑", operate = "u", module = "名人管理")
    public ResponseMessage updateById(@PathVariable("id") String id, @RequestBody EntityTagsModel<CelebrityEntity> celebrityModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return celebrityService.updateById(id, celebrityModel, getCurrentUser());
    }

    @ApiOperation(value = "名人信息关联标签", notes = "名人信息关联标签")
    @ApiImplicitParam(name = "tags", value = "关联标签", required = true, dataType = "Map")
    @RequestMapping(value = "/relateTags", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('celebrity:rt')")
    @OperationLog(value = "wtcp-bics/名人信息关联标签", operate = "u", module = "名人管理")
    public ResponseMessage insertTagsBatch(@RequestBody Map<String, Object> tags, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return celebrityService.insertTagsBatch(tags, getCurrentUser());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PreAuthorize("hasAuthority('celebrity:w')")
    @PutMapping(value = "/weight")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "名人管理")
    public ResponseMessage changeWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel, getCurrentUser(), CelebrityEntity.class);
    }

    @ApiOperation(value = "上下线状态变更", notes = "上下线状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "名人ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "上下线状态", required = true, dataType = "String")
    })
    @PreAuthorize("hasAuthority('celebrity:o')")
    @RequestMapping(value = "/changeStatus/{id}/{status}", method = RequestMethod.GET)
    public ResponseMessage updateStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) throws Exception {
        return celebrityService.updateStatus(id, status, getCurrentUser().getUsername());
    }


    @ApiOperation(value = "名人信息审核", notes = "名人信息审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "名人信息ID", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态", required = true),
            @ApiImplicitParam(name = "msg", value = "审核意见")
    })
    @PreAuthorize("hasAuthority('celebrity:e')")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ResponseMessage updateAuditStatus(@RequestParam String id, @RequestParam int auditStatus, String msg) throws Exception {
        return celebrityService.updateAuditStatus(id, auditStatus, msg, getCurrentUser());
    }

    @ApiOperation(value = "关联组织机构", notes = "关联组织机构")
    @PreAuthorize("hasAuthority('celebrity:b')")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "组织机构model", required = true, dataType = "DataBindModel")})
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage updateDeptCode(@RequestBody @Valid DataBindModel model, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return celebrityService.updateDeptCode(getCurrentUser().getUsername(), model);
    }
}
