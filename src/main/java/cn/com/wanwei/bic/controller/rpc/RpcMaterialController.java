package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/rpc/material")
@Api(value = "素材信息Feign接口", tags = "素材信息Feign接口")
public class RpcMaterialController {

    @Autowired
    private MaterialService materialService;

    @ApiOperation(value = "获取素材列表", notes = "根据ids获取素材列表")
    @GetMapping(value = "/findByIds")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids",required = true,dataType = "String"),
            @ApiImplicitParam(name = "backType", value = "参数(1:全部返回，2：按照每个id返回)", defaultValue = "1",dataType = "Integer"),
    })
    @OperationLog(value = "wtcp-bics/获取素材列表", operate = "r", module = "素材管理")
    public ResponseMessage findByIds(@RequestParam("ids")String ids,
                                     @RequestParam(value = "backType", defaultValue = "1")Integer backType) throws Exception{
        return materialService.findByIds(ids, backType);
    }

}
