package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        MPARating mpaRating = MPARating.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getObject("release_date", LocalDate.class))
                .duration(rs.getInt("duration_in_minutes"))
                .mpaRating(mpaRating)
                .build();
    }
}
