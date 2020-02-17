package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.model.DataType;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/catalogues")
@Api(value = "各类型资源信息C端接口", tags = "各类型资源信息C端接口")
public class InternalCataloguesController {

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "根据资源编码获取资源", notes = "根据资源编码获取资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "资源编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "资源名称", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "用逗号分隔的主键字符串", dataType = "String"),
    })
    @GetMapping("/getDataByType")
    public ResponseMessage getDataByType(@RequestParam String type, String name, String ids) throws Exception {
        return commonService.getDataByType(type, name, ids);
    }

    @ApiOperation(value = "获取资源类型", notes = "获取资源类型")
    @GetMapping("/getDataType")
    public ResponseMessage getDataType() {
        return ResponseMessage.defaultResponse().setData(DataType.list());
    }

}
