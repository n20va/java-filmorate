package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<User> rowMapper;

    @Override
    public Optional<User> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        String selectUserByIdSql = "SELECT * FROM users WHERE user_id = :id";
        List<User> users = jdbc.query(selectUserByIdSql, params, rowMapper);

        return users.isEmpty() ? Optional.empty() : Optional.of(users.getFirst());
    }

    @Override
    public Collection<User> findAll() {
        String selectAllUsersSql = "SELECT * FROM users ORDER BY user_id";
        return jdbc.query(selectAllUsersSql, rowMapper);
    }

    @Override
    public User save(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        String saveUserSql = """
                INSERT INTO users (email, login, name, birthday)
                VALUES (:email, :login, :name, :birthday)""";

        jdbc.update(saveUserSql, params, keyHolder, new String[]{"user_id"});

        user.setId(keyHolder.getKeyAs(Long.class));
        return user;
    }

    @Override
    public void update(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday())
                .addValue("id", user.getId());

        String updateUserSql = """
                UPDATE users
                SET email = :email, login = :login, name = :name, birthday = :birthday
                WHERE user_id = :id""";

        jdbc.update(updateUserSql, params);
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        String deleteUserByIdSql = "DELETE FROM users WHERE user_id = :id";
        jdbc.update(deleteUserByIdSql, params);
    }

    @Override
    public Collection<User> findAllFriends(long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId);

        String selectFriendsOfUserSql = """
                SELECT *
                FROM users u
                WHERE u.user_id IN (SELECT f.user_id2
                                    FROM friendships f
                                    WHERE f.user_id1 = :user_id)""";

        return jdbc.query(selectFriendsOfUserSql, params, rowMapper);
    }

    @Override
    public Collection<User> findAllCommonFriends(long userId1, long userId2) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id1", userId1)
                .addValue("user_id2", userId2);

        String selectCommonFriendsSql = """
                SELECT *
                FROM users u
                WHERE u.user_id IN (SELECT user_id2
                                    FROM friendships
                                    WHERE user_id1 = :user_id1
                                    INTERSECT
                                    SELECT user_id2
                                    FROM friendships
                                    WHERE user_id1 = :user_id2)""";

        return jdbc.query(selectCommonFriendsSql, params, rowMapper);
    }
}
