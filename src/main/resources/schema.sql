CREATE TABLE IF NOT EXISTS mpa_ratings (
    mpa_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(300) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255),
    birthday DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS friendships (
    user_id INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'UNCONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (film_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_films_mpa ON films(mpa_id);
CREATE INDEX IF NOT EXISTS idx_film_genres_film ON film_genres(film_id);
CREATE INDEX IF NOT EXISTS idx_film_genres_genre ON film_genres(genre_id);
CREATE INDEX IF NOT EXISTS idx_film_likes_film ON film_likes(film_id);
CREATE INDEX IF NOT EXISTS idx_film_likes_user ON film_likes(user_id);
CREATE INDEX IF NOT EXISTS idx_friendships_user ON friendships(user_id);
CREATE INDEX IF NOT EXISTS idx_friendships_friend ON friendships(friend_id);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_login ON users(login);

ALTER TABLE films ADD CONSTRAINT IF NOT EXISTS fk_films_mpa
    FOREIGN KEY (mpa_id) REFERENCES mpa_ratings(mpa_id);

ALTER TABLE film_genres ADD CONSTRAINT IF NOT EXISTS fk_film_genres_film
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE;

ALTER TABLE film_genres ADD CONSTRAINT IF NOT EXISTS fk_film_genres_genre
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE;

ALTER TABLE friendships ADD CONSTRAINT IF NOT EXISTS fk_friendships_user
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE friendships ADD CONSTRAINT IF NOT EXISTS fk_friendships_friend
    FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE film_likes ADD CONSTRAINT IF NOT EXISTS fk_film_likes_film
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE;

ALTER TABLE film_likes ADD CONSTRAINT IF NOT EXISTS fk_film_likes_user
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;