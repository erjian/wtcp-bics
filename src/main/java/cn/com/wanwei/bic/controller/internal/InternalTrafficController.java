package cn.com.wanwei.bic.controller.internal;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/public/traffic")
@Api(value = "C端交通枢纽相关接口", tags = "C端交通枢纽相关接口")
public class InternalTrafficController {
}
