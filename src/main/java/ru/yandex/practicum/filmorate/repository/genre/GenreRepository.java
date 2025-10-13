package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(long id);

    Collection<Genre> findAll();

    List<Genre> getByIds(List<Long> genreIds);
}
