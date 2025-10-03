package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        Film film = Film.builder()
                .name(" ")
                .description("Test description")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(120)
                .build();

        Set violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Фильм с пустым именем должен не проходить валидацию");
    }

    @Test
    void shouldFailValidationWhenDurationIsNegative() {
        Film film = Film.builder()
                .name("Test")
                .description("Test description")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(-100)
                .build();

        Set violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Фильм с отрицательной продолжительностью должен не проходить валидацию");
    }

    @Test
    void shouldPassValidationForCorrectFilm() {
        Film film = Film.builder()
                .name("Test")
                .description("Normal description")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(120)
                .build();

        Set violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Корректный фильм должен проходить валидацию");
    }
}
