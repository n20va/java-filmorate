package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);

    Collection<User> findAll();

    User save(User user);

    void update(User user);

    void deleteById(long id);

    Collection<User> findAllFriends(long userId);

    Collection<User> findAllCommonFriends(long userId1, long userId2);
}
