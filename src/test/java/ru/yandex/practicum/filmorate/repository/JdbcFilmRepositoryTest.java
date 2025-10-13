package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.repository.film.FilmResultSetExtractor;
import ru.yandex.practicum.filmorate.repository.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.film.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.util.TestFilmUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcFilmRepository.class, FilmRowMapper.class, GenreRowMapper.class, FilmResultSetExtractor.class})
public class JdbcFilmRepositoryTest {
    @Autowired
    private JdbcFilmRepository filmRepository;

    @Test
    @DisplayName("Should find film by id")
    public void findFilmById() {
        Film film = TestFilmUtils.getTestFilm();

        Optional<Film> filmOptional = filmRepository.findById(film.getId());

        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("Should return all films")
    public void findAll() {
        Collection<Film> testFilmCollection = TestFilmUtils.getTestFilmCollection();

        Collection<Film> films = filmRepository.findAll();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(testFilmCollection);
    }

    @Test
    @DisplayName("Should update film ")
    public void updateFilm() {
        Film film = TestFilmUtils.getTestFilm();
        film.setName("updatedName");

        filmRepository.update(film);

        Optional<Film> retrievedFilm = filmRepository.findById(film.getId());

        assertThat(retrievedFilm)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("Should save film")
    public void deleteFilm() {
        Film film = Film.builder()
                .name("newFilm")
                .description("newDesc")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(60)
                .mpaRating(MPARating.builder().id(2L).name("PG").build())
                .build();

        Film savedFilm = filmRepository.save(film);

        Optional<Film> retrievedFilm = filmRepository.findById(savedFilm.getId());

        assertThat(retrievedFilm)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedFilm);
    }

    @Test
    @DisplayName("Should delete film by id")
    public void deleteFilmById() {
        Film film = TestFilmUtils.getTestFilm();

        filmRepository.deleteById(film.getId());

        Optional<Film> retrievedFilm = filmRepository.findById(film.getId());

        assertThat(retrievedFilm)
                .isEmpty();
    }
}
