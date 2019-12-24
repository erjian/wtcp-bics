package cn.com.wanwei.bic.feign;

import cn.com.wanwei.common.config.rest.OAuth2FeignConfiguration;
import cn.com.wanwei.common.log.annotation.OperationLog;
import cn.com.wanwei.common.model.ResponseMessage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author **
 * @date 2019年12月18日09:49:56
 */
@FeignClient(name = "${wtcp.oauth.service-name}", configuration = OAuth2FeignConfiguration.class, fallback = AuthServiceFeign.AuthServiceFeignHystrix.class)
public interface AuthServiceFeign {

    @GetMapping(value = "/rpc/dept/findByDeptCode")
    ResponseMessage findByDeptCode(@RequestParam(value = "deptCode")String deptCode);

    @GetMapping(value = "/rpc/dept/findChildByAreaCode")
    ResponseMessage findChildByAreaCode(@RequestParam String areaCode);

    @Component
    class AuthServiceFeignHystrix implements AuthServiceFeign{

        @Override
        public ResponseMessage findByDeptCode(String deptCode) {
            return ResponseMessage.defaultFallBack();
        }

        @Override
        public ResponseMessage findChildByAreaCode(String areaCode) {
            return ResponseMessage.defaultFallBack();
        }
    }
}
