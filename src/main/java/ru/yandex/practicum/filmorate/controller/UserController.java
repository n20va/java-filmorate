package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        validateUser(user);
        user.setId(nextId++);
        setUserNameIfBlank(user);
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя: {}", user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id=" + user.getId() + " не найден.");
        }
        validateUser(user);
        setUserNameIfBlank(user);
        users.put(user.getId(), user);
        log.info("Пользователь обновлён: {}", user);
        return user;
    }

    private void setUserNameIfBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

}

