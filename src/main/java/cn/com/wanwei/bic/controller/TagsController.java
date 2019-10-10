package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.DestinationTagsEntity;
import cn.com.wanwei.bic.entity.ScenicTagsEntity;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/tags")
@Api(value = "标签管理", tags = "标签管理相关接口")
public class TagsController extends BaseController {

    @Autowired
    private TagsService tagsService;

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

}
