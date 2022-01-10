package com.starking.minhasFinancas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.annotations.SwaggerDefinition;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableWebMvc
@EnableSwagger2
@SwaggerDefinition
public class MinhasFinancasApplication implements WebMvcConfigurer{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MinhasFinancasApplication.class, args);
	}
}
