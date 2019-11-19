package cn.com.wanwei.bic.controller.open;


import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.ExtendService;
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
@RequestMapping("/public/extend")
@Api(value = "C端扩展信息管理", tags = "C端扩展信息管理相关接口")
public class OpenExtendController extends BaseController {

    @Autowired
    private ExtendService extendService;

    @ApiOperation(value = "获取扩展信息列表", notes = "获取扩展信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "principalId", value = "关联信息ID", required = true),
            @ApiImplicitParam(name = "type", value = "扩展信息类型（1：营地特色，2：营地设施,3：菜品，4：特色项目）" )
    })
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public ResponseMessage getList(@RequestParam(value = "principalId") String principalId,@RequestParam(value = "type",required = false) Integer type){
        return extendService.getList(principalId,type);
    }

    @ApiOperation(value = "查询扩展信息详情", notes = "根据ID查询扩展信息详情")
    @ApiImplicitParam(name = "id", value = "扩展信息ID", required = true)
    @RequestMapping(value = "/getExtendInfo/{id}", method = RequestMethod.GET)
    public ResponseMessage getExtendInfo(@PathVariable("id") String id) throws Exception{
        return extendService.selectByPrimaryKey(id);
    }
}
