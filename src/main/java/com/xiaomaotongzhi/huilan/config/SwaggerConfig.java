package com.xiaomaotongzhi.huilan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
@Bean
public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .pathMapping("/")
            .select()
            //配置扫描接口的方式，基于包去扫描，这个包要是controller
            .apis(RequestHandlerSelectors.basePackage("com.xiaomaotongzhi.huilan.controller"))
            .paths(PathSelectors.any())
            .build().apiInfo(new ApiInfoBuilder().title("huilanbei")
                    .description("huilanbei interface") //详细信息
                    .version("2.0")
                    .contact(new Contact("xiaomao","www.xiaomaotongzhi.com","2359643054@qq.com"))
                    .license("xiaomaotongzhi")
                    .licenseUrl("http://www.xiaomaotongzhi.com")
                    .build()) ;

    }
}
