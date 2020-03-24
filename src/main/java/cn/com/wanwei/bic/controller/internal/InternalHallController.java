package cn.com.wanwei.bic.controller.internal;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.bic.entity.VenueEntity;
import cn.com.wanwei.bic.service.HallService;
import cn.com.wanwei.bic.service.VenueService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/hall")
@SuppressWarnings("all")
@Api(value = "C端场馆厅室", tags = "C端场馆厅室管理相关接口")
public class InternalHallController {

    @Autowired
    private HallService hallService;

    @Autowired
    private VenueService venueService;

    @ApiOperation(value = "C端场馆厅室详情", notes = "C端根据ID查询场馆厅室详情")
    @ApiImplicitParam(name = "id", value = "场馆厅室ID", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseMessage detail(@PathVariable("id") String id) throws Exception {
        return ResponseMessage.defaultResponse().setData(hallService.findById(id));
    }

    @ApiOperation(value = "C端场馆厅室分页列表", notes = "C端场馆厅室分页列表")
    @GetMapping(value = "/page")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        EscapeCharUtils.escape(filter, "title");
        PageInfo<HallEntity> entities = hallService.findByPageWithDeptCode(page, size, filter);
        for(HallEntity entity:entities.getContent()){
            VenueEntity venueEntity = venueService.selectByPrimaryKey(entity.getVenueId());
            entity.setVenueName(venueEntity.getTitle());
        }
        return ResponseMessage.defaultResponse().setData(entities);
    }

}
