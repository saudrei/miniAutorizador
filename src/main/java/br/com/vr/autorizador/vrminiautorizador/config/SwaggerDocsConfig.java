package br.com.vr.autorizador.vrminiautorizador.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerDocsConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(getApiInfo());
    }
    
    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Mini Autorizador API Documentation",
                "Documentação das API's do Mini Autorizador",
                "1.0.0",
                "TERMS OF SERVICE URL",
                new Contact("VR", "", "vr@vr.com.br"),
                "LICENSE",
                "http://www.vr.com.br",
                Collections.emptyList());
    }
    

}
