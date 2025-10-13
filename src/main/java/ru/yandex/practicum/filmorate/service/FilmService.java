package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;

import java.util.Collection;

public interface FilmService {
    Collection<FilmDto> findAll();

    FilmDto findById(long id);

    FilmDto create(NewFilmRequest request);

    FilmDto update(UpdateFilmRequest request);

    void deleteById(long id);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    Collection<FilmDto> findFilmsWithTopLikes(int count);
}
