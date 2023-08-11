package com.poluhin.ss.demo.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return (new OpenAPI()).components((new Components()).addSecuritySchemes("bearer-jwt",
            (new SecurityScheme()).type(Type.HTTP).scheme("bearer").bearerFormat("JWT").in(In.HEADER)
                .name("Authorization")))
            .addSecurityItem((new SecurityRequirement()).addList("bearer-jwt", Collections.emptyList()));
    }

}

