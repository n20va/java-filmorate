package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.MpaDao;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class, MpaDao.class, GenreDao.class})
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    void testFindFilmById() {
        MpaRating mpa = new MpaRating();
        mpa.setId(1);
        mpa.setName("G");
        mpa.setDescription("Нет возрастных ограничений");
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(mpa)
                .build();
        Film addedFilm = filmStorage.addFilm(film);
        Optional<Film> foundFilm = filmStorage.getFilmById(addedFilm.getId());
        assertThat(foundFilm)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("id", addedFilm.getId()));
    }

    @Test
    void testGetAllFilms() {
        MpaRating mpa1 = new MpaRating();
        mpa1.setId(1);
        mpa1.setName("G");
        mpa1.setDescription("Нет возрастных ограничений");

        Film film1 = Film.builder()
                .name("Film 1")
                .description("Description 1")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(mpa1)
                .build();
        MpaRating mpa2 = new MpaRating();
        mpa2.setId(2);
        mpa2.setName("PG");
        mpa2.setDescription("Детям рекомендуется смотреть с родителями");
        Film film2 = Film.builder()
                .name("Film 2")
                .description("Description 2")
                .releaseDate(LocalDate.of(2021, 1, 1))
                .duration(150)
                .mpa(mpa2)
                .build();
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        List<Film> films = filmStorage.getAllFilms();
        assertThat(films).hasSize(2);
    }
}
