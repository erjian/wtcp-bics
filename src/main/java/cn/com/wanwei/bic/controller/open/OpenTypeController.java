package cn.com.wanwei.bic.controller.open;

import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/type")
@Api(value = "C端数据类型管理", tags = "C端数据类型管理相关接口")
public class OpenTypeController {

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @ApiOperation(value = "根据分类编码获取数据类型", notes = "根据分类编码获取数据类型")
    @ApiImplicitParam(name = "code", value = "分类编码", required = true)
    @RequestMapping(value = "/findByCode", method = RequestMethod.GET)
    public ResponseMessage findByCode(@RequestParam String code) {
         ResponseMessage responseMessage=coderServiceFeign.findByCode(code);
         return responseMessage;
    }
}
