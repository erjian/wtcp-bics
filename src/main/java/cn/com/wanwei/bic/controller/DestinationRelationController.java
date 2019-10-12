package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.DestinationRelationEntity;
import cn.com.wanwei.bic.service.DestinationRelationService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/11 11:27:27
 * @desc 目的地关联信息控制层
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/destRelation")
@Api(value = "目的地基础信息管理", tags = "目的地基础信息管理相关接口")
public class DestinationRelationController extends BaseController{

    @Autowired
    private DestinationRelationService destinationRelationService;

    @ApiOperation(value = "根据目的地ID获取关联信息", notes = "根据目的地ID获取关联信息")
    @ApiImplicitParam(name = "id", value = "目的地id", required = true)
    @PreAuthorize("hasAuthority('destRelation:r')")
    @OperationLog(value = "wtcp-bic/根据目的地ID获取关联信息", operate = "r", module = "目的地管理")
    @RequestMapping(value = "/findPrincipalByDestinationId", method = RequestMethod.GET)
    public ResponseMessage findPrincipalByDestinationId(@RequestParam String id) throws Exception{
        return destinationRelationService.findPrincipalByDestinationId(id, DestinationRelationEntity.class);
    }

    @ApiOperation(value = "目的地关联信息新增", notes = "目的地关联信息新增")
    @ApiImplicitParam(name = "destinationRelationEntity", value = "目的地关联信息", required = true, dataType = "DestinationRelationEntity")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('destRelation:c')")
    @OperationLog(value = "wtcp-bics/目的地关联信息新增", operate = "c", module = "目的地基础信息管理")
    public ResponseMessage save(@RequestBody DestinationRelationEntity destinationRelationEntity, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseMessage.validFailResponse().setMsg(bindingResult.getAllErrors());
        }
        return destinationRelationService.save(destinationRelationEntity,getCurrentUser().getUsername());
    }

}
