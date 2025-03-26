package com.carpenstreet.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfiguration {
    @Bean
    fun openApiConfiguration(): OpenAPI {
        val securitySchemeName = "X-USER-ID"
        val securityRequirement = SecurityRequirement().addList(securitySchemeName)

        val components =
            Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.APIKEY)
                        .`in`(SecurityScheme.In.HEADER)
                )

        val server = Server().url("/")

        return OpenAPI()
            .openapi("3.1.0")
            .addSecurityItem(securityRequirement)
            .components(components)
            .info(
                Info().title("Carpenstreet Product API")
                    .description("카펜스트리트 상품 프로젝트 API 명세서입니다.")
                    .version("v1"),
            )
            .servers(listOf(server))
    }
}
