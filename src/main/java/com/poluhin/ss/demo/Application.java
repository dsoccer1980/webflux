package com.poluhin.ss.demo;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        logApplicationStartup(context.getEnvironment());
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https")
            .orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::hasText)
            .orElse("/");
        log.info("Swagger: \t\t{}://localhost:{}{}swagger-ui/index.html",
            protocol, serverPort, contextPath
        );
    }

}
