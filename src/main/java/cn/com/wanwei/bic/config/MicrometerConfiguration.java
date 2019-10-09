package cn.com.wanwei.bic.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringBoot监控 Micrometer 配置
 */
@Configuration
public class MicrometerConfiguration {

    @Value("${spring.application.name}")
    private String appName;


    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer() {
        return (registry) -> registry.config().commonTags("application", appName);
    }

}
