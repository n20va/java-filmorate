package ru.yandex.practicum.filmorate.repository.mparating;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;
import java.util.Optional;

public interface MPARatingRepository {
    Optional<MPARating> findById(long id);

    Collection<MPARating> findAll();
}
