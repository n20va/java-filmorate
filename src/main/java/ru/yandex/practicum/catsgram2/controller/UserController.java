package ru.yandex.practicum.catsgram2.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram2.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram2.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram2.exception.NotFoundException;
import ru.yandex.practicum.catsgram2.model.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> emailToIdMap = new HashMap<>();
    private long idCounter = 1;

    @GetMapping
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        // Проверка обязательных полей
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        // Проверка уникальности email
        if (emailToIdMap.containsKey(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        // Создание нового пользователя
        User newUser = new User();
        newUser.setId(getNextId());
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setRegistrationDate(Instant.now());

        // Сохранение пользователя
        users.put(newUser.getId(), newUser);
        emailToIdMap.put(newUser.getEmail(), newUser.getId());

        return newUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        // Проверка обязательных полей
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        // Поиск существующего пользователя
        User existingUser = users.get(user.getId());
        if (existingUser == null) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }

        // Проверка уникальности email при изменении
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (emailToIdMap.containsKey(user.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            // Удаляем старый email из мапы
            emailToIdMap.remove(existingUser.getEmail());
            // Добавляем новый email
            emailToIdMap.put(user.getEmail(), user.getId());
            existingUser.setEmail(user.getEmail());
        }

        // Обновляем только те поля, которые не null
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }

        return existingUser;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return user;
    }

    private long getNextId() {
        return idCounter++;
    }
}