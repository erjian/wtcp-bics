package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.HallService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.RequestUtil;
import cn.com.wanwei.mybatis.model.DeptCodeBindModel;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import com.google.common.base.Strings;
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
@RequestMapping("/hall")
@Api(value = "场馆厅室", tags = "场馆厅室管理相关接口")
public class HallController extends BaseController {

    @Autowired
    private HallService hallService;

    @Autowired
    private CommonService commonService;


    @ApiOperation(value = "场馆厅室分页列表", notes = "场馆厅室分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, defaultValue = "0", dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页大小", required = true, defaultValue = "10", dataType = "String")
    })
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('hall:r')")
    @OperationLog(value = "wtcp-bics/场馆厅室分页列表", operate = "r", module = "场馆厅室")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        EscapeCharUtils.escape(filter, "title");
        return ResponseMessage.defaultResponse().setData(hallService.findByPageWithDeptCode(page, size, filter));
    }

    @ApiOperation(value = "获取场馆厅室列表（可根据所属场馆ID：venueId 和厅室名称：title 进行查询）",
            notes = "获取场馆厅室列表（可根据所属场馆ID：venueId 和厅室名称：title 进行查询）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "venueId", value = "所属场馆ID"),
            @ApiImplicitParam(name = "title", value = "场馆厅室名称")
    })
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('hall:r')")
    @OperationLog(value = "wtcp-bics/获取场馆厅室列表", operate = "r", module = "场馆厅室")
    public ResponseMessage findByList(String title, String venueId, HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        filter.put("title", Strings.isNullOrEmpty(title) ? "" : title);
        filter.put("venueId", Strings.isNullOrEmpty(venueId) ? "" : venueId);
        EscapeCharUtils.escape(filter, "title");
        return ResponseMessage.defaultResponse().setData(hallService.findByList(filter));
    }

    @ApiOperation(value = "查询场馆厅室详情", notes = "根据ID查询场馆厅室详情")
    @ApiImplicitParam(name = "id", value = "场馆厅室ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('hall:v')")
    @OperationLog(value = "wtcp-bics/根据id查询场馆厅室详情", operate = "v", module = "场馆厅室")
    public ResponseMessage detail(@PathVariable("id") String id) throws Exception {
        return ResponseMessage.defaultResponse().setData(hallService.findById(id));
    }

    @ApiOperation(value = "删除场馆厅室", notes = "根据ID删除场馆厅室")
    @ApiImplicitParam(name = "id", value = "场馆厅室ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('hall:d')")
    @OperationLog(value = "wtcp-bics/根据id删除场馆厅室", operate = "d", module = "场馆厅室")
    public ResponseMessage delete(@PathVariable("id") String id) {
        return hallService.deleteById(id);
    }

    @ApiOperation(value = "场馆厅室新增", notes = "场馆厅室新增")
    @ApiImplicitParam(name = "hallModel", value = "场馆厅室model", required = true, dataType = "EntityTagsModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('hall:c')")
    @OperationLog(value = "wtcp-bics/场馆厅室新增", operate = "c", module = "场馆厅室管理")
    public ResponseMessage save(@RequestBody EntityTagsModel<HallEntity> hallModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return hallService.save(hallModel, getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "场馆厅室编辑", notes = "场馆厅室编辑")
    @ApiImplicitParam(name = "hallModel", value = "场馆厅室", required = true, dataType = "EntityTagsModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('hall:u')")
    @OperationLog(value = "wtcp-bics/场馆厅室编辑", operate = "u", module = "场馆厅室管理")
    public ResponseMessage edit(@PathVariable("id") String id, @RequestBody EntityTagsModel<HallEntity> hallModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return hallService.updateByPrimaryKey(id, hallModel, getCurrentUser());
    }

    @PreAuthorize("hasAuthority('hall:w')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PutMapping(value = "/weight")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "w", module = "场馆厅室")
    public ResponseMessage changeWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel, getCurrentUser(), HallEntity.class);
    }

    @PreAuthorize("hasAuthority('hall:o')")
    @ApiOperation(value = "上下线状态变更", notes = "上下线状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "场馆厅室ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "上下线状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateOnlineStatus", method = RequestMethod.GET)
    public ResponseMessage updateOnlineStatus(@RequestParam String id, @RequestParam Integer status) throws Exception {
        User user = getCurrentUser();
        return hallService.updateOnlineStatus(id, status, user.getUsername());
    }

    @PreAuthorize("hasAuthority('hall:b')")
    @ApiOperation(value = "组织机构切换", notes = "组织机构切换")
    @ApiImplicitParams({@ApiImplicitParam(name = "deptCodeBindModel", value = "组织机构切换model", required = true, dataType = "DeptCodeBindModel")})
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage updateDeptCode(@RequestBody @Valid DeptCodeBindModel deptCodeBindModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        deptCodeBindModel.setCurrentUserName(getCurrentUser().getUsername());
        return hallService.updateDeptCode(deptCodeBindModel);
    }

    @ApiOperation(value = "场馆厅室名称是否重复", notes = "场馆厅室名称是否重复(status:1 表示不重复，0表示重复)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "场馆厅室id"),
            @ApiImplicitParam(name = "title", value = "场馆厅室名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/existByTitle", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/检查场馆厅室名称是否重复", operate = "", module = "场馆厅室", frontCode = "", resource = "")
    public ResponseMessage existByTitle(@RequestParam String title, String id) {
        return hallService.existsByValueAndIdNot(title, id, "该名称已存在");
    }

}
