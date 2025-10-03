package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание фильма не может превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    private MpaRating mpa;
    private Set<Genre> genres;
}
