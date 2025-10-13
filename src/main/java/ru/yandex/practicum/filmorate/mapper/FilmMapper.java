package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.stream.Collectors;

@UtilityClass
public class FilmMapper {
    public FilmDto toFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .description(film.getDescription())
                .name(film.getName())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(MPARatingMapper.toMPARatingDto(film.getMpaRating()))
                .genres(film.getGenres()
                        .stream()
                        .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                        .toList())
                .build();
    }

    public Film toFilm(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpaRating(MPARatingMapper.toMPARating(request.getMpa()))
                .genres(request.getGenres()
                        .stream()
                        .map(genreDto -> Genre.builder()
                                .id(genreDto.id())
                                .name(genreDto.name())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    public Film updateFilmFields(Film film, UpdateFilmRequest request) {
        if (request.hasName()) {
            film.setName(request.getName());
        }

        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }

        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }

        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }

        if (request.hasGenres()) {
            film.setGenres(request.getGenres().stream()
                    .map(GenreMapper::toGenre)
                    .collect(Collectors.toSet()));
        }

        if (request.hasMpa()) {
            film.setMpaRating(MPARatingMapper.toMPARating(request.getMpa()));
        }

        return film;
    }
}
