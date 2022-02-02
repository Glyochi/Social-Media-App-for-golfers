package com.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class SpringbootApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringbootApplication.class, args);
    }

    @Configuration
    @EnableSwagger2WebMvc
    public static class SpringFoxConfig extends WebMvcConfigurerAdapter {
        @Bean
        public Docket api() {
            //This follows builder pattern***
            return new Docket(DocumentationType.SWAGGER_2)
                    .select() //Getting the api selector builder object
                    .apis(RequestHandlerSelectors.any()) //
                    .paths(PathSelectors.any()) //Including only paths specified in the parameter
                    .build() //Build
                    .apiInfo(apiDetails());
        }

        private ApiInfo apiDetails(){
            return new ApiInfo(
                    "2_ns_8 - Backend API",
                    "This is the user-friendly version of our backend API",
                    "0.2",
                    "Definitely free to use we don't really mind",
                    null,
//                    new springfox.documentation.service.Contact("Duong Hoang Vu, Alex Glass", "http://ThisIsNotAWebsite.com", "ThisIsNotAnEmail@notgmail.com"),
                    "API NOT LICENSED",
                    "http://ThisIsNotAWebsite.com",
                    Collections.emptyList());


        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");

            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}
