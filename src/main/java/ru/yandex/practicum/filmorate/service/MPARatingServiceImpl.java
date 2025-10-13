package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MPARatingMapper;
import ru.yandex.practicum.filmorate.repository.mparating.MPARatingRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class MPARatingServiceImpl implements MPARatingService {
    private final MPARatingRepository mpaRepository;

    @Override
    public Collection<MPARatingDto> findAll() {
        return mpaRepository.findAll()
                .stream()
                .map(MPARatingMapper::toMPARatingDto)
                .toList();
    }

    @Override
    public MPARatingDto findById(long mpaId) {
        return mpaRepository.findById(mpaId)
                .map(MPARatingMapper::toMPARatingDto)
                .orElseThrow(NotFoundException.supplier("MPA Rating with id %d not found", mpaId));
    }
}
