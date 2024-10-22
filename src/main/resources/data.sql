-- Очистка таблиц по порядку зависимости
DELETE FROM film_likes;        -- Удаляем лайки фильмов
DELETE FROM film_genre;       -- Удаляем жанры фильмов
DELETE FROM user_friend;     -- Удаляем дружеские связи пользователей
DELETE FROM film;             -- Удаляем фильмы
DELETE FROM genre;            -- Удаляем жанры
DELETE FROM mpa;           -- Удаляем рейтинги
DELETE FROM users;            -- Удаляем пользователей

-- Сбрасываем счетчики автоинкремента для таблиц с полем id
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN id RESTART WITH 1;
ALTER TABLE genre ALTER COLUMN id RESTART WITH 1;
ALTER TABLE mpa ALTER COLUMN id RESTART WITH 1;

-- Наполнение тестовыми данными
INSERT INTO mpa (name, description) VALUES
                                           ('G', 'General audiences.'),
                                           ('PG', 'Parental guidance suggested.'),
                                           ('PG-13', 'Parents are strongly cautioned.'),
                                           ('R', 'Restricted.'),
                                           ('NC-17', 'Adults only.');

INSERT INTO film (name, description, release_date, duration, rating_id) VALUES
                                                                            ('Inception', 'A thief is given a chance to erase his criminal past.', '2010-07-16', 148, 3),
                                                                            ('The Matrix', 'A hacker learns the shocking truth about reality.', '1999-03-31', 136, 4),
                                                                            ('Interstellar', 'Explorers travel through a wormhole to find a new home.', '2014-11-07', 169, 3),
                                                                            ('The Shawshank Redemption', 'Two men bond and find solace in prison.', '1994-09-23', 142, 4),
                                                                            ('The Godfather', 'A crime boss hands control to his reluctant son.', '1972-03-24', 175, 4),
                                                                            ('Pulp Fiction', 'Mob hitmen and a boxer navigate intertwined lives.', '1994-10-14', 154, 4),
                                                                            ('Forrest Gump', 'A man witnesses historic events through his life.', '1994-07-06', 142, 3),
                                                                            ('Gladiator', 'A general seeks revenge against a corrupt emperor.', '2000-05-05', 155, 4),
                                                                            ('The Dark Knight', 'The Joker brings chaos to Gotham City.', '2008-07-18', 152, 3),
                                                                            ('The Lion King', 'A young lion returns to reclaim his throne.', '1994-06-15', 88, 1);

INSERT INTO genre (name) VALUES
                             ('Комедия'),
                             ('Драма'),
                             ('Мультфильм'),
                             ('Триллер'),
                             ('Документальный'),
                             ('Боевик');

INSERT INTO film_genre (film_id, genre_id) VALUES
                                               (1, 4),  -- Inception - Thriller
                                               (1, 1),  -- Inception - Comedy
                                               (2, 4),  -- The Matrix - Thriller
                                               (3, 5),  -- Interstellar - Documentary
                                               (4, 2),  -- The Shawshank Redemption - Drama
                                               (5, 2),  -- The Godfather - Drama
                                               (6, 4),  -- Pulp Fiction - Thriller
                                               (7, 2),  -- Forrest Gump - Drama
                                               (8, 6),  -- Gladiator - Action
                                               (9, 4),  -- The Dark Knight - Thriller
                                               (10, 3); -- The Lion King - Animation

INSERT INTO users (email, login, name, birthday) VALUES
                                                      ('user1@example.com', 'user1', 'Alice Smith', '1990-05-15'),
                                                      ('user2@example.com', 'user2', 'Bob Johnson', '1985-08-22'),
                                                      ('user3@example.com', 'user3', 'Charlie Brown', '1992-11-30'),
                                                      ('user4@example.com', 'user4', 'David Wilson', '1988-02-10'),
                                                      ('user5@example.com', 'user5', 'Emma Davis', '1995-03-05');

-- Фильмы с одним лайком
INSERT INTO film_likes (film_id, user_id) VALUES
                                              (1, 1),  -- User 1 likes Inception
                                              (2, 2);  -- User 2 likes The Matrix

-- Фильмы с 2-5 лайками
INSERT INTO film_likes (film_id, user_id) VALUES
                                              (3, 1),  -- User 1 likes Interstellar
                                              (3, 2),  -- User 2 likes Interstellar
                                              (3, 3),  -- User 3 likes Interstellar
                                              (4, 1),  -- User 1 likes The Shawshank Redemption
                                              (4, 2),  -- User 2 likes The Shawshank Redemption
                                              (4, 3),  -- User 3 likes The Shawshank Redemption
                                              (4, 4);  -- User 4 likes The Shawshank Redemption

INSERT INTO film_likes (film_id, user_id) VALUES
                                              (5, 2),  -- User 2 likes The Godfather
                                              (5, 3),  -- User 3 likes The Godfather
                                              (5, 1),  -- User 1 likes The Godfather
                                              (5, 4);  -- User 4 likes The Godfather

INSERT INTO film_likes (film_id, user_id) VALUES
                                              (6, 1),  -- User 1 likes Pulp Fiction
                                              (6, 2);  -- User 2 likes Pulp Fiction

INSERT INTO user_friend (user_id, friend_id, isConfirmed) VALUES
                                                               (1, 2, false),
                                                               (1, 3, false),
                                                               (2, 1, false),
                                                               (3, 1, false),
                                                               (3, 2, false),
                                                               (3, 4, false),
                                                               (4, 3, false);
