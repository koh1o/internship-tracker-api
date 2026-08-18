package io.github.koh1o.internshiptrackerapi.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Internship Tracker API",
                version = "1.0",
                description = "REST API for tracking companies, vacancies and internship applications"
        )
)
public class OpenApiConfiguration {
}
