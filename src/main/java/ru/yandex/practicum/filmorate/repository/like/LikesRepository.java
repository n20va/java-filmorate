package ru.yandex.practicum.filmorate.repository.like;

public interface LikesRepository {
    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);
}
