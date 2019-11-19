/**
 * 该源代码文件 OpenPoiController.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年11月18日15:15:52
 */
package cn.com.wanwei.bic.controller.open;

import cn.com.wanwei.bic.controller.BaseController;
import cn.com.wanwei.bic.service.PoiService;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * wtcp-bics - OpenPoiController
 * C端poi接口控制类
 */
@RestController
@RefreshScope
@RequestMapping("/public/poi")
@Api(value = "C端POI管理", tags = "C端POI管理相关接口")
public class OpenPoiController extends BaseController {
	
	@Autowired
	private PoiService poiService;

	@ApiOperation(value = "根据景区ID获取POI列表", notes = "根据景区ID获取POI列表")
	@ApiImplicitParams({@ApiImplicitParam(name = "principalId", value = "景区关联ID", required = true, dataType = "String"),
			@ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "String")})
	@RequestMapping(value = "/getList/{id}", method = RequestMethod.GET)
	public ResponseMessage getList(@PathVariable("id") String principalId,String type) {
		return poiService.getList(principalId, type);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "C端根据ID获取POI详情", notes = "C端根据ID获取POI详情")
    @ApiImplicitParam(name = "id", value = "POI信息ID", required = true, dataType = "String")
	@RequestMapping(value = "/getOne/{id}", method = RequestMethod.GET)
	public ResponseMessage getOne(@PathVariable("id") String id) {
		return poiService.getOne(id);
	}

}
