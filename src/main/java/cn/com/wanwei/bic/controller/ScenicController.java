package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
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
}

