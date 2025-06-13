package com.grepp.codemap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CodeMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeMapApplication.class, args);
    }

}
