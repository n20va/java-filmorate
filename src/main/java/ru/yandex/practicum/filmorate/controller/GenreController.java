package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
@Validated
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<GenreDto> findAll() {
        log.trace("Collection of all genres requested");
        return genreService.findAll();
    }

    @GetMapping("/{genreId}")
    public GenreDto findById(@PathVariable @Positive long genreId) {
        log.trace("Find genre with id {} requested", genreId);
        return genreService.findById(genreId);
    }
}
