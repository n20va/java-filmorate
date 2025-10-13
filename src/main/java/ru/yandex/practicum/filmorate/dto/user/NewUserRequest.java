package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @NotNull(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    String email;
    @NotBlank(message = "Login must not be empty")
    String login;
    String name;
    @NotNull(message = "User birthday must not be empty")
    @Past(message = "User birthday must be a past date")
    LocalDate birthday;
}
