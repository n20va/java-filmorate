package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private Film getFilmOrThrow(int filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + filmId + " не найден"));
    }

    private void getUserOrThrow(int userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    public Film addFilm(Film film) {
        Film addedFilm = filmStorage.addFilm(film);
        log.info("Добавлен фильм: {}", addedFilm);
        return addedFilm;
    }

    public Film updateFilm(Film film) {
        getFilmOrThrow(film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Обновлен фильм: {}", updatedFilm);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return getFilmOrThrow(id);
    }

    public void addLike(int filmId, int userId) {
        getFilmOrThrow(filmId);
        getUserOrThrow(userId);
        likes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
        log.info("Пользователь с id={} поставил лайк фильму с id={}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        getFilmOrThrow(filmId);
        getUserOrThrow(userId);
        if (likes.containsKey(filmId)) {
            likes.get(filmId).remove(userId);
            log.info("Пользователь с id={} удалил лайк с фильма с id={}", userId, filmId);
        }
    }

    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Параметр count должен быть положительным");
        }
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> {
                    int likesCount1 = likes.getOrDefault(f1.getId(), Collections.emptySet()).size();
                    int likesCount2 = likes.getOrDefault(f2.getId(), Collections.emptySet()).size();
                    return Integer.compare(likesCount2, likesCount1);
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}
