package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    Long eventId;
    Long userId;
    EventType eventType;
    Operation operation;
    Long entityId;
    Long timestamp;
}