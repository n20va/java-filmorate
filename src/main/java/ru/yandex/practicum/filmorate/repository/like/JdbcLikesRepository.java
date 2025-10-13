package ru.yandex.practicum.filmorate.repository.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLikesRepository implements LikesRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addLike(long userId, long filmId) {
        MapSqlParameterSource params = getParameterMap(userId, filmId);
        String addLikeToFilmSql = """
                INSERT INTO likes (user_id, film_id)
                VALUES (:user_id, :film_id)""";

        jdbc.update(addLikeToFilmSql, params);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        MapSqlParameterSource params = getParameterMap(userId, filmId);
        String deleteLikeFromFilmSql = """
                DELETE FROM likes
                WHERE user_id = :user_id AND film_id = :film_id""";

        jdbc.update(deleteLikeFromFilmSql, params);
    }

    private MapSqlParameterSource getParameterMap(long userId, long filmId) {
        return new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("film_id", filmId);
    }
}
