package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Collection<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(GenreMapper::toGenreDto)
                .toList();
    }

    @Override
    public GenreDto findById(long id) {
        return genreRepository.findById(id)
                .map(GenreMapper::toGenreDto)
                .orElseThrow(NotFoundException.supplier("Genre with id %d not foud", id));
    }
}
