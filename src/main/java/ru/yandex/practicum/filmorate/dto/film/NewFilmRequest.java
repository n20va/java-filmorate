package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotBlank;
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
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewFilmRequest {
    @NotBlank(message = "Film name must not be null or empty")
    String name;
    @Size(message = "Description length must be less than 200", max = 200)
    @NotBlank(message = "Description must not be null or empty")
    String description;
    @NotNull(message = "Release date must be present")
    @AfterDate(value = "1895-12-28", message = "Release date must be after December 28, 1895")
    LocalDate releaseDate;
    @NotNull(message = "Duration must be present")
    @Positive(message = "Film duration must be positive")
    Integer duration;
    MPARatingDto mpa;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<GenreDto> genres = new ArrayList<>();
}
