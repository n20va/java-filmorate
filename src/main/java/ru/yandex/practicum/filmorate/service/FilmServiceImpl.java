package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.like.LikesRepository;
import ru.yandex.practicum.filmorate.repository.mparating.MPARatingRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmServiceImpl implements FilmService {
    FilmRepository filmRepository;
    UserRepository userRepository;
    LikesRepository likesRepository;
    GenreRepository genreRepository;
    MPARatingRepository mpaRepository;
    EventService eventService;

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository.findAll()
                .stream()
                .map(FilmMapper::toFilmDto)
                .toList();
    }

    @Override
    public FilmDto findById(long id) {
        return filmRepository.findById(id)
                .map(FilmMapper::toFilmDto)
                .orElseThrow(NotFoundException.supplier("Film with id %d not found", id));
    }

    @Override
    public FilmDto create(NewFilmRequest request) {
        Film film = FilmMapper.toFilm(request);

        throwIfMpaRatingNotFound(request.getMpa().id());
        List<Long> genreIds = request.getGenres().stream()
                .distinct()
                .map(GenreDto::id)
                .toList();

        List<Genre> genres = genreRepository.getByIds(genreIds);

        if (genreIds.size() != genres.size()) {
            throw new NotFoundException("No such genres found");
        }

        film = filmRepository.save(film);
        log.info("Film with id {} has been created", film.getId());
        return FilmMapper.toFilmDto(film);
    }

    @Override
    public FilmDto update(UpdateFilmRequest request) {
        Film film = filmRepository.findById(request.getId()).orElseThrow(
                NotFoundException.supplier("Film with id %d not found", request.getId())
        );

        throwIfMpaRatingNotFound(request.getMpa().id());

        if (request.hasGenres()) {
            List<Long> genreIds = request.getGenres().stream()
                    .distinct()
                    .map(GenreDto::id)
                    .toList();

            List<Genre> genres = genreRepository.getByIds(genreIds);

            if (genreIds.size() != genres.size()) {
                throw new NotFoundException("No such genres found");
            }
        }

        film = FilmMapper.updateFilmFields(film, request);
        log.info("Film with id {} has been updated", film.getId());
        filmRepository.update(film);
        return FilmMapper.toFilmDto(film);
    }

    @Override
    public void deleteById(long id) {
        filmRepository.deleteById(id);
        log.info("Film with id {} has been deleted", id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        throwIfFilmNotFound(filmId);
        throwIfUserNotFound(userId);
        likesRepository.addLike(userId, filmId);

        // Добавляем событие лайка
        eventService.addEvent(Event.builder()
                .userId(userId)
                .eventType(EventType.LIKE)
                .operation(Operation.ADD)
                .entityId(filmId)
                .timestamp(System.currentTimeMillis())
                .build());

        log.info("Like to film with id {} has been added by user {}", filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        throwIfFilmNotFound(filmId);
        throwIfUserNotFound(userId);
        likesRepository.removeLike(userId, filmId);

        // Добавляем событие удаления лайка
        eventService.addEvent(Event.builder()
                .userId(userId)
                .eventType(EventType.LIKE)
                .operation(Operation.REMOVE)
                .entityId(filmId)
                .timestamp(System.currentTimeMillis())
                .build());

        log.info("Like to film with id {} has been removed by user {}", filmId, userId);
    }

    @Override
    public Collection<FilmDto> findFilmsWithTopLikes(int count) {
        return filmRepository.findTopPopularFilms(count)
                .stream()
                .map(FilmMapper::toFilmDto)
                .toList();
    }

    private void throwIfUserNotFound(long userId) {
        userRepository.findById(userId)
                .orElseThrow(NotFoundException.supplier("User with id %d not found", userId));
    }

    private void throwIfFilmNotFound(long filmId) {
        filmRepository.findById(filmId)
                .orElseThrow(NotFoundException.supplier("Film with id %d not found", filmId));
    }

    private void throwIfMpaRatingNotFound(long mpaRatingId) {
        mpaRepository.findById(mpaRatingId)
                .orElseThrow(NotFoundException.supplier("MPA rating with id %d not found", mpaRatingId));
    }
}