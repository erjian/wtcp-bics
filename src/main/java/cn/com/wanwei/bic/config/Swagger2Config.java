package cn.com.wanwei.bic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger.enable}")
    private boolean enableSwagger;

    @Bean
    public Docket createRestApi() {
        ParameterBuilder builder = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<>();
        builder.name("access_token").description("令牌").required(false).modelRef(new ModelRef("String"))
                .parameterType("query").build();
        parameters.add(builder.build());
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).enable(enableSwagger).select()
                .apis(RequestHandlerSelectors.basePackage("cn.com.wanwei.bic.controller"))
                .paths(PathSelectors.any()).build().globalOperationParameters(parameters);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("旅游资源管理中心接口文档").description("旅游资源管理中心接口说明文档").termsOfServiceUrl("")
                .contact(new Contact("万维智慧旅游", "http://www.travel189.com", "bic@icloud.com")).version("2.1").build();
    }

}
