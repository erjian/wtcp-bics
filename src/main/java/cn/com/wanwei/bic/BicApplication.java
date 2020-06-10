package cn.com.wanwei.bic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableFeignClients(basePackages = "cn.com.wanwei")
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@ComponentScan("cn.com.wanwei")
@MapperScan("cn.com.wanwei.*.mapper")
@EnableAsync
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BicApplication.class, args);
    }

}
