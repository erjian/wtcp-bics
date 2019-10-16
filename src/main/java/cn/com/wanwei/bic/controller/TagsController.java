package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.KbServiceFeign;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/tags")
@Api(value = "标签管理", tags = "标签管理相关接口")
public class TagsController extends BaseController {

    @Autowired
    private TagsService tagsService;

    @Autowired
    private KbServiceFeign kbServiceFeign;

    @ApiOperation(value = "根据关联ID获取景区标签信息", notes = "根据关联ID获取景区标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的景区ID", required = true)
    @PreAuthorize("hasAuthority('bictags:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID获取景区标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findScenicByPid", method = RequestMethod.GET)
    public ResponseMessage findScenicByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, ScenicTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取目的地标签信息", notes = "根据关联ID获取目的地标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的目的地ID", required = true)
    @PreAuthorize("hasAuthority('bictags:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID获取目的地标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findDestByPid", method = RequestMethod.GET)
    public ResponseMessage findDestByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, DestinationTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取休闲娱乐标签信息", notes = "根据关联ID获取休闲娱乐标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的休闲娱乐ID", required = true)
    @PreAuthorize("hasAuthority('bictags:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID获取休闲娱乐标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findEmByPid", method = RequestMethod.GET)
    public ResponseMessage findEmByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, EntertainmentTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取扩展信息的标签信息", notes = "根据关联ID获取扩展信息的标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的扩展信息的ID", required = true)
    @PreAuthorize("hasAuthority('bictags:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID获取扩展信息的标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findExtendByPid", method = RequestMethod.GET)
    public ResponseMessage findExtendByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, ExtendTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取周边标签信息", notes = "根据关联ID获取周边标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的周边ID", required = true)
    @PreAuthorize("hasAuthority('bictags:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID获取周边标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findPeripheryByPid", method = RequestMethod.GET)
    public ResponseMessage findPeripheryByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, PeripheryTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取POI标签信息", notes = "根据关联ID获取POI标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的POIID", required = true)
    @PreAuthorize("hasAuthority('bictags:r')")
    @OperationLog(value = "wtcp-bic/根据关联ID获取POI标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findPoiByPid", method = RequestMethod.GET)
    public ResponseMessage findPoiByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, PoiTagsEntity.class);
    }

    @ApiOperation(value = "根据feign接口获取标签数据", notes = "根据feign接口获取标签数据")
    @OperationLog(value = "wtcp-bic/根据feign接口获取标签数据", operate = "r", module = "标签管理")
    @RequestMapping(value = "/getTagsTree", method = RequestMethod.GET)
    public ResponseMessage getTagsTree() {
        return kbServiceFeign.getAllTreeNoRight(0L);
    }

}
