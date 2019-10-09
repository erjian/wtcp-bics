package cn.com.wanwei.bic.feign;

import cn.com.wanwei.common.config.rest.OAuth2FeignRequestInterceptor;
import cn.com.wanwei.common.model.ResponseMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${wtcp.kb.service-name}", configuration = OAuth2FeignRequestInterceptor.class,
        fallback = KbServiceFeign.KbServiceFeignHystrix.class)
public interface KbServiceFeign {

    @GetMapping("/tags/getTagsByKind")
    ResponseMessage getTagsByKind(@RequestParam(value = "kind") Integer kind);

    @GetMapping("/tags/noRightTree")
    ResponseMessage getAllTreeNoRight(@RequestParam(name = "pid", defaultValue = "0") Long pid);

    @Component
    class KbServiceFeignHystrix implements KbServiceFeign {

        @Override
        public ResponseMessage getTagsByKind(Integer kind) {
            return ResponseMessage.defaultFallBack();
        }

        @Override
        public ResponseMessage getAllTreeNoRight(Long pid) {
            return ResponseMessage.defaultFallBack();
        }
    }
}

