CREATE TABLE IF NOT EXISTS mpa (
                                      id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                      name VARCHAR(255),
                                      description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS genre (
                                     id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS film (
                                    id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                    name VARCHAR(255),
                                    description VARCHAR(255),
                                    release_date DATE,
                                    duration INTEGER,
                                    rating_id INTEGER,
                                    CONSTRAINT fk_film_rating FOREIGN KEY (rating_id) REFERENCES mpa (id)
);

CREATE TABLE IF NOT EXISTS film_genre (
                                          film_id INTEGER,
                                          genre_id INTEGER,
                                          PRIMARY KEY (film_id, genre_id),
                                          CONSTRAINT fk_film_id FOREIGN KEY (film_id) REFERENCES film (id),
                                          CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genre (id)
);

CREATE TABLE IF NOT EXISTS users (
                                      id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                      email VARCHAR(255),
                                      login VARCHAR(255),
                                      name VARCHAR(255),
                                      birthday DATE
);

CREATE TABLE IF NOT EXISTS film_likes (
                                          film_id INTEGER,
                                          user_id INTEGER,
                                          PRIMARY KEY (film_id, user_id),
                                          CONSTRAINT fk_film_likes_id FOREIGN KEY (film_id) REFERENCES film (id),
                                          CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS user_friend (
                                            user_id INTEGER,
                                            friend_id INTEGER,
                                            isConfirmed BOOLEAN,
                                            PRIMARY KEY (user_id, friend_id),
                                            CONSTRAINT fk_user_id_friend FOREIGN KEY (user_id) REFERENCES users (id),
                                            CONSTRAINT fk_friend_id FOREIGN KEY (friend_id) REFERENCES users (id)
);
