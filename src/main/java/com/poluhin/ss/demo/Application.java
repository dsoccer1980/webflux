package com.poluhin.ss.demo;

import com.poluhin.ss.demo.domain.entity.Role;
import com.poluhin.ss.demo.domain.entity.User;
import com.poluhin.ss.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Application {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void fill() {
        User user = new User(1, "user", new BCryptPasswordEncoder().encode("pass"), true);
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user).subscribe();

        user = new User(2, "admin", new BCryptPasswordEncoder().encode("pass"), true);
        user.setRoles(Set.of(Role.ADMIN));
        userRepository.save(user).subscribe();

        System.out.println(userRepository.findAll().doOnNext(System.out::println).subscribe());
    }

}
