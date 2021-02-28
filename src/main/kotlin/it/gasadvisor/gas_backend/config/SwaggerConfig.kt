package it.gasadvisor.gas_backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.schema.ModelRef

import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.service.Parameter


@Configuration
@EnableSwagger2
@Profile("LOCAL")
class SwaggerConfig {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("it.gasadvisor.gas_backend"))
            .paths(PathSelectors.any())
            .build()
            .globalOperationParameters(listOf(authHeader))
    }

    val authHeader: Parameter = ParameterBuilder()
        .parameterType("header")
        .name("Authorization")
        .modelRef(ModelRef("string"))
        .build()

}
