package com.madaha.codesecone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
public class CodeSecOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeSecOneApplication.class, args);
    }

}
