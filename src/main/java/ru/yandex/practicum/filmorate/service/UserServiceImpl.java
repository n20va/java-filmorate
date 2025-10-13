package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.friendship.FriendshipsRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendshipsRepository friendshipsRepository;
    private final EventService eventService;

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserDto)
                .orElseThrow(NotFoundException.supplier("User with id %d not found", id));
    }

    @Override
    public UserDto create(NewUserRequest request) {
        if (request.getName() == null) {
            request.setName(request.getLogin());
        }
        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);
        log.info("User with id {} created", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UpdateUserRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(
                NotFoundException.supplier("User with id %d not found", request.getId())
        );
        user = UserMapper.updateUserFields(user, request);
        userRepository.update(user);
        log.info("User with id {} updated", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
        log.info("User with id {} deleted", id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        throwIfUserNotFound(userId);
        throwIfUserNotFound(friendId);

        friendshipsRepository.addFriendship(userId, friendId);

        // Добавляем событие добавления в друзья
        eventService.addEvent(Event.builder()
                .userId(userId)
                .eventType(EventType.FRIEND)
                .operation(Operation.ADD)
                .entityId(friendId)
                .timestamp(System.currentTimeMillis())
                .build());

        log.info("User with id {} added user with id {} as friend", userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        throwIfUserNotFound(userId);
        throwIfUserNotFound(friendId);
        friendshipsRepository.removeFriendship(userId, friendId);

        eventService.addEvent(Event.builder()
                .userId(userId)
                .eventType(EventType.FRIEND)
                .operation(Operation.REMOVE)
                .entityId(friendId)
                .timestamp(System.currentTimeMillis())
                .build());

        log.info("User with id {} removed user with id {} from friends", userId, friendId);
    }

    @Override
    public Collection<UserDto> findFriends(long userId) {
        throwIfUserNotFound(userId);
        return userRepository.findAllFriends(userId)
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public Collection<UserDto> findAllCommonFriends(long userId1, long userId2) {
        throwIfUserNotFound(userId1);
        throwIfUserNotFound(userId2);
        return userRepository.findAllCommonFriends(userId1, userId2)
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    private void throwIfUserNotFound(long userId) {
        userRepository.findById(userId)
                .orElseThrow(NotFoundException.supplier("User with id %d not found", userId));
    }
}