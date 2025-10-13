MERGE INTO mpa_ratings (name) KEY (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

MERGE INTO genres (name) KEY (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO users (email, login, name, birthday)
VALUES ('email1@dotcom', 'login1', 'name1', '2020-01-01'),
       ('email2@dotcom', 'login2', 'name2', '2020-01-01'),
       ('email3@dotcom', 'login3', 'name3', '2020-01-01');

INSERT INTO friendships (user_id1, user_id2)
VALUES (1, 3),
       (1, 2),
       (2, 3);

INSERT INTO films (name, description, release_date, duration_in_minutes, mpa_id)
VALUES ('film1', 'desc1', '2020-01-01', 120, 1),
       ('film2', 'desc2', '2020-01-01', 120, 1);