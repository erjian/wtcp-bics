package cn.com.wanwei.bic.feign;

import cn.com.wanwei.common.config.rest.OAuth2FeignConfiguration;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${wtcp.coder.service-name}", configuration = OAuth2FeignConfiguration.class, fallback = CoderServiceFeign.CoderServiceFeignHystrix.class)
public interface CoderServiceFeign {

    @ApiOperation(value = "获取地区编码", notes = "根据父编码获取地区编码列表")
    @RequestMapping(value = "/area/list", method = RequestMethod.GET)
    ResponseMessage areaList(@RequestParam(value = "pcode") String pcode);

    @ApiOperation(value = "获取地区编码详细信息", notes = "根据编码获取地区编码详细信息")
    @RequestMapping(value = "/area/{code}", method = RequestMethod.GET)
    ResponseMessage areaDetail(@RequestParam(value = "code") String code);

    @GetMapping(value = "/coders/generate/snow/{appCode}")
    ResponseMessage buildSerialNum(@PathVariable("appCode") Integer appCode);

    @GetMapping(value = "/coders/generate/rule/{id}")
    ResponseMessage buildSerialByCode(@PathVariable("id") Long id,
                                @RequestParam(value = "appCode") Integer appCode,
                                @RequestParam(value = "areaCode") String areaCode);

    @GetMapping(value = "/dictionary/getByCode")
    ResponseMessage getByCode(@RequestParam(value = "code") String code);

    @GetMapping(value = "/rpc/dict/findByCode")
    ResponseMessage findByCode(@RequestParam(value = "code") String code);

    @Component
    class CoderServiceFeignHystrix implements CoderServiceFeign {

        @Override
        public ResponseMessage areaList(String pcode) {
            return ResponseMessage.defaultFallBack();
        }

        @Override
        public ResponseMessage areaDetail(String code) {
            return ResponseMessage.defaultFallBack();
        }

        @Override
        public ResponseMessage buildSerialNum(Integer appCode) {
            return ResponseMessage.defaultFallBack();
        }

        @Override
        public ResponseMessage buildSerialByCode(Long id, Integer appCode, String areaCode) {
            return ResponseMessage.defaultFallBack();
        }

        @Override
        public ResponseMessage getByCode(String code) {
            return ResponseMessage.defaultFallBack();
        }

        @Override
        public ResponseMessage findByCode(String code) {
            return ResponseMessage.defaultFallBack();
        }
    }
}
