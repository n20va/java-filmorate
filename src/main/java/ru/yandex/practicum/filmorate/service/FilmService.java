package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaService mpaService;
    private final GenreService genreService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                      @Qualifier("userDbStorage") UserStorage userStorage,
                      MpaService mpaService, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public void addLike(int filmId, int userId) {
        getFilmOrThrow(filmId);
        getUserOrThrow(userId);
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь с id={} поставил лайк фильму с id={}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        getFilmOrThrow(filmId);
        getUserOrThrow(userId);
        filmStorage.removeLike(filmId, userId);
        log.info("Пользователь с id={} удалил лайк с фильма с id={}", userId, filmId);
    }

    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Параметр count должен быть положительным");
        }
        List<Film> films = filmStorage.getPopularFilms(count);
        genreService.loadGenresForFilms(films);
        return films;
    }

    private Film getFilmOrThrow(int filmId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + filmId + " не найден"));
        genreService.loadGenresForFilms(Collections.singletonList(film));
        return film;
    }

    private void getUserOrThrow(int userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }
    
    public Film addFilm(Film film) {
        if (film.getMpa() != null) {
            mpaService.getMpaById(film.getMpa().getId());
        }
        validateGenres(film.getGenres());
        Film addedFilm = filmStorage.addFilm(film);
        genreService.loadGenresForFilms(Collections.singletonList(addedFilm));
        log.info("Добавлен фильм: {}", addedFilm);
        return addedFilm;
    }

    public Film updateFilm(Film film) {
        getFilmOrThrow(film.getId());
        if (film.getMpa() != null) {
            mpaService.getMpaById(film.getMpa().getId());
        }
        validateGenres(film.getGenres());
        Film updatedFilm = filmStorage.updateFilm(film);
        genreService.loadGenresForFilms(Collections.singletonList(updatedFilm));
        log.info("Обновлен фильм: {}", updatedFilm);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        genreService.loadGenresForFilms(films);
        return films;
    }
    
    public Film getFilmById(int id) {
        log.debug("Получен запрос на получение фильма с ID: {}", id);
        return getFilmOrThrow(id);
    }

    private void validateGenres(Set<Genre> genres) {
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                genreService.getGenreById(genre.getId());
            }
        }
    }
}

