package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Genre> rowMapper;

    public Optional<Genre> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        String selectGenreByIdSql = "SELECT * FROM genres WHERE genre_id = :id";
        List<Genre> genres = jdbc.query(selectGenreByIdSql, params, rowMapper);

        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.getFirst());
    }

    public Collection<Genre> findAll() {
        String selectAllGenresSql = "SELECT * FROM genres ORDER BY genre_id";
        return jdbc.query(selectAllGenresSql, rowMapper);
    }

    @Override
    public List<Genre> getByIds(List<Long> genreIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("list", genreIds);
        String selectAllGenresByIdsSql = "SELECT * FROM genres WHERE genre_id IN (:list)";
        return jdbc.query(selectAllGenresByIdsSql, params, rowMapper);
    }
}
