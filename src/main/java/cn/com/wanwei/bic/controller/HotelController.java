package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.HotelEntity;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.HotelService;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/hotel")
@Api(value = "酒店管理", tags = "酒店管理相关接口")
public class HotelController extends BaseController{

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "酒店管理分页列表", notes = "酒店管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('hotel:r')")
    @OperationLog(value = "wtcp-bics/酒店管理分页列表", operate = "r", module = "酒店管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        EscapeCharUtils.escape(filter, "title","subTitle");
        return ResponseMessage.defaultResponse().setData(hotelService.findByPageWithDeptCode(page, size, filter));
    }

    @ApiOperation(value = "获取酒店列表", notes = "获取酒店列表（未分页）")
    @RequestMapping(value = "/getHotelList", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('hotel:r')")
    @OperationLog(value = "wtcp-bics/获取酒店列表", operate = "r", module = "酒店管理")
    public ResponseMessage findByList(String title, HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        EscapeCharUtils.escape(filter, "title","subTitle");
        return ResponseMessage.defaultResponse().setData(hotelService.findByList(filter));
    }


    @ApiOperation(value = "查询酒店详情", notes = "根据id查询酒店详情")
    @ApiImplicitParam(name = "id", value = "酒店ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('hotel:v')")
    @OperationLog(value = "wtcp-bics/根据id查询酒店详情", operate = "v", module = "酒店管理")
    public ResponseMessage findById(@PathVariable("id") String id) throws Exception {
        HotelEntity hotelEntity = hotelService.findById(id);
        if (hotelEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(hotelEntity);
    }

    @ApiOperation(value = "删除酒店", notes = "根据ID删除酒店")
    @ApiImplicitParam(name = "id", value = "酒店ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('hotel:d')")
    @OperationLog(value = "wtcp-bics/根据id删除酒店", operate = "d", module = "酒店管理")
    public ResponseMessage deleteById(@PathVariable("id") String id) throws Exception {
        return hotelService.deleteById(id);
    }

    @ApiOperation(value = "酒店新增", notes = "酒店新增")
    @ApiImplicitParam(name = "hotelModel", value = "酒店model", required = true, dataType = "EntityTagsModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('hotel:c')")
    @OperationLog(value = "wtcp-bics/酒店新增", operate = "c", module = "酒店管理")
    public ResponseMessage insert(@RequestBody EntityTagsModel<HotelEntity> hotelModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return hotelService.insert(hotelModel, getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "酒店编辑", notes = "酒店编辑")
    @ApiImplicitParam(name = "hotelModel", value = "酒店", required = true, dataType = "EntityTagsModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('hotel:u')")
    @OperationLog(value = "wtcp-bics/酒店编辑", operate = "u", module = "酒店管理")
    public ResponseMessage updateByPrimaryKey(@PathVariable("id") String id, @RequestBody EntityTagsModel<HotelEntity> hotelModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return hotelService.updateByPrimaryKey(id, hotelModel, getCurrentUser());
    }

    @ApiOperation(value = "权重修改", notes = "酒店权重修改")
    @ApiImplicitParams({ @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")})
    @PutMapping(value = "/weight")
    @PreAuthorize("hasAuthority('hotel:w')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "w", module = "酒店管理")
    public ResponseMessage changeWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel, getCurrentUser(), HotelEntity.class);
    }



    @ApiOperation(value = "上下线状态变更", notes = "上下线状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "酒店ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "上下线状态", required = true, dataType = "String")
    })
    @PreAuthorize("hasAuthority('hotel:o')")
    @RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/上下线修改", operate = "w", module = "酒店管理")
    public ResponseMessage updateOnlineStatus(@RequestParam String id, @RequestParam Integer status) throws Exception {
        return hotelService.updateOnlineStatus(id, status, getCurrentUser().getUsername());
    }

    @PreAuthorize("hasAuthority('hotel:e')")
    @ApiOperation(value = "酒店审核", notes = "酒店审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "酒店信息ID", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态", required = true),
            @ApiImplicitParam(name = "msg", value = "审核意见")
    })
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ResponseMessage updateAuditStatus(@RequestParam String id, @RequestParam int auditStatus, String msg) throws Exception {
        return hotelService.updateAuditStatus(id, auditStatus, msg, getCurrentUser());
    }


    @PreAuthorize("hasAuthority('hotel:b')")
    @ApiOperation(value = "组织机构切换", notes = "组织机构切换")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "组织机构切换model", required = true, dataType = "DataBindModel")})
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage updateDeptCode(@RequestBody @Valid DeptCodeBindModel deptCodeBindModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        deptCodeBindModel.setCurrentUserName(getCurrentUser().getUsername());
        return hotelService.updateDeptCode(deptCodeBindModel);
    }


    @ApiOperation(value = "酒店名称是否重复", notes = "酒店名称是否重复(status:1 表示不重复，0表示重复)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "酒店id"),
            @ApiImplicitParam(name = "title", value = "酒店名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/existByTitle", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/检查酒店名称是否重复", operate = "", module = "酒店", frontCode = "", resource = "")
    public ResponseMessage existByTitle(@RequestParam String title, String id) {
        return hotelService.existsByValueAndIdNot(title, id, "该名称已存在");

    }
}
