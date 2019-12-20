package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.BusinessEntity;
import cn.com.wanwei.bic.entity.ResourceConfigEntity;
import cn.com.wanwei.bic.service.BusinessService;
import cn.com.wanwei.bic.service.ResourceConfigService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/resourceConfig")
@Api(value = "资源配置信息管理", tags = "资源配置信息管理相关接口")
public class ResourceConfigController extends BaseController {

    @Autowired
    private ResourceConfigService resourceConfigService;

    @ApiOperation(value = "根据code查询资源配置信息详情", notes = "根据code查询资源配置信息详情")
    @ApiImplicitParam(name = "code", value = "资源code", required = true)
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/根据code查询资源配置信息详情", operate = "v", module = "资源配置信息管理")
    public ResponseMessage detail(@PathVariable("code") String code) throws Exception {
        return resourceConfigService.selectByCode(code);
    }

    @ApiOperation(value = "资源配置信息新增", notes = "资源配置信息新增")
    @ApiImplicitParam(name = "resourceConfigEntity", value = "资源配置信息", required = true, dataType = "ResourceConfigEntity")
    @PostMapping(value = "/save")
    @OperationLog(value = "wtcp-bics/资源配置信息新增", operate = "c", module = "资源配置信息管理")
    public ResponseMessage save(@RequestBody ResourceConfigEntity resourceConfigEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        if(StringUtil.isEmpty(resourceConfigEntity.getId())){
            return resourceConfigService.save(resourceConfigEntity,getCurrentUser());
        }else{
            return resourceConfigService.edit(resourceConfigEntity.getId().trim(),resourceConfigEntity,getCurrentUser());
        }
    }

    /****** 辅助数据查询开始 ******/
    @ApiOperation(value = "查询表信息", notes = "查询表信息")
    @RequestMapping(value = "/tableInfo", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/查询表信息", operate = "v", module = "资源配置信息管理")
    public ResponseMessage tableInfo() throws Exception {
        return resourceConfigService.selectTableInfo();
    }

    @ApiOperation(value = "根据表名查询相应的字段信息", notes = "根据表名查询相应的字段信息")
    @RequestMapping(value = "/columnInfo", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/查询表信息", operate = "v", module = "资源配置信息管理")
    public ResponseMessage columnInfo(@RequestParam String tableName) throws Exception {
        return resourceConfigService.selectColumnInfo(tableName);
    }
    /****** 辅助数据查询结束 ******/
}

