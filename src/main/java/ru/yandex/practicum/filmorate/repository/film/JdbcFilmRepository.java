package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Genre> genreRowMapper;
    private final RowMapper<Film> filmRowMapper;
    private final ResultSetExtractor<List<Film>> filmResultSetExtractor;

    @Override
    public Optional<Film> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        String selectFilmByIdSql = """
                SELECT f.film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration_in_minutes,
                       f.mpa_id,
                       mr.name AS mpa_name,
                       g.genre_id,
                       g.name AS genre_name
                FROM films f
                JOIN mpa_ratings mr ON f.mpa_id = mr.mpa_id
                LEFT JOIN film_genres fg ON f.film_id = fg.film_id
                LEFT JOIN genres g ON g.genre_id = fg.genre_id
                WHERE f.film_id = :id""";

        List<Film> films = jdbc.query(selectFilmByIdSql, params, filmResultSetExtractor);

        return films.isEmpty() ? Optional.empty() : Optional.of(films.getFirst());
    }

    @Override
    public Collection<Film> findAll() {
        String filmsAndMpaSql = """
                SELECT f.film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration_in_minutes,
                       f.mpa_id,
                       mr.name AS mpa_name
                FROM films f
                JOIN mpa_ratings mr ON f.mpa_id = mr.mpa_id""";
        Map<Long, Film> films = jdbc.query(filmsAndMpaSql, filmRowMapper).stream()
                .collect(Collectors.toMap(
                        Film::getId,
                        Function.identity()
                ));

        String genresSql = "SELECT * FROM genres";
        Map<Long, Genre> genres = jdbc.query(genresSql, genreRowMapper).stream()
                .collect(Collectors.toMap(
                        Genre::getId,
                        Function.identity()
                ));

        String filmsGenresSql = """
                SELECT film_id,
                       genre_id
                FROM film_genres""";
        List<Map.Entry<Long, Long>> filmsGenresList = jdbc.query(filmsGenresSql,
                (rs, rowNum) -> Map.entry(rs.getLong("film_id"),
                        rs.getLong("genre_id"))
        );

        for (Map.Entry<Long, Long> entry : filmsGenresList) {
            films.get(entry.getKey()).addGenre(genres.get(entry.getValue()));
        }

        return films.values();
    }

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = prepareParamMap(film);

        String saveFilmSql = """
                INSERT INTO films(name, description, release_date, duration_in_minutes, mpa_id)
                VALUES (:name, :description, :release_date, :duration, :mpa_id)""";
        jdbc.update(saveFilmSql, params, keyHolder, new String[]{"film_id"});

        film.setId(keyHolder.getKeyAs(Long.class));

        saveGenres(film.getGenres(), film.getId());
        return film;
    }

    @Override
    public void update(Film film) {
        String updateFilmSql = """
                UPDATE films
                SET name = :name, description = :description, release_date = :release_date,
                duration_in_minutes = :duration, mpa_id = :mpa_id
                WHERE film_id = :film_id""";
        MapSqlParameterSource params = prepareParamMap(film)
                .addValue("film_id", film.getId());
        jdbc.update(updateFilmSql, params);

        String deleteFilmGenresSql = """
                DELETE FROM film_genres WHERE film_id = :film_id""";
        MapSqlParameterSource deleteParams = new MapSqlParameterSource();
        deleteParams.addValue("film_id", film.getId());
        jdbc.update(deleteFilmGenresSql, deleteParams);

        saveGenres(film.getGenres(), film.getId());
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        String deleteFilmByIdSql = "DELETE FROM films WHERE film_id = :id";
        jdbc.update(deleteFilmByIdSql, params);
    }

    @Override
    public Collection<Film> findTopPopularFilms(int count) {
        String selectTopFilmsSql = """
                SELECT f.film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration_in_minutes,
                       f.mpa_id,
                       mr.name AS mpa_name,
                       g.genre_id,
                       g.name AS genre_name
                FROM films f
                JOIN mpa_ratings mr ON f.mpa_id = mr.mpa_id
                LEFT JOIN film_genres fg ON f.film_id = fg.film_id
                LEFT JOIN genres g ON g.genre_id = fg.genre_id
                LEFT JOIN likes fl ON f.film_id = fl.film_id
                GROUP BY f.film_id, f.name, g.genre_id
                ORDER BY COUNT(fl.user_id) DESC, f.film_id
                LIMIT :count""";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("count", count);

        return jdbc.query(selectTopFilmsSql, params, filmResultSetExtractor);
    }

    private void saveGenres(Set<Genre> genres, long filmId) {
        String insertGenresSql = """
                INSERT INTO film_genres (film_id, genre_id)
                VALUES (:film_id, :genre_id)""";

        SqlParameterSource[] batchParams = genres.stream()
                .map(Genre::getId)
                .map(genreId -> new MapSqlParameterSource()
                        .addValue("film_id", filmId)
                        .addValue("genre_id", genreId))
                .toArray(SqlParameterSource[]::new);

        jdbc.batchUpdate(insertGenresSql, batchParams);
    }

    private MapSqlParameterSource prepareParamMap(Film film) {
        return new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpaRating().getId());
    }
}
