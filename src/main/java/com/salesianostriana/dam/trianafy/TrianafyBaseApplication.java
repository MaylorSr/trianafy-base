package com.salesianostriana.dam.trianafy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info =
@Info(description = "P01 - Proyecto  2º DAM",
        version = "1.0",
        contact = @Contact(email = "maylorbustamante2001@gmail.com", name = "Maylor"),
        license = @License(name = "API de MAYLOR"),
        title = "API similar a Spotify"
)
)
@SpringBootApplication
public class TrianafyBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrianafyBaseApplication.class, args);
    }

}
