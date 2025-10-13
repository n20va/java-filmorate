package ru.yandex.practicum.filmorate.repository.event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventRepository {
    List<Event> findByUserId(long userId);

    Event save(Event event);
}