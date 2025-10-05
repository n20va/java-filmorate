package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Repository("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> filmLikes = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        filmLikes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            if (!filmLikes.containsKey(film.getId())) {
                filmLikes.put(film.getId(), new HashSet<>());
            }
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void addLike(int filmId, int userId) {
        if (filmLikes.containsKey(filmId)) {
            filmLikes.get(filmId).add(userId);
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        if (filmLikes.containsKey(filmId)) {
            filmLikes.get(filmId).remove(userId);
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> {
                    int likesCount1 = filmLikes.getOrDefault(f1.getId(), Collections.emptySet()).size();
                    int likesCount2 = filmLikes.getOrDefault(f2.getId(), Collections.emptySet()).size();
                    return Integer.compare(likesCount2, likesCount1);
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}
