package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    MPARating mpaRating;
    @Builder.Default
    Set<Genre> genres = new HashSet<>();
    @Builder.Default
    Set<Long> usersWhoLiked = new HashSet<>();

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }
}
