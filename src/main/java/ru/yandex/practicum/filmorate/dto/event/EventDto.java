package ru.yandex.practicum.filmorate.dto.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EventDto(
        Long timestamp,
        Long userId,
        EventType eventType,
        Operation operation,
        Long eventId,
        Long entityId
) {}