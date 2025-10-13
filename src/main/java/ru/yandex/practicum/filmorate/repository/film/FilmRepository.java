package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {
    Optional<Film> findById(long id);

    Collection<Film> findAll();

    Film save(Film film);

    void update(Film film);

    void deleteById(long id);

    Collection<Film> findTopPopularFilms(int count);
}
