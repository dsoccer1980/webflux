package com.poluhin.ss.demo;

import com.poluhin.ss.demo.domain.entity.Role;
import com.poluhin.ss.demo.domain.entity.User;
import com.poluhin.ss.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

@Slf4j
@SpringBootApplication
public class Application {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        logApplicationStartup(context.getEnvironment());
    }

    @PostConstruct
    public void fill() {
        User user = new User(1, "user", new BCryptPasswordEncoder().encode("pass"), true);
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user).subscribe();

        user = new User(2, "admin", new BCryptPasswordEncoder().encode("pass"), true);
        user.setRoles(Set.of(Role.ADMIN));
        userRepository.save(user).subscribe();
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
