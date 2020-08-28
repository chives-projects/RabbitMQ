package com.csc.rabbitmqdemo.template.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description Swagger配置
 * @Author csc
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value(value = "${swagger.enabled}")
    Boolean swaggerEnabled;

    @Bean
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).
                apiInfo(createApiInfo())
                .enable(swaggerEnabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.csc.rabbitmqdemo"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .description("基础数据服务接口")
                .contact(new Contact("csc", "https://github.com/achiever-or-loser/RabbitMQ.git", "csc@csc.com"))
                .version("v1.0.1")
                .title("rabbitmq")
                .license("Apache2.0")
                .licenseUrl("https://www.rabbitmq.com/getstarted.html")
                .build();
    }
}
