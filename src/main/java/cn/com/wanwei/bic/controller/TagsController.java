package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.feign.KbServiceFeign;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.ProgressUtils;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private KbServiceFeign kbServiceFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProgressUtils progressUtils;

    @RequestMapping(value = "/test/progress", method = RequestMethod.GET)
    public ResponseMessage testProgress(@RequestParam String key){
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        try {
            String progressKey = key.concat("_test_progress");
            Object back = redisTemplate.opsForValue().get(progressKey);
            if(null == back){
                back = 0;
                progressUtils.setProgress(redisTemplate, progressKey);
            }
            responseMessage.setData(back);
        }catch (Exception e){
            responseMessage.setStatus(ResponseMessage.FAILED);
        }
        return responseMessage;
    }

    @ApiOperation(value = "根据关联ID获取景区标签信息", notes = "根据关联ID获取景区标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的景区ID", required = true)
    @OperationLog(value = "wtcp-bic/根据关联ID获取景区标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findScenicByPid", method = RequestMethod.GET)
    public ResponseMessage findScenicByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, ScenicTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取目的地标签信息", notes = "根据关联ID获取目的地标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的目的地ID", required = true)
    @OperationLog(value = "wtcp-bic/根据关联ID获取目的地标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findDestByPid", method = RequestMethod.GET)
    public ResponseMessage findDestByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, DestinationTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取休闲娱乐标签信息", notes = "根据关联ID获取休闲娱乐标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的休闲娱乐ID", required = true)
    @OperationLog(value = "wtcp-bic/根据关联ID获取休闲娱乐标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findEmByPid", method = RequestMethod.GET)
    public ResponseMessage findEmByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, EntertainmentTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取扩展信息的标签信息", notes = "根据关联ID获取扩展信息的标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的扩展信息的ID", required = true)
    @OperationLog(value = "wtcp-bic/根据关联ID获取扩展信息的标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findExtendByPid", method = RequestMethod.GET)
    public ResponseMessage findExtendByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, ExtendTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取周边标签信息", notes = "根据关联ID获取周边标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的周边ID", required = true)
    @OperationLog(value = "wtcp-bic/根据关联ID获取周边标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findPeripheryByPid", method = RequestMethod.GET)
    public ResponseMessage findPeripheryByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, PeripheryTagsEntity.class);
    }

    @ApiOperation(value = "根据关联ID获取POI标签信息", notes = "根据关联ID获取POI标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的POIID", required = true)
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

    @ApiOperation(value = "根据feign接口获取标签数据", notes = "根据feign接口获取标签数据")
    @ApiImplicitParam(name = "kind", value = "标签类型kind", required = true)
    @OperationLog(value = "wtcp-bic/根据feign接口获取标签数据", operate = "r", module = "标签管理")
    @RequestMapping(value = "/getTagsByKind", method = RequestMethod.GET)
    public ResponseMessage getTagsByKind(@RequestParam Integer kind) {
        return kbServiceFeign.getTagsByKind(kind);
    }

    @ApiOperation(value = "根据关联ID获取旅行社标签信息", notes = "根据关联ID获取旅行社标签信息")
    @ApiImplicitParam(name = "principalId", value = "关联的旅行社ID", required = true)
    @OperationLog(value = "wtcp-bic/根据关联ID获取旅行社标签信息", operate = "r", module = "标签管理")
    @RequestMapping(value = "/findTravelAgentByPid", method = RequestMethod.GET)
    public ResponseMessage findTravelAgentByPid(@RequestParam String principalId) {
        return tagsService.findByPrincipalId(principalId, TravelAgentTagsEntity.class);
    }

}
