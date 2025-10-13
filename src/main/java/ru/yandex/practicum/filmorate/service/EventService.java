package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.event.EventDto;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventService {
    List<EventDto> getUserFeed(long userId);

    void addEvent(Event event);
}