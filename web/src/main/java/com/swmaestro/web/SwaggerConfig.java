package com.swmaestro.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String version;
    private String name;

    @Bean
    public Docket apiSpec() {
        version = "v1";
        name = "PROsentation API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.swmaestro.web"))
//                .paths(PathSelectors.ant("/user/*"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(this.name, this.version));
    }

    // temp
    private ApiInfo apiInfo(String name, String version) {
        return new ApiInfo(
                name,
                "API Docs represented by Swagger2",
                version,
                "",
                new Contact("", "", ""),
                "",
                "",
                new ArrayList<>() // vender extensions
        );
    }
}
