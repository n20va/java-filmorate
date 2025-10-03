# java-filmorate
Template repository for Filmorate project.
<img width="781" height="677" alt="Снимок экрана 2025-10-03 в 18 12 35" src="https://github.com/user-attachments/assets/7cea28de-3c01-4240-86df-20cb03c1add2" />

**films** - хранение информации о фильмах

**users** - хранение информации о пользователях  

**mpa_ratings** - справочник возрастных рейтингов

**genres** - справочник жанров

Таблицы связей:

**film_genres** - связь фильмов и жанров (многие-ко-многим)

**film_likes** - лайки фильмов пользователями

**friendships** - дружеские связи между пользователями

Примеры использования:

1. Получение всех фильмов с рейтингами и жанрами
```sql
SELECT 
    f.film_id,
    f.name,
    f.description,
    f.release_date,
    f.duration,
    m.name AS mpa_rating,
    GROUP_CONCAT(g.name) AS genres
FROM films f
LEFT JOIN mpa_ratings m ON f.mpa_id = m.mpa_id
LEFT JOIN film_genres fg ON f.film_id = fg.film_id
LEFT JOIN genres g ON fg.genre_id = g.genre_id
GROUP BY f.film_id;
```

2. Топ 10 популярных фильмов (по количеству лайков)
```sql
SELECT 
    f.film_id,
    f.name,
    COUNT(fl.user_id) AS likes_count
FROM films f
LEFT JOIN film_likes fl ON f.film_id = fl.film_id
GROUP BY f.film_id, f.name
ORDER BY likes_count DESC
LIMIT 10;
```

3. Список общих друзей двух пользователей
```sql
WITH friends1 AS (
    SELECT friend_id 
    FROM friendships 
    WHERE user_id = 1 AND status = 'CONFIRMED'
    UNION
    SELECT user_id 
    FROM friendships 
    WHERE friend_id = 1 AND status = 'CONFIRMED'
),
friends2 AS (
    SELECT friend_id 
    FROM friendships 
    WHERE user_id = 2 AND status = 'CONFIRMED'
    UNION
    SELECT user_id 
    FROM friendships 
    WHERE friend_id = 2 AND status = 'CONFIRMED'
)
SELECT u.user_id, u.login, u.name
FROM users u
JOIN friends1 f1 ON u.user_id = f1.friend_id
JOIN friends2 f2 ON u.user_id = f2.friend_id;
```

4. Получение всех пользователей с количеством друзей
```sql
SELECT 
    u.user_id,
    u.email,
    u.login,
    u.name,
    COUNT(DISTINCT f.friend_id) AS friends_count
FROM users u
LEFT JOIN friendships f ON (
    (f.user_id = u.user_id OR f.friend_id = u.user_id) 
    AND f.status = 'CONFIRMED'
)
GROUP BY u.user_id, u.email, u.login, u.name;
```

5. Фильмы по определенному жанру
```sql
SELECT 
    f.film_id,
    f.name,
    f.release_date,
    m.name AS mpa_rating
FROM films f
JOIN film_genres fg ON f.film_id = fg.film_id
JOIN genres g ON fg.genre_id = g.genre_id
JOIN mpa_ratings m ON f.mpa_id = m.mpa_id
WHERE g.name = 'Комедия';
```

6. Запросы на дружбу (неподтвержденные)
```sql
SELECT 
    u.user_id,
    u.login,
    u.name
FROM friendships f
JOIN users u ON f.user_id = u.user_id
WHERE f.friend_id = 1 AND f.status = 'UNCONFIRMED';
```

7. Добавление нового фильма
```sql
INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('Новый фильм', 'Описание нового фильма', '2024-01-01', 120, 3);
INSERT INTO film_genres (film_id, genre_id) VALUES 
(LAST_INSERT_ID(), 1),
(LAST_INSERT_ID(), 2);
```

8. Обновление статуса дружбы
```sql
UPDATE friendships 
SET status = 'CONFIRMED' 
WHERE user_id = 2 AND friend_id = 1;
```

9. Получение лайков пользователя
```sql
SELECT 
    f.film_id,
    f.name,
    fg.genres
FROM film_likes fl
JOIN films f ON fl.film_id = f.film_id
LEFT JOIN (
    SELECT film_id, GROUP_CONCAT(g.name) AS genres
    FROM film_genres fg
    JOIN genres g ON fg.genre_id = g.genre_id
    GROUP BY film_id
) fg ON f.film_id = fg.film_id
WHERE fl.
