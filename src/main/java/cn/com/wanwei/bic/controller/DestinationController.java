package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.DestinationEntity;
import cn.com.wanwei.bic.service.DestinationService;
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

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/10 9:23:23
 * @desc 目的地管理控制层
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/destination")
@Api(value = "目的地基础信息管理", tags = "目的地基础信息管理相关接口")
public class DestinationController extends BaseController {

    @Autowired
    private DestinationService destinationService;

    @ApiOperation(value = "目的地基础信息管理分页列表", notes = "目的地基础信息管理分页列表")
    @GetMapping(value = "/page")
    @PreAuthorize("hasAuthority('destination:r')")
    @OperationLog(value = "wtcp-bics/目的地基础信息管理分页列表", operate = "r", module = "目的地基础信息管理")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return destinationService.findByPage(page,size,getCurrentUser(),filter);
    }

    @ApiOperation(value = "目的地基础信息新增", notes = "目的地基础信息新增")
    @ApiImplicitParam(name = "destinationEntity", value = "目的地基础信息", required = true, dataType = "DestinationEntity")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('destination:c')")
    @OperationLog(value = "wtcp-bics/目的地基础信息新增", operate = "c", module = "目的地基础信息管理")
    public ResponseMessage save(@RequestBody DestinationEntity destinationEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return destinationService.save(destinationEntity,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "目的地基础信息编辑", notes = "目的地基础信息编辑")
    @ApiImplicitParam(name = "destinationEntity", value = "目的地基础信息", required = true, dataType = "DestinationEntity")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('destination:u')")
    @OperationLog(value = "wtcp-bics/目的地基础信息编辑", operate = "u", module = "目的地基础信息管理")
    public ResponseMessage edit(@PathVariable("id") String id, @RequestBody DestinationEntity destinationEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return destinationService.edit(id,destinationEntity,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "查询目的地基础信息详情", notes = "根据ID查询目的地基础信息详情")
    @ApiImplicitParam(name = "id", value = "目的地基础信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('destination:v')")
    @OperationLog(value = "wtcp-bics/根据id查询目的地基础信息详情", operate = "v", module = "目的地基础信息管理")
    public ResponseMessage detail(@PathVariable("id") String id) throws Exception{
        return destinationService.selectByPrimaryKey(id);
    }

    @ApiOperation(value = "删除目的地基础信息", notes = "根据ID删除目的地基础信息")
    @ApiImplicitParam(name = "id", value = "目的地基础信息ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('destination:d')")
    @OperationLog(value = "wtcp-bics/根据id删除目的地基础信息", operate = "d", module = "目的地基础信息管理")
    public ResponseMessage delete(@PathVariable("id") String id) throws Exception {
        return destinationService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "目的地基础信息权重修改", notes = "目的地基础信息权重修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "目的地基础信息ID", required = true),
            @ApiImplicitParam(name = "weight", value = "权重", required = true)
    })
    @RequestMapping(value = "/changeWeight/{id}}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('destination:q')")
    @OperationLog(value = "wtcp-bics/目的地基础信息权重修改", operate = "u", module = "目的地基础信息权重修改")
    public ResponseMessage changeWeight(@PathVariable("id") String id, @PathVariable("weight") Float weight) throws Exception {
        return destinationService.changeWeight(id,weight,getCurrentUser().getUsername());
    }

    @ApiOperation(value = "目的地信息审核", notes = "目的地信息审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "目的地基础信息ID", required = true),
            @ApiImplicitParam(name = "status", value = "状态", required = true)
    })
    @PostMapping(value = "/changeStatus")
    @PreAuthorize("hasAuthority('destination:a')")
    public ResponseMessage changeStatus(@PathVariable("id") String id, @PathVariable("status") Integer status  ,BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return destinationService.changeStatus(id, status ,getCurrentUser().getUsername(),0);
    }

    @ApiOperation(value = "目的地信息上线", notes = "目的地信息上线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "目的地基础信息ID", required = true),
            @ApiImplicitParam(name = "status", value = "状态", required = true)
    })
    @PostMapping(value = "/changeIssue" )
    @PreAuthorize("hasAuthority('destination:s')")
    public ResponseMessage changeIssue(@PathVariable("id") String id, @PathVariable("status") Integer status ,BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return destinationService.changeStatus(id, status,getCurrentUser().getUsername(),1);
    }

    @ApiOperation(value = "目的地名称重名校验", notes = "目的地名称重名校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "目的地信息ID"),
            @ApiImplicitParam(name = "regionFullName", value = "目的地名称")
    })
    @GetMapping(value = "/checkRegionFullName")
    public ResponseMessage checkRegionFullName(@RequestParam(value = "id",required = false) String id,
                                      @RequestParam(value = "regionFullName",required = false) String regionFullName){
        return destinationService.checkRegionFullName(id,regionFullName);
    }

}
