package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
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
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/scenic")
@Api(value = "景区基础信息管理", tags = "景区基础信息管理相关接口")
public class ScenicController extends BaseController {

    @Autowired
    private ScenicService scenicService;

    @ApiOperation(value = "景区基础信息管理分页列表", notes = "景区基础信息管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('scenic:r')")
    @OperationLog(value = "wtcp-bics/景区基础信息管理分页列表", operate = "r", module = "景区基础信息管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return scenicService.findByPage(page,size,getCurrentUser(),filter);
    }

    @ApiOperation(value = "查询景区基础信息详情", notes = "根据ID查询景区基础信息详情")
    @ApiImplicitParam(name = "id", value = "景区基础信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('scenic:v')")
    @OperationLog(value = "wtcp-bics/根据id查询景区基础信息详情", operate = "v", module = "景区基础信息管理")
    public ResponseMessage detail(@PathVariable("id") Long id) throws Exception {
        ScenicEntity scenicEntity = scenicService.selectByPrimaryKey(id);
        if (scenicEntity == null) {
            return ResponseMessage.validFailResponse().setMsg("数据不存在");
        }
        return ResponseMessage.defaultResponse().setData(scenicEntity);
    }

    @ApiOperation(value = "删除景区基础信息", notes = "根据ID删除景区基础信息")
    @ApiImplicitParam(name = "id", value = "景区基础信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('scenic:d')")
    @OperationLog(value = "wtcp-bics/根据id删除景区基础信息", operate = "d", module = "景区基础信息管理")
    public ResponseMessage delete(@PathVariable("id") Long id) throws Exception {
        return scenicService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "景区基础信息新增", notes = "景区基础信息新增")
    @ApiImplicitParam(name = "scenicEntity", value = "景区基础信息", required = true, dataType = "ScenicEntity")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('scenic:c')")
    @OperationLog(value = "wtcp-bics/景区基础信息新增", operate = "c", module = "景区基础信息管理")
    public ResponseMessage save(@RequestBody ScenicEntity scenicEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return scenicService.save(scenicEntity,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "景区基础信息编辑", notes = "景区基础信息编辑")
    @ApiImplicitParam(name = "scenicEntity", value = "景区基础信息", required = true, dataType = "ScenicEntity")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('scenic:u')")
    @OperationLog(value = "wtcp-bics/景区基础信息编辑", operate = "u", module = "景区基础信息管理")
    public ResponseMessage edit(@PathVariable("id") Long id, @RequestBody ScenicEntity scenicEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return scenicService.edit(id,scenicEntity,getCurrentUser().getUsername());
    }

    @PreAuthorize("hasAuthority('scenic:w')")
    @ApiOperation(value = "景区基础信息权重修改", notes = "景区基础信息权重修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "景区基础信息ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "weightNum", value = "权重", required = true, dataType = "String")
    })
    @RequestMapping(value = "/changeWeight/{id}/{sortNum}", method = RequestMethod.GET)
    public ResponseMessage changeWeight(@PathVariable("id") Long id, @PathVariable("sortNum") Float weightNum) throws Exception {
        return scenicService.changeWeight(id,weightNum,getCurrentUser().getUsername());
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
}

