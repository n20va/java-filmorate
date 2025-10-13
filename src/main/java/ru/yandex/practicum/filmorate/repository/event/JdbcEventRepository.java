package ru.yandex.practicum.filmorate.repository.event;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcEventRepository implements EventRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Event> findByUserId(long userId) {
        String sql = """
            SELECT event_id, user_id, event_type, operation, entity_id, timestamp
            FROM events
            WHERE user_id = :user_id
            ORDER BY timestamp DESC
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId);

        return jdbc.query(sql, params, new EventRowMapper());
    }

    @Override
    public Event save(Event event) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", event.getUserId())
                .addValue("event_type", event.getEventType().name())
                .addValue("operation", event.getOperation().name())
                .addValue("entity_id", event.getEntityId())
                .addValue("timestamp", event.getTimestamp());

        String sql = """
            INSERT INTO events (user_id, event_type, operation, entity_id, timestamp)
            VALUES (:user_id, :event_type, :operation, :entity_id, :timestamp)
            """;

        jdbc.update(sql, params, keyHolder, new String[]{"event_id"});

        event.setEventId(keyHolder.getKeyAs(Long.class));
        return event;
    }

    private static class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Event.builder()
                    .eventId(rs.getLong("event_id"))
                    .userId(rs.getLong("user_id"))
                    .eventType(EventType.valueOf(rs.getString("event_type")))
                    .operation(Operation.valueOf(rs.getString("operation")))
                    .entityId(rs.getLong("entity_id"))
                    .timestamp(rs.getLong("timestamp"))
                    .build();
        }
    }
}