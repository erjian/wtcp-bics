package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/material")
@Api(value = "素材管理", tags = "素材管理相关接口")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialService materialService;

    @ApiOperation(value = "根据关联ID获取素材信息", notes = "根据关联ID获取素材信息")
    @ApiImplicitParam(name = "principalId", value = "关联ID", required = true)
    @RequestMapping(value = "/findByPrincipalId", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('material:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID获取素材信息", operate = "r", module = "素材管理")
    public ResponseMessage findByPrincipalId(@RequestParam Long principalId) {
        return ResponseMessage.defaultResponse().setData(materialService.findByPrincipalId(principalId));
    }

    @ApiOperation(value = "根据关联ID及素材类型获取素材信息", notes = "根据关联ID及素材类型获取素材信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "principalId", value = "关联ID", required = true),
            @ApiImplicitParam(name = "fileType", value = "素材类型（image：图片，audio：音频，video：视频， file: 文档）", required = true)
    })
    @RequestMapping(value = "/findByPidAndType", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('material:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID及素材类型获取素材信息", operate = "r", module = "素材管理")
    public ResponseMessage findByPidAndType(@RequestParam Long principalId, @RequestParam String fileType) {
        return ResponseMessage.defaultResponse().setData(materialService.findByPidAndType(principalId, fileType));
    }

    @ApiOperation(value = "根据关联ID及素材标识获取素材信息", notes = "根据关联ID及素材标识获取素材信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "principalId", value = "关联ID", required = true),
            @ApiImplicitParam(name = "identify", value = "素材标识（1：标题图片，2：亮点图片，3：标题且亮点图片）", required = true)
    })
    @RequestMapping(value = "/findByPidAndIdentify", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('material:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID及素材标识获取素材信息", operate = "r", module = "素材管理")
    public ResponseMessage findByPidAndIdentify(@RequestParam Long principalId, @RequestParam Integer identify) {
        return ResponseMessage.defaultResponse().setData(materialService.findByPidAndIdentify(principalId, identify));
    }

}
