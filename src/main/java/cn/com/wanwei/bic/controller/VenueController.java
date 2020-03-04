package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.VenueEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.VenueService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import com.alibaba.druid.util.StringUtils;
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
@RequestMapping("/venue")
@Api(value = "场馆管理", tags = "场馆管理相关接口")
public class VenueController extends BaseController {

    @Autowired
    private VenueService venueService;

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "场馆管理分页列表", notes = "场馆管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('venue:r')")
    @OperationLog(value = "wtcp-bics/场馆管理分页列表", operate = "r", module = "场馆管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return venueService.findByPage(page,size,filter);
    }

    @ApiOperation(value = "查询场馆详情", notes = "根据ID查询场馆详情")
    @ApiImplicitParam(name = "id", value = "场馆ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('venue:v')")
    @OperationLog(value = "wtcp-bics/根据id查询场馆详情", operate = "v", module = "场馆管理")
    public ResponseMessage findById(@PathVariable("id") String id) throws Exception {
        VenueEntity venueEntity = venueService.findById(id);
        if (venueEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(venueEntity);
    }

    @ApiOperation(value = "删除场馆", notes = "根据ID删除场馆")
    @ApiImplicitParam(name = "id", value = "场馆ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('venue:d')")
    @OperationLog(value = "wtcp-bics/根据id删除场馆", operate = "d", module = "场馆管理")
    public ResponseMessage deleteById(@PathVariable("id") String id) throws Exception {
        return venueService.deleteById(id);
    }

    @ApiOperation(value = "场馆新增", notes = "场馆新增")
    @ApiImplicitParam(name = "venueModel", value = "场馆model", required = true, dataType = "EntityTagsModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('venue:c')")
    @OperationLog(value = "wtcp-bics/场馆新增", operate = "c", module = "场馆管理")
    public ResponseMessage insert(@RequestBody EntityTagsModel<VenueEntity> venueModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return venueService.insert(venueModel,getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "场馆编辑", notes = "场馆编辑")
    @ApiImplicitParam(name = "venueModel", value = "场馆", required = true, dataType = "EntityTagsModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('venue:u')")
    @OperationLog(value = "wtcp-bics/场馆编辑", operate = "u", module = "场馆管理")
    public ResponseMessage updateById(@PathVariable("id") String id, @RequestBody EntityTagsModel<VenueEntity> venueModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return venueService.updateById(id,venueModel,getCurrentUser());
    }

    @ApiOperation(value = "场馆基础信息关联标签", notes = "场馆基础信息关联标签")
    @ApiImplicitParam(name = "tags", value = "关联标签", required = true, dataType = "Map")
    @RequestMapping(value = "/relateTags", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('venue:rt')")
    @OperationLog(value = "wtcp-bics/场馆基础信息关联标签", operate = "u", module = "场馆管理")
    public ResponseMessage updateRelateTags(@RequestBody Map<String, Object> tags, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return venueService.updateRelateTags(tags,getCurrentUser());
    }

    @PreAuthorize("hasAuthority('venue:w')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PutMapping(value = "/weight")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "场馆基础信息管理")
    public ResponseMessage updateChangeWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel,getCurrentUser(),VenueEntity.class);
    }

    @PreAuthorize("hasAuthority('venue:o')")
    @ApiOperation(value = "上下线状态变更", notes = "上下线状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "场馆ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "上下线状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/changeStatus/{id}/{status}", method = RequestMethod.GET)
    public ResponseMessage updateChangeStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) throws Exception {
        return venueService.updateChangeStatus(id,status,getCurrentUser().getUsername());
    }

    @PreAuthorize("hasAuthority('venue:e')")
    @ApiOperation(value = "场馆审核", notes = "场馆审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "场馆信息ID", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态", required = true),
            @ApiImplicitParam(name = "msg", value = "审核意见")
    })
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ResponseMessage updateExamineVenue(@RequestParam String id, @RequestParam int auditStatus, String msg) throws Exception {
        return venueService.updateExamineVenue(id, auditStatus, msg, getCurrentUser());
    }

    @PreAuthorize("hasAuthority('venue:b')")
    @ApiOperation(value = "数据绑定", notes = "数据绑定")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "数据绑定model", required = true, dataType = "DataBindModel")})
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage updateDataBind(@RequestBody @Valid DataBindModel model, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return venueService.updateDataBind(getCurrentUser().getUsername(),model);
    }

    @ApiOperation(value = "获取场馆列表", notes = "获取场馆列表")
    @RequestMapping(value = "/getVenueList", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/获取场馆列表", operate = "r", module = "场馆管理")
    public ResponseMessage findVenueInfo(String title) throws Exception {
        return venueService.findVenueInfo(StringUtils.isEmpty(title)?"":title.trim().toLowerCase(), null);
    }

    @ApiOperation(value = "场馆名称是否重复", notes = "场馆名称是否重复(status:1 表示不重复，0表示重复)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "场馆名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "场馆id")
    })
    @RequestMapping(value = "/existByTitle", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/检查场馆名称是否重复", operate = "", module = "场馆基础信息管理", frontCode = "", resource = "")
    public ResponseMessage findByTitleAndIdNot(@RequestParam String title, String id) {
        return venueService.findByTitleAndIdNot(title, id != null ? id : "-1");
    }

}

