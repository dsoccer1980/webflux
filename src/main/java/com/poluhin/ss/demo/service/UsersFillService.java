package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.entity.Role;
import com.poluhin.ss.demo.domain.entity.User;
import com.poluhin.ss.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersFillService {

    private final UserRepository userRepository;

    @PostConstruct
    public void fill() {
        User user = new User(1, "user", new BCryptPasswordEncoder().encode("pass"), true);
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user).subscribe();

        user = new User(2, "admin", new BCryptPasswordEncoder().encode("pass"), true);
        user.setRoles(Set.of(Role.ADMIN));
        userRepository.save(user).subscribe();
    }

}
