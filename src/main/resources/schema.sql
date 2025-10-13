CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    varchar(80) NOT NULL UNIQUE,
    login    varchar(30) NOT NULL UNIQUE,
    name     varchar(30) NOT NULL,
    birthday date        NOT NULL CHECK (birthday <= CURRENT_DATE())
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     varchar(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS mpa_ratings
(
    mpa_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name   varchar(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    film_id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                varchar(120) NOT NULL,
    description         varchar(200) NOT NULL,
    release_date        date         NOT NULL CHECK (release_date >= '1895-12-28'),
    duration_in_minutes BIGINT      NOT NULL CHECK (duration_in_minutes > 0),
    mpa_id              BIGINT      NOT NULL,
    FOREIGN KEY (mpa_id) REFERENCES mpa_ratings (mpa_id)
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes
(
    user_id BIGINT NOT NULL,
    film_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendships
(
    user_id1 BIGINT                      NOT NULL,
    user_id2 BIGINT                      NOT NULL,
    status   ENUM ('pending', 'accepted') NOT NULL DEFAULT 'pending',
    PRIMARY KEY (user_id1, user_id2),
    FOREIGN KEY (user_id1) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id2) REFERENCES users (user_id) ON DELETE CASCADE,
    CHECK (user_id1 <> user_id2)
);