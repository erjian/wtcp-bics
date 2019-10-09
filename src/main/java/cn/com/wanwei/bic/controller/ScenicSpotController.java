package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.ScenicSpotEntity;
import cn.com.wanwei.bic.service.ScenicSpotService;
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
import java.util.Map;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/spot")
@Api(value = "景点管理", tags = "景点管理相关接口")
public class ScenicSpotController extends  BaseController{

    @Autowired
    private ScenicSpotService scenicSpotService;

    @ApiOperation(value = "景点管理分页列表",notes = "景点管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('spot:r')")
    @OperationLog(value = "wtcp-bics/景点管理分页列表", operate = "r", module = "景点管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request){
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return scenicSpotService.findByPage(page,size,filter);
    }

    @ApiOperation(value = "查询景点信息", notes = "根据ID查询景点信息")
    @ApiImplicitParam(name = "id", value = "景点信息ID", required = true)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('spot:r')")
    @OperationLog(value = "wtcp-bics/查询景点信息", operate = "r", module = "景点管理")
    public ResponseMessage find(@PathVariable("id") Long id) {
        return scenicSpotService.find(id);
    }

    @ApiOperation(value = "新增景点信息", notes = "新增景点信息")
    @ApiImplicitParam(name = "scenicSpotEntity", value = "景点管理实体",dataType="ScenicSpotEntity", required = true)
    @PostMapping
    @PreAuthorize("hasAuthority('spot:c')")
    @OperationLog(value = "wtcp-bics/新增景点信息", operate = "c", module = "景点管理")
    public ResponseMessage create(@RequestBody ScenicSpotEntity scenicSpotEntity, BindingResult bindingResult) throws Exception {
       if(bindingResult.hasErrors()){
           return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
       }
        return scenicSpotService.create(scenicSpotEntity,getCurrentUser());
    }

    @ApiOperation(value = "编辑景点信息", notes = "编辑景点信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "景点信息ID", required = true),
        @ApiImplicitParam(name = "scenicSpotEntity", value = "景点管理实体",dataType="ScenicSpotEntity", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('spot:u')")
    @OperationLog(value = "wtcp-bics/编辑景点信息", operate = "u", module = "景点管理")
    public ResponseMessage create(@PathVariable(value = "id") Long id, @RequestBody ScenicSpotEntity scenicSpotEntity, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return scenicSpotService.update(id,scenicSpotEntity,getCurrentUser());
    }

    @ApiOperation(value = "删除景点信息", notes = "删除景点信息")
    @ApiImplicitParam(name = "id", value = "景点信息ID", required = true)
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('spot:d')")
    @OperationLog(value = "wtcp-bics/删除景点信息", operate = "d", module = "景点管理")
    public ResponseMessage delete(@PathVariable(value = "id") Long id){
        return scenicSpotService.delete(id);
    }

    @ApiOperation(value = "权重更改", notes = "权重更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "景点信息ID", required = true),
            @ApiImplicitParam(name = "weight", value = "权重", required = true)
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('spot:q')")
    @OperationLog(value = "wtcp-bics/权重更改", operate = "u", module = "景点管理")
    public ResponseMessage goWeight(@PathVariable(value = "id") Long id,@RequestParam Float weight) throws Exception {
        return scenicSpotService.goWeight(id,weight,getCurrentUser());
    }
}
