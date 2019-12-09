package cn.com.wanwei.bic.controller.rpc;


import cn.com.wanwei.bic.entity.DestinationEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/rpc/destination")
@Api(value = "目的地", tags = "目的地")
public class RpcDestinationController {
    /**
     * 获取wtcp-bic目的地管理列表
     *
     * @param areaCodes 城市编码串,多个以逗号隔开
     * @return page
     */
    @ApiOperation(value = "根据区域编码获取目的地列表", notes = "根据区域编码获取目的地列表")
    @ApiImplicitParam(name = "areaCodes", value = "城市编码串", required = true)
    @GetMapping(value = "/destinations/getDestinationInfo")
    public ResponseMessage<List<DestinationEntity>> destinationList(@RequestParam String areaCodes){
        return null;
    }

}
