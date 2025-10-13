package ru.yandex.practicum.filmorate.repository.friendship;

public interface FriendshipsRepository {
    void addFriendship(long userId, long friendId);

    void removeFriendship(long userId, long friendId);
}
