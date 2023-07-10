package com.example.leonproject.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Leon",
                        email = "xxxxxxx@shoa.com",
                        url = "xxxxxxxxxxxxx/xxxxxxx.com"
                ),
                description = "Test Swagger For Practice",
                title = "LEON PROJECT API",
                version = "1.1",
                license = @License(
                        name = "No license",
                        url = "xxxxxxxxxx/xxxxxxx.com"
                ),
                termsOfService = "Terms of service1"
        ),
        servers = {
                @Server(
                        description = "Dev ENV",
                        url = "http://localhost:8080/LeonAPI/dev"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "http://localhost:8080/LeonAPI/prod"
                )
        }
)
@SecurityScheme(
        name = "JWT Token",
        description = "Required JWT TOKEN from your response header",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP, //Type 必填
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
