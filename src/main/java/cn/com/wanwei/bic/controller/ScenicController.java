package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.ScenicService;
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
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/scenic")
@Api(value = "景区管理", tags = "景区管理相关接口")
public class ScenicController extends BaseController {

    @Autowired
    private ScenicService scenicService;

    @ApiOperation(value = "景区管理分页列表", notes = "景区管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('scenic:r')")
    @OperationLog(value = "wtcp-bics/景区管理分页列表", operate = "r", module = "景区管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        EscapeCharUtils.escape(filter, "title", "subTitle", "areaName");
        return scenicService.findByPage(page,size,getCurrentUser(),filter);
    }

    @ApiOperation(value = "查询景区详情", notes = "根据ID查询景区详情")
    @ApiImplicitParam(name = "id", value = "景区ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('scenic:v')")
    @OperationLog(value = "wtcp-bics/根据id查询景区详情", operate = "v", module = "景区管理")
    public ResponseMessage detail(@PathVariable("id") String id) throws Exception {
        ScenicEntity scenicEntity = scenicService.selectByPrimaryKey(id);
        if (scenicEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(scenicEntity);
    }

    @ApiOperation(value = "删除景区", notes = "根据ID删除景区")
    @ApiImplicitParam(name = "id", value = "景区ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('scenic:d')")
    @OperationLog(value = "wtcp-bics/根据id删除景区", operate = "d", module = "景区管理")
    public ResponseMessage delete(@PathVariable("id") String id) throws Exception {
        return scenicService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "景区新增", notes = "景区新增")
    @ApiImplicitParam(name = "scenicModel", value = "景区model", required = true, dataType = "EntityTagsModel")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('scenic:c')")
    @OperationLog(value = "wtcp-bics/景区新增", operate = "c", module = "景区管理")
    public ResponseMessage save(@RequestBody EntityTagsModel<ScenicEntity> scenicModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return scenicService.save(scenicModel,getCurrentUser(), ruleId, appCode);
    }

    @ApiOperation(value = "景区编辑", notes = "景区编辑")
    @ApiImplicitParam(name = "scenicModel", value = "景区", required = true, dataType = "EntityTagsModel")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('scenic:u')")
    @OperationLog(value = "wtcp-bics/景区编辑", operate = "u", module = "景区管理")
    public ResponseMessage edit(@PathVariable("id") String id, @RequestBody EntityTagsModel<ScenicEntity> scenicModel, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return scenicService.edit(id,scenicModel,getCurrentUser());
    }

    @ApiOperation(value = "景区基础信息关联标签", notes = "景区基础信息关联标签")
    @ApiImplicitParam(name = "tags", value = "关联标签", required = true, dataType = "Map")
    @RequestMapping(value = "/relateTags", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('scenic:rt')")
    @OperationLog(value = "wtcp-bics/景区基础信息关联标签", operate = "u", module = "景区管理")
    public ResponseMessage relateTags(@RequestBody Map<String, Object> tags, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return scenicService.relateTags(tags,getCurrentUser());
    }

    @PreAuthorize("hasAuthority('scenic:w')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weightModel", value = "排序model", required = true, dataType = "WeightModel")
    })
    @PutMapping(value = "/weight")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "景区基础信息管理")
    public ResponseMessage changeWeight(@RequestBody @Valid WeightModel weightModel, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return scenicService.changeWeight(weightModel,getCurrentUser());
    }

    @PreAuthorize("hasAuthority('scenic:o')")
    @ApiOperation(value = "上下线状态变更", notes = "上下线状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "景区ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "上下线状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/changeStatus/{id}/{status}", method = RequestMethod.GET)
    public ResponseMessage changeStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) throws Exception {
        return scenicService.changeStatus(id,status,getCurrentUser().getUsername());
    }

    @PreAuthorize("hasAuthority('scenic:e')")
    @ApiOperation(value = "景区审核", notes = "景区审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "景区信息ID", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态", required = true),
            @ApiImplicitParam(name = "msg", value = "审核意见")
    })
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ResponseMessage audit(@RequestParam String id, @RequestParam int auditStatus, String msg) throws Exception {
        return scenicService.examineScenic(id, auditStatus, msg, getCurrentUser());
    }

    @PreAuthorize("hasAuthority('scenic:b')")
    @ApiOperation(value = "数据绑定", notes = "数据绑定")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "数据绑定model", required = true, dataType = "DataBindModel")})
    @RequestMapping(value = "/dataBind", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseMessage dataBind(@RequestBody @Valid DataBindModel model, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return scenicService.dataBind(getCurrentUser().getUsername(),model);
    }

    @ApiOperation(value = "获取景区列表", notes = "获取景区列表")
    @RequestMapping(value = "/getScenicList", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('scenic:r')")
    @OperationLog(value = "wtcp-bics/获取景区列表", operate = "r", module = "景区管理")
    public ResponseMessage getScenicList(String title) throws Exception {
        title = title == null? "":title;
        return scenicService.getScenicInfo(title.trim().toLowerCase());
    }

    @ApiOperation(value = "景区名称是否重复", notes = "景区名称是否重复(status:1 表示不重复，0表示重复)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "景区名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "景区id")
    })
    @RequestMapping(value = "/existByTitle", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/检查景区名称是否重复", operate = "", module = "景区基础信息管理", frontCode = "", resource = "")
    public ResponseMessage existByTitle(@RequestParam String title, String id) {
        return scenicService.findByTitleAndIdNot(title, id != null ? id : "-1");
    }
}

