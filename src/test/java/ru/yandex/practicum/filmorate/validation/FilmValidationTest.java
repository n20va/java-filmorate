package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {
    private Film validFilm;

    @BeforeEach
    void setUp() {
        validFilm = Film.builder()
                .name("Valid Film")
                .description("A valid film description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
    }

    @Test
    void shouldCreateValidFilm() {
        assertDoesNotThrow(() -> validateFilm(validFilm));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        validFilm.setName("");
        assertThrows(ValidationException.class, () -> validateFilm(validFilm));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionTooLong() {
        String longDescription = "A".repeat(201);
        validFilm.setDescription(longDescription);
        assertThrows(ValidationException.class, () -> validateFilm(validFilm));
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateBeforeCinemaBirthday() {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> validateFilm(validFilm));
    }

    @Test
    void shouldThrowExceptionWhenDurationNotPositive() {
        validFilm.setDuration(0);
        assertThrows(ValidationException.class, () -> validateFilm(validFilm));
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть длиннее 200 символов.");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть положительной.");
        }
    }
}
