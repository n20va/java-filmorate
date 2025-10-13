package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private static Validator validator;
    private static NewUserRequest user;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    void setUpEach() {
        user = new NewUserRequest();
        user.setEmail("johndoe@example.com");
        user.setLogin("johndoe");
        user.setBirthday(LocalDate.of(1980, 1, 1));
    }

    @Test
    void testValidAllFields() {
        assertTrue(validator.validate(user).isEmpty());
    }

    @Test
    void testNullEmail() {
        user.setEmail(null);
        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void testEmptyEmail() {
        user.setEmail("  ");
        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void testInvalidEmail() {
        user.setEmail("это-неправильный?эмейл@");
        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void testNullLogin() {
        user.setLogin(null);
        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void testEmptyLogin() {
        user.setLogin("   ");
        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void testFutureBirthday() {
        user.setBirthday(LocalDate.now());
        assertFalse(validator.validate(user).isEmpty());
    }
}