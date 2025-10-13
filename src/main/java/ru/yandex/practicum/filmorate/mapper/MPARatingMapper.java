package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.mparating.MPARatingDto;
import ru.yandex.practicum.filmorate.model.MPARating;

@UtilityClass
public class MPARatingMapper {
    public MPARatingDto toMPARatingDto(MPARating mparating) {
        return new MPARatingDto(mparating.getId(), mparating.getName());
    }

    public static MPARating toMPARating(MPARatingDto mpa) {
        return MPARating.builder()
                .id(mpa.id())
                .name(mpa.name())
                .build();
    }
}
