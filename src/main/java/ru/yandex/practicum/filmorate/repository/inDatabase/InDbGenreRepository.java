package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InDbGenreRepository extends InDbBaseRepository<Genre> {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";

    public InDbGenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genre> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
    public List<Genre> getByIds(List<Long> ids) {
        String list = ids.stream().map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = "SELECT * FROM genre WHERE id in (" + list + ")";

        return findMany(sql);
    }


}
