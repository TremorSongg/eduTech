package com.example.edutech.OpenAPIConfig;

// Desplegar dentro de la pagina de swagger el titulo, version, etc
import io.swagger.v3.oas.models.info.Info;
// Nos permite crear el retorno y la comunicacion con la api que tengamos personalizada
import io.swagger.v3.oas.models.OpenAPI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI edutechOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EduTech API Gestion")
                        .version("2.0")
                        .description("Microservicios API REST para gestionar cursos, usuarios y carritos de compra en la plataforma EduTech."));
    } 
}
