package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.entity.PeripheryEntity;
import cn.com.wanwei.bic.service.CateRelationService;
import cn.com.wanwei.bic.service.PeripheryService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/rpc/periphery")
@Api(value = "周边管理Feign接口", tags = "周边管理Feign接口")
public class RpcPeripheryController {

    @Autowired
    private PeripheryService peripheryService;

    @Autowired
    private CateRelationService cateRelationService;

    @ApiOperation(value = "获取周边详细信息", notes = "获取周边详细信息")
    @ApiImplicitParam(name = "id", value = "Id", required = true)
    @GetMapping(value = "/{id}")
    @OperationLog(value = "wtcp-bics/查询周边管理信息", operate = "r", module = "周边管理")
    public ResponseMessage detail(@PathVariable("id") String id) {
        return peripheryService.find(id);
    }

    @ApiOperation(value = "根据餐饮企业获取关联的美食信息", notes = "根据餐饮企业获取关联的美食信息")
    @ApiImplicitParam(name = "cateringId", value = "餐饮企业ID", required = true)
    @RequestMapping(value = "/findByCateringId", method = RequestMethod.GET)
    public ResponseMessage findByCateringId(@RequestParam("cateringId") String cateringId, HttpServletRequest request) {
        List<PeripheryEntity> peripheryList = getPeripheryList(1, cateringId, request);
        return ResponseMessage.defaultResponse().setData(peripheryList);
    }

    @ApiOperation(value = "根据美食获取餐饮企业信息", notes = "根据美食获取餐饮企业信息")
    @ApiImplicitParam(name = "cateId", value = "美食ID", required = true)
    @RequestMapping(value = "/findByCateId", method = RequestMethod.GET)
    public ResponseMessage findByCateId(@RequestParam("cateId") String cateId, HttpServletRequest request) {
        List<PeripheryEntity> peripheryList = getPeripheryList(2, cateId, request);
        return ResponseMessage.defaultResponse().setData(peripheryList);
    }

    private List<PeripheryEntity> getPeripheryList(Integer type, String id, HttpServletRequest request) {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        List<String> ids = Lists.newArrayList();
        if (type == 1) {
            ids = cateRelationService.findByCateringId(id);
        }
        if (type == 2) {
            ids = cateRelationService.findByCateId(id);
        }
        filter.put("ids", ids);
        List<PeripheryEntity> peripheryList = peripheryService.findByIds(filter);
        return peripheryList;
    }

}
