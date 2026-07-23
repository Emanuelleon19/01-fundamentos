package ec.edu.ups.icc.fundamentos01.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenApi() {
        Info info = new Info()
                .title("API de Programación y Plataformas Web")
                .version("1.0.0")
                .description("Documentación interactiva de la API REST desarrollada");
                
        SecurityScheme bearerScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Ingrese el JWT generado en /auth/login");

        Components components = new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, bearerScheme);

        return new OpenAPI()
                .info(info)
                .components(components);
    }
}