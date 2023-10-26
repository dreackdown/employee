package dev.hugofaria.employee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RESTful Employee Management API")
                        .version("v1")
                        .description("Explore our RESTful Employee Management API for streamlined employee data management. Navigate seamlessly with Swagger documentation for smooth integration and development.")
                        .termsOfService("https://github.com/dreackdown")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://github.com/dreackdown")
                        )
                );
    }
}