package ru.yandex.practicum.filmorate.repository.mparating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMPARatingRepository implements MPARatingRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<MPARating> rowMapper;

    @Override
    public Optional<MPARating> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        String selectMpaByIdSql = "SELECT * FROM mpa_ratings WHERE mpa_id = :id";

        List<MPARating> mpaRatings = jdbc.query(selectMpaByIdSql, params, rowMapper);

        return mpaRatings.isEmpty() ? Optional.empty() : Optional.of(mpaRatings.getFirst());
    }

    @Override
    public Collection<MPARating> findAll() {
        String selectAllMpaSql = "SELECT * FROM mpa_ratings ORDER BY mpa_id";
        return jdbc.query(selectAllMpaSql, rowMapper);
    }
}
