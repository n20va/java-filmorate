package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmTest {
    private static Validator validator;
    private static NewFilmRequest film;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    void setUpEach() {
        film = new NewFilmRequest();

        film.setName("Film valid name");
        film.setDescription("Film valid description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100);
    }

    @Test
    void testValidAllFields() {
        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    void testEmptyName() {
        film.setName("  ");
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void testNullName() {
        film.setName(null);
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void testEmptyDescription() {
        film.setDescription("  ");
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void testNullDescription() {
        film.setDescription(null);
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void testVeryLongDescription() {
        film.setDescription("s".repeat(201));
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void testExact200LongDescription() {
        film.setDescription("s".repeat(200));
        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    void testReleaseDateBefore1895() {
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void testNegativeDuration() {
        film.setDuration(-10);
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void testZeroDuration() {
        film.setDuration(0);
        assertFalse(validator.validate(film).isEmpty());
    }
}