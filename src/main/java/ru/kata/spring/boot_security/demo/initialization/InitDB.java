package ru.kata.spring.boot_security.demo.initialization;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class InitDB {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public InitDB(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void fillDb() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        User admin = new User("admin", "Ivan", "Petrov", LocalDate.now(), "root");

        // Проверка пароля перед установкой
        if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Admin password cannot be null or empty");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.addRole(roleService.add(roleAdmin));
        userService.add(admin);

        User user = new User("user", "Boris", "Britva", LocalDate.now(), "root");

        // Проверка пароля перед установкой
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("User password cannot be null or empty");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(roleService.add(roleUser));
        userService.add(user);
    }
}
