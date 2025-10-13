package ru.yandex.practicum.filmorate.dto.mparating;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MPARatingDto(Long id, String name) {
}