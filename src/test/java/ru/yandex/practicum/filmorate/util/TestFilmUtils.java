package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

public class TestFilmUtils {
    static MPARating filmRating = MPARating.builder()
            .id(1L)
            .name("G")
            .build();

    public static Film getTestFilm() {
        return Film.builder()
                .id(1L)
                .name("film1")
                .description("desc1")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpaRating(filmRating)
                .build();
    }

    public static Collection<Film> getTestFilmCollection() {
        Film testFilm1 = getTestFilm();
        Film testFilm2 = Film.builder()
                .id(2L)
                .name("film2")
                .description("desc2")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpaRating(filmRating)
                .build();
        return Arrays.asList(testFilm1, testFilm2);
    }
}
