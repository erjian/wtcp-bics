package cn.com.wanwei.bic.controller.rpc;


import cn.com.wanwei.bic.service.DestinationService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/rpc/destination")
@Api(value = "目的地", tags = "目的地")
public class RpcDestinationController {

    @Autowired
    private DestinationService  destinationService;

    @ApiOperation(value = "根据区域编码/目的地名称/目的地ids串查询目的地信息",notes = "根据区域编码、目的地名称、目的地ids串，必须且只能传其中一个参数，例如：如果areaCodes不为空，则根据areaCodes查询目的地列表，依此类推")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCodes", value = "目的地编码(可为多个，编码之间用逗号隔开)", dataType = "String"),
            @ApiImplicitParam(name = "areaName", value = "目的地名称（单个名称模糊搜索）", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "目的地ids串（可为多个，id之间用逗号隔开）", dataType = "String")
    })
    @RequestMapping(value = "/getDestinationInfo", method = RequestMethod.GET)
    public ResponseMessage getDestinationInfo(@RequestParam(value="areaCodes",required=false) String areaCodes,
                                              @RequestParam(value="areaName",required=false) String areaName,
                                              @RequestParam(value="ids",required=false) String ids) {
        return destinationService.getDestinationInfo(areaCodes,areaName,ids);

    }

}
