package com.nineleaps.journalApp.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "My Journal API",
                version = "1.0",
                description = "APIs for managing a Journal application",
                termsOfService = "http://example.com/terms",
                contact = @Contact(name = "Akshat Mishra", email = "akshat.mishra@nineleaps.com",
                        url = "http://example.com"),
                license = @License(name = "API License", url = "http://example.com/license")
        ),
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(name = "BearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class SwaggerConfig {
}
