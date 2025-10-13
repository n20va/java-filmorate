package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException {
        List<Film> films = new ArrayList<>();
        Film currentFilm = null;
        Long previousFilmId = null;

        while (rs.next()) {
            Long currentFilmId = rs.getLong("film_id");

            if (!currentFilmId.equals(previousFilmId)) {
                if (currentFilm != null) {
                    films.add(currentFilm);
                }

                MPARating mpaRating = MPARating.builder()
                        .id(rs.getLong("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build();

                currentFilm = Film.builder()
                        .id(currentFilmId)
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getObject("release_date", LocalDate.class))
                        .duration(rs.getInt("duration_in_minutes"))
                        .mpaRating(mpaRating)
                        .build();

                previousFilmId = currentFilmId;
            }



            Genre genre = Genre.builder()
                    .id(rs.getLong("genre_id"))
                    .name(rs.getString("genre_name"))
                    .build();
            if (!rs.wasNull()) {
                currentFilm.addGenre(genre);
            }
        }

        if (currentFilm != null) {
            films.add(currentFilm);
        }

        return films;
    }
}
