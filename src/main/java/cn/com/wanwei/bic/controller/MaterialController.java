package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.bic.model.MaterialModel;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/material")
@Api(value = "素材管理", tags = "素材管理相关接口")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialModel materialModel;

    @ApiOperation(value = "根据主键获取素材信息", notes = "根据主键获取素材信息")
    @ApiImplicitParam(name = "id", value = "素材主键", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('material:r')")
    @OperationLog(value = "wtcp-bic/根据主键获取素材信息", operate = "r", module = "素材管理")
    public ResponseMessage detail(@PathVariable String id) {
        return ResponseMessage.defaultResponse().setData(materialService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "根据主键和关联ID删除素材信息", notes = "根据主键和关联ID删除素材信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "principalId", value = "关联信息ID", required = true),
            @ApiImplicitParam(name = "id", value = "素材主键", required = true)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('material:d')")
    @OperationLog(value = "wtcp-bic/根据主键和关联ID删除素材信息", operate = "d", module = "素材管理")
    public ResponseMessage deleteOneByPidAndId(@RequestParam String principalId, @PathVariable String id) {
        return ResponseMessage.defaultResponse().setData(materialService.deleteOneByPidAndId(principalId, id));
    }

    @ApiOperation(value = "根据关联ID获取素材信息", notes = "根据关联ID获取素材信息")
    @ApiImplicitParam(name = "principalId", value = "关联ID", required = true)
    @RequestMapping(value = "/findByPrincipalId", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('material:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID获取素材信息", operate = "r", module = "素材管理")
    public ResponseMessage findByPrincipalId(@RequestParam String principalId) {
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
    public ResponseMessage findByPidAndType(@RequestParam String principalId, @RequestParam String fileType) {
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
    public ResponseMessage findByPidAndIdentify(@RequestParam String principalId, @RequestParam String identify) {
        return ResponseMessage.defaultResponse().setData(materialService.findByPidAndIdentify(principalId, identify));
    }

    @ApiOperation(value = "根据关联ID和主键更新图片素材标识", notes = "根据关联ID和主键更新图片素材标识")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "principalId", value = "关联ID", required = true),
            @ApiImplicitParam(name = "id", value = "素材主键", required = true),
            @ApiImplicitParam(name = "identify", value = "素材标识（1：标题图片，2：亮点图片，3：标题且亮点图片）", required = true)
    })
    @RequestMapping(value = "/updateImgIdentify", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('material:u')")
    @OperationLog(value = "wtcp-bic/根据关联ID和主键更新图片素材标识", operate = "u", module = "素材管理")
    public ResponseMessage updateIdentify(@RequestParam String principalId, @RequestParam String id, @RequestParam String identify) throws Exception {
        return ResponseMessage.defaultResponse().setData(materialService.updateIdentify(principalId, id, identify, getCurrentUser()));
    }

    @ApiOperation(value = "根据关联ID保存素材", notes = "根据关联ID保存素材")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "principalId", value = "关联ID", required = true),
            @ApiImplicitParam(name = "materialList", value = "素材主键", required = true)
    })
    @RequestMapping(value = "/batchInsert", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('material:u')")
    @OperationLog(value = "batchInsert/根据关联ID保存素材", operate = "批量保存素材", module = "素材管理")
    public ResponseMessage batchInsert(@RequestParam String principalId, @RequestBody List<MaterialEntity> materialList) throws Exception {
        return ResponseMessage.defaultResponse().setData(materialService.batchInsert(principalId, materialList, getCurrentUser()));
    }


    @ApiOperation(value = "素材类型", notes = "素材类型")
    @GetMapping(value = "/getMaterialType")
    @OperationLog(value = "wtcp-bics/素材类型", operate = "r", module = "素材类型")
    public ResponseMessage getMaterialType(){
        return ResponseMessage.defaultResponse().setData(materialService.getMaterialType());
    }

}
