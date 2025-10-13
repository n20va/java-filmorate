package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> findAll();

    UserDto findById(Long id);

    UserDto create(NewUserRequest request);

    UserDto update(UpdateUserRequest request);

    void deleteById(long id);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    Collection<UserDto> findFriends(long userId);

    Collection<UserDto> findAllCommonFriends(long userId1, long userId2);
}