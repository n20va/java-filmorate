package ru.yandex.practicum.filmorate.dto.error;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {
}
