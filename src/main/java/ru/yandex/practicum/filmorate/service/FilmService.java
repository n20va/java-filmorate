package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.MpaDao;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                      @Qualifier("userDbStorage") UserStorage userStorage,
                      MpaDao mpaDao, GenreDao genreDao, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.jdbcTemplate = jdbcTemplate;
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
        loadGenresForFilms(films);
        return films;
    }

    private Film getFilmOrThrow(int filmId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + filmId + " не найден"));
        loadGenresForFilms(Collections.singletonList(film));
        return film;
    }

    private void getUserOrThrow(int userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    public Film addFilm(Film film) {
        if (film.getMpa() != null) {
            mpaDao.getMpaById(film.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id=" + film.getMpa().getId() + " не найден"));
        }
        validateGenres(film.getGenres());

        Film addedFilm = filmStorage.addFilm(film);
        log.info("Добавлен фильм: {}", addedFilm);
        return addedFilm;
    }

    public Film updateFilm(Film film) {
        getFilmOrThrow(film.getId());
        if (film.getMpa() != null) {
            mpaDao.getMpaById(film.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id=" + film.getMpa().getId() + " не найден"));
        }
        validateGenres(film.getGenres());

        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Обновлен фильм: {}", updatedFilm);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        loadGenresForFilms(films);
        return films;
    }

    public Film getFilmById(int id) {
        log.debug("Получен запрос на получение фильма с ID: {}", id);
        return getFilmOrThrow(id);
    }

    private void validateGenres(Set<Genre> genres) {
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                genreDao.getGenreById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Жанр с id=" + genre.getId() + " не найден"));
            }
        }
    }

    private void loadGenresForFilms(List<Film> films) {
        if (films.isEmpty()) return;
        if (filmStorage instanceof ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage) {
            return;
        }

        List<Integer> filmIds = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        Map<Integer, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film));
        String inClause = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String sql = String.format(
                "SELECT fg.film_id, g.genre_id, g.name " +
                "FROM film_genres fg " +
                "JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id IN (%s) " +
                "ORDER BY fg.film_id, g.genre_id", inClause);

        jdbcTemplate.query(sql, filmIds.toArray(), rs -> {
            int filmId = rs.getInt("film_id");
            Film film = filmMap.get(filmId);
            if (film != null) {
                Genre genre = new Genre();
                genre.setId(rs.getInt("genre_id"));
                genre.setName(rs.getString("name"));
                film.getGenres().add(genre);
            }
        });
    }
}
