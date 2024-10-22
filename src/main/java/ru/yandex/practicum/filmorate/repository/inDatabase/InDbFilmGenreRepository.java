package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class InDbFilmGenreRepository extends InDbBaseRepository<FilmGenre> {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_genre WHERE film_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genre";
    private static final String INSERT_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

    public InDbFilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmGenre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public List<FilmGenre> getById(long id) {
        return findMany(FIND_BY_ID_QUERY, id);
    }

    public int[] save(Film film) {
        return batchInsert(INSERT_QUERY,
                film.getId(),
                new ArrayList<>(film.getGenres())
        );
    }

}
