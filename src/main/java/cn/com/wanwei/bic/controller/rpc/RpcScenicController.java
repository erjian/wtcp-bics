package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/rpc/scenic")
@Api(value = "景区信息Feign接口", tags = "景区信息Feign接口")
public class RpcScenicController extends BaseController {

    @Autowired
    private ScenicService scenicService;

    @ApiOperation(value = "根据景区名称和上线状态查询景区信息的feign接口", notes = "根据景区名称和上线状态查询景区信息的feign接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "景区名称"),
            @ApiImplicitParam(name = "status", value = "上下线（9 上线，1 下线）"),
            @ApiImplicitParam(name = "category", value = "类别：景区(125001001) OR 示范村(125001002)")
    })
    @RequestMapping(value = "/getScenicInfo", method = RequestMethod.GET)
    @OperationLog(value = "wtcp-bics/根据景区名称和上线状态查询景区信息的feign接口", operate = "v", module = "景区管理")
    public ResponseMessage getScenicInfo(String title, Integer status, String category) throws Exception {
        return scenicService.getScenicInfo(StringUtils.isEmpty(title) ? "" : title.trim().toLowerCase(), status, category);
    }

    // ----------------------------------以下接口为自驾游提供-----------------------------------------

    @ApiOperation(value = "获取景区列表", notes = "根据区域获取景区列表(ids != null时，为不包含ids的信息)")
    @GetMapping(value = "/findByAreaCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页号", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10"),
            @ApiImplicitParam(name = "regionFullCode", value = "区域编码", required = true, dataType = "String")
    })
    @OperationLog(value = "wtcp-bics/获取景区列表", operate = "r", module = "景区管理")
    public ResponseMessage findByAreaCode(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                          HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        return scenicService.scenicPageNew(page, size, filter);
    }

    @ApiOperation(value = "根据id集合获取景区列表，只返回上线的数据", notes = "根据id集合获取景区列表，只返回上线的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "多个ID已英文逗号分隔", required = true),
            @ApiImplicitParam(name = "status", value = "状态，默认返回上线的，-1为不限制", defaultValue = "9")
    })
    @GetMapping("/findListByIds")
    public ResponseMessage findListByIds(@RequestParam String ids, @RequestParam(required = false, defaultValue = "9") Integer status) throws Exception {
        return scenicService.findListByIds(ids, status);
    }


    // ----------------------------------以下接口为导游导览提供-----------------------------------------

    @ApiOperation(value = "获取景区分页列表", notes = "获取景区分页列表，排序方式及字段可指定，" +
            "数据会根据用户数据权限过滤，调用时请将access_token作为参数传入" +
            "可根据类型category，级别level，名称title获取数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页号", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10")
    })
    @OperationLog(value = "wtcp-bic/获取景区分页列表", operate = "获取景区分页列表", module = "景区管理")
    @GetMapping(value = "/page")
    public ResponseMessage findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) throws Exception {
        Map<String, Object> filter = RequestUtil.getParameters(request);
        // 只返回上线的数据
        String statusKey = "status";
        if (!filter.containsKey(statusKey) || null == filter.get(statusKey)) {
            filter.put("status", 9);
        }
        return scenicService.findByPageForFeign(page, size, filter);
    }

    @ApiOperation(value = "获取景区详细信息", notes = "获取景区详细信息")
    @ApiImplicitParam(name = "id", value = "景区主键", required = true)
    @OperationLog(value = "wtcp-bic/获取景区详细信息", operate = "详情", module = "景区管理")
    @GetMapping(value = "/{id}")
    public ResponseMessage detail(@PathVariable("id") String id) throws Exception {
        return scenicService.getOne(id);
    }

    // ----------------------------------以下接口为景区电商提供-----------------------------------------

    @ApiOperation(value = "根据所属机构获取一条景区详情及其标签", notes = "根据景区所属机构编码获取一条景区详情和关联标签数据feign接口")
    @GetMapping(value = "/findByDeptCode")
    @ApiImplicitParam(name = "deptCode", value = "机构编码", required = true, dataType = "String")
    @OperationLog(value = "wtcp-bics/获取一条景区详情及其标签", operate = "r", module = "景区管理")
    public ResponseMessage findByDeptCode(@RequestParam String deptCode) throws Exception {
        return scenicService.findByDeptCode(deptCode);
    }


}

