package cn.com.wanwei.bic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
            log.info("------------------------ResourceServerConfig  default");
//            // 开发环境不需要授权
            http.csrf().disable().exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                    .authorizeRequests().anyRequest().permitAll();


            // 生产环境需要授权
//            http.csrf().disable().exceptionHandling()
//                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
//                    .authorizeRequests()
//                    .mvcMatchers("/v2/api-docs", "/hystrix.stream", "/turbine.stream",
//                            "/token" , "/public/**", "/rpc/**", "/actuator/prometheus")
//                    .permitAll().anyRequest().authenticated().and().httpBasic();

    }

}
