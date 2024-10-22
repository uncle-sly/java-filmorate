package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class InDbFilmRepository extends InDbBaseRepository<Film> implements FilmRepository {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";

    private static final String INSERT_LIKE_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE from film_likes WHERE  film_id = ? AND user_Id = ?";
    private static final String SELECT_POPULAR_QUERY = " SELECT f.*, COUNT(fl.film_id) AS likes " +
            "FROM film f JOIN film_likes fl ON f.id = fl.film_id " +
            "GROUP BY f.name ORDER BY likes DESC LIMIT ?";

    public InDbFilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Film> get(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public List<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Film save(Film film) {
        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    public void addLike(Film film, User user) {
        insert(INSERT_LIKE_QUERY,
                film.getId(),
                user.getId()
        );
    }

    public boolean deleteLike(Film film, User user) {
        return delete(DELETE_LIKE_QUERY,
                film.getId(),
                user.getId()
        );
    }

    public List<Film> getPopular(long count) {
        return findMany(SELECT_POPULAR_QUERY, count);
    }

}
