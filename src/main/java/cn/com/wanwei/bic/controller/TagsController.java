package cn.com.wanwei.bic.controller;

import cn.com.wanwei.bic.entity.BaseTagsEntity;
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseMessage test() {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        BaseTagsEntity entity = tagsService.selectByPrimaryKey("1", ScenicTagsEntity.class);
        return responseMessage.setData(entity);
    }

}
