package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private User getUserOrThrow(int userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    public User addUser(User user) {
        User addedUser = userStorage.addUser(user);
        log.info("Добавлен пользователь: {}", addedUser);
        return addedUser;
    }

    public User updateUser(User user) {
        getUserOrThrow(user.getId());
        User updatedUser = userStorage.updateUser(user);
        log.info("Обновлен пользователь: {}", updatedUser);
        return updatedUser;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return getUserOrThrow(id);
    }

    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }
        getUserOrThrow(userId);
        getUserOrThrow(friendId);
        friends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
        log.info("Пользователь с id={} добавил в друзья пользователя с id={}", userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя удалить самого себя из друзей");
        }
        getUserOrThrow(userId);
        getUserOrThrow(friendId);
        if (friends.containsKey(userId)) {
            friends.get(userId).remove(friendId);
        }
        if (friends.containsKey(friendId)) {
            friends.get(friendId).remove(userId);
        }
        log.info("Пользователь с id={} удалил из друзей пользователя с id={}", userId, friendId);
    }

    public List<User> getFriends(int userId) {
        if (!friends.containsKey(userId)) {
            return new ArrayList<>();
        }
        return friends.get(userId).stream()
                .map(this::getUserOrThrow)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        Set<Integer> userFriends = friends.getOrDefault(userId, new HashSet<>());
        Set<Integer> otherFriends = friends.getOrDefault(otherId, new HashSet<>());
        
        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(this::getUserOrThrow)
                .collect(Collectors.toList());
    }
}
