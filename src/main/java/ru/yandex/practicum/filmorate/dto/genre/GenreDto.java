package ru.yandex.practicum.filmorate.dto.genre;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenreDto(Long id, String name) {
}