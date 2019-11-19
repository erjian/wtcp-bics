package cn.com.wanwei.bic.feign;

import cn.com.wanwei.common.config.rest.OAuth2FeignConfiguration;
import cn.com.wanwei.common.model.ResponseMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${wtcp.coder.service-name}", configuration = OAuth2FeignConfiguration.class, fallback = CoderServiceFeign.CoderServiceFeignHystrix.class)
public interface CoderServiceFeign {

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
