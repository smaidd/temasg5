package com.example.lab5Demo;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class TaskConfiguration {
    @Bean
    public OpenAPI openApi(){
        return new OpenAPI().info(new Info()
                .title("Tasks API")
                .version("1.0")
                .description("This is a sample CRUD Tasks API"));
    }
}
