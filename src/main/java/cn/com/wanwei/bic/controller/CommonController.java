package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.model.BatchAuditModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/common")
@Api(value = "公共接口", tags = "公共接口")
public class CommonController extends BaseController {

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "信息批量审核&上线", notes = "信息批量审核&上线")
    @ApiImplicitParam(name = "batchAuditModel", value = "信息批量审核&上线Model", required = true, dataType = "BatchAuditModel")
    @OperationLog(value = "wtcp-bic/信息批量审核&上线", module = "信息批量审核&上线")
    @PreAuthorize("hasAuthority('commonInfo:be') or hasAuthority('commonInfo:bo')")
    @PutMapping("/batchChangeStatus")
    public ResponseMessage batchChangeStatus(@RequestBody BatchAuditModel batchAuditModel) throws Exception {
        Class clazz = SpringContextUtil.getBean(String.format("%sEntity", batchAuditModel.getClassPrefixName())).getClass();
        if (null != clazz) {
            return commonService.batchChangeStatus(batchAuditModel, getCurrentUser(), clazz);
        }
        return ResponseMessage.validFailResponse().setMsg("参数错误");
    }

}
