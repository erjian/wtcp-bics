package cn.com.wanwei.bic.controller;


import cn.com.wanwei.bic.entity.RentalCarEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.RentalCarService;
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
 * wtcp-bics/租车
 */
@Slf4j
@Api(value = "汽车租赁",tags = "租车管理相关接口")
@RestController
@RefreshScope
@RequestMapping("/rentalCar")
public class RentalCarController extends BaseController{

    @Autowired
    private RentalCarService rentalCarService;

    @Autowired
    private CommonService commonService;

    @GetMapping("/page")
    @ApiOperation(value = "汽车租赁分页列表",notes = "汽车租赁分页列表")
    @PreAuthorize("hasAuthority('rental:r')")
    @OperationLog(value = "wtcp-bics/汽车租赁分页列表", operate = "r", module = "租车管理")
    public ResponseMessage findByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                      @RequestParam(value = "size",defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception{
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return rentalCarService.findByPage(page,size,filter);
    }


    @ApiOperation(value = "查询汽车租赁详情", notes = "根据ID查询租车详情")
    @ApiImplicitParam(name = "id", value = "租车Id", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('rental:v')")
    @OperationLog(value = "wtcp-bics/根据ID查询租车详情", operate = "v", module = "租车管理")
    public ResponseMessage detail (@PathVariable("id") String id) throws Exception{
        return rentalCarService.detail(id);
    }


    @ApiOperation(value = "删除租车信息", notes = "根据ID删除租车信息")
    @ApiImplicitParam(name = "id", value = "租车信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('rental:d')")
    @OperationLog(value = "wtcp-bics/根据ID删除租车信息", operate = "d", module = "租车管理")
    public ResponseMessage delete(@PathVariable("id") String id) throws Exception {
        return rentalCarService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "租车新增", notes = "租车新增")
    @ApiImplicitParam(name = "rentalCarModel", value = "租车model", required = true, dataType = "RentalCarModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('rental:c')")
    @OperationLog(value = "wtcp-bics/租车新增", operate = "c", module = "租车管理")
    public ResponseMessage save(@RequestBody EntityTagsModel<RentalCarEntity> rentalCarModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return rentalCarService.save(rentalCarModel,getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "租车编辑", notes = "租车编辑")
    @ApiImplicitParam(name = "rentalCarModel", value = "租车model", required = true, dataType = "RentalCarModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('rental:u')")
    @OperationLog(value = "wtcp-bics/租车编辑", operate = "u", module = "租车管理")
    public ResponseMessage edit(@PathVariable("id") String id, @RequestBody EntityTagsModel<RentalCarEntity> rentalCarModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return rentalCarService.edit(id,rentalCarModel,getCurrentUser());
    }


    @ApiOperation(value = "租车信息关联标签", notes = "租车信息关联标签")
    @ApiImplicitParam(name = "tags", value = "关联标签", required = true, dataType = "Map")
    @RequestMapping(value = "/relateTags", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('rental:rt')")
    @OperationLog(value = "wtcp-bics/租车信息关联标签", operate = "u", module = "租车管理")
    public ResponseMessage relateTags(@RequestBody Map<String, Object> tags, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return rentalCarService.relateTags(tags,getCurrentUser());
    }

    @PreAuthorize("hasAuthority('rental:w')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PutMapping(value = "/weight")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "租车管理")
    public ResponseMessage changeWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return commonService.changeWeight(weightModel,getCurrentUser(),RentalCarEntity.class);
    }

    @PreAuthorize("hasAuthority('rental:o')")
    @ApiOperation(value = "上下线状态变更", notes = "上下线状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "租车ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "上下线状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/changeStatus/{id}/{status}", method = RequestMethod.GET)
    public ResponseMessage changeStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) throws Exception {
        return rentalCarService.changeStatus(id,status,getCurrentUser().getUsername());
    }

    @PreAuthorize("hasAuthority('rental:e')")
    @ApiOperation(value = "租车审核", notes = "租车审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "租车信息ID", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态", required = true),
            @ApiImplicitParam(name = "msg", value = "审核意见")
    })
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ResponseMessage audit(@RequestParam String id, @RequestParam int auditStatus, String msg) throws Exception {
        return rentalCarService.examineRental(id, auditStatus, msg, getCurrentUser());
    }

    @PreAuthorize("hasAuthority('rental:b')")
    @ApiOperation(value = "数据绑定", notes = "数据绑定")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "数据绑定model", required = true, dataType = "DataBindModel")})
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage dataBind(@RequestBody @Valid DataBindModel model, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return rentalCarService.dataBind(getCurrentUser().getUsername(),model);
    }

    @ApiOperation(value = "租车名称是否重复", notes = "租车名称是否重复(status:1 表示不重复，0表示重复)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "租车名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "租车Id")
    })
    @RequestMapping(value = "/existByTitle", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/检查租车名称是否重复", module = "租车管理")
    public ResponseMessage existByTitle(@RequestParam String title, String id) throws Exception{
        return rentalCarService.findByTitleAndIdNot(title, id != null ? id : "-1");
    }

    @ApiOperation(value = "获取租车信息列表", notes = "获取租车信息列表")
    @RequestMapping(value = "/getRentalCarInfo", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('rental:r')")
    @OperationLog(value = "wtcp-bics/获取租车信息列表", operate = "r", module = "租车管理")
    public ResponseMessage getRentalCarInfo(String title) throws Exception {
        title = title == null? "":title;
        return rentalCarService.getRentalCarInfo(title.trim().toLowerCase());
    }

}
