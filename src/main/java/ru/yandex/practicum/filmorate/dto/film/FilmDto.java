package ru.yandex.practicum.filmorate.dto.film;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDto {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    MPARatingDto mpa;
    @Singular
    List<GenreDto> genres;
}
