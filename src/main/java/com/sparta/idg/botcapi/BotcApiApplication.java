package com.sparta.idg.botcapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "BOTC API", version = "1.0", description = "API Endpoints Decoration"))
public class BotcApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotcApiApplication.class, args);
    }

}
