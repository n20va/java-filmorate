package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;
import ru.yandex.practicum.filmorate.validation.AfterDate;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFilmRequest {
    @NotNull
    Long id;
    String name;
    @Size(message = "Description length must be less than 200", max = 200)
    String description;
    @AfterDate(value = "1895-12-28", message = "Release date must be after December 28, 1895")
    LocalDate releaseDate;
    @Positive(message = "Film duration must be positive")
    Integer duration;
    MPARatingDto mpa;
    List<GenreDto> genres;

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasDescription() {
        return description != null && !description.isEmpty();
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasGenres() {
        return genres != null && !genres.isEmpty();
    }

    public boolean hasMpa() {
        return mpa != null && mpa.id() != null;
    }
}
