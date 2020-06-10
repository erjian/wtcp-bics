package cn.com.wanwei.bic.controller.rpc;

import cn.com.wanwei.bic.model.HotelInfoModel;
import cn.com.wanwei.bic.model.HotelListModel;
import cn.com.wanwei.bic.service.HotelService;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping("/rpc/hotel")
@Api(value = "酒店管理Feign接口", tags = "酒店管理Feign接口")
public class RpcHotelController {

    @Autowired
    private HotelService hotelService;

    @ApiOperation(value = "根据areaCode查询酒店下拉列表数据", notes = "根据areaCode查询酒店下拉列表数据")
    @GetMapping(value = "/findByAreaCode")
    @ApiImplicitParam(name = "areaCode", value = "区划编码", defaultValue = "")
    @OperationLog(value = "wtcp-bics/根据areaCode查询酒店下拉列表数据", operate = "r", module = "酒店管理")
    public ResponseMessage<List<HotelListModel>> findByAreaCode(@RequestParam(value = "areaCode") String areaCode) {
        return hotelService.findByAreaCode(areaCode);
    }

    @ApiOperation(value = "根据id查询酒店详情数据", notes = "根据id查询酒店详情数据")
    @GetMapping(value = "/detail")
    @ApiImplicitParam(name = "id", value = "酒店ID", defaultValue = "")
    @OperationLog(value = "wtcp-bics/根据id查询酒店详情数据", operate = "r", module = "酒店管理")
    public ResponseMessage<HotelInfoModel> findInfoById(@RequestParam(value = "id") String id) {
        return hotelService.findInfoById(id);
    }
}
