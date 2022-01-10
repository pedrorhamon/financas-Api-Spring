package com.starking.minhasFinancas.api.config;

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

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.starking.minhasFinancas.api.resource"))
          .paths(PathSelectors.any())
          .build()
          .useDefaultResponseMessages(false)
          .apiInfo(apiInfo());
    }

	private ApiInfo apiInfo() {
	    return new ApiInfoBuilder()
	            .title("Book-Server")
	            .description(buildDescription().toString())
	            .version("1.0.0")
	            .license("Apache License Version 2.0")
	            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")	            
	            .contact(new Contact("Pedro Rhamon", "https://github.com/pedrorhamon", "pedrorhamon16@gmail.com"))
	            .build();
	}

	private StringBuilder buildDescription() {
		StringBuilder text = new StringBuilder();
		text.append("Um exemplo de aplicação Spring Boot REST API e documentada com swagger.");
		text.append("Neste exemplo do servidor de livros estão sendo utilizados Spring e swagger");
		return text;
	}

}
