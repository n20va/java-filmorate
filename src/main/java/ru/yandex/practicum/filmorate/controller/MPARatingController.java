package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
@Validated
public class MPARatingController {
    private final MPARatingService mpaService;

    @GetMapping
    public Collection<MPARatingDto> findAll() {
        log.trace("Collection of all MPARatings requested");
        return mpaService.findAll();
    }

    @GetMapping("/{mpaId}")
    public MPARatingDto findById(@PathVariable @Positive long mpaId) {
        log.trace("Find MPARating with id {} requested", mpaId);
        return mpaService.findById(mpaId);
    }
}
