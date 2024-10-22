package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class InDbBaseRepository<T> {

    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;


    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected boolean delete(String query, Object... params) {
        int rowsDeleted = jdbc.update(query, params);
        return rowsDeleted > 0;
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new ValidationException("Не удалось обновить данные с: " + params[4]);
        }
    }

    protected Long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Long id = null;
//      System.out.println(keyHolder.getKeys());
//        if (keyHolder.getKeys() != null && !keyHolder.getKeys().isEmpty() && keyHolder.getKeys().size() == 1) {
//            id = keyHolder.getKeyAs(Integer.class).longValue();
//        }
        if (keyHolder.getKeys() != null && !keyHolder.getKeys().isEmpty() && keyHolder.getKeys().size() == 1) {
            Integer key = keyHolder.getKeyAs(Integer.class);
            if (key != null) {
                id = key.longValue();
            }
        }

        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else if (keyHolder.getKeys().size() > 1) {
            return null;
        } else {
            throw new ValidationException("Не удалось сохранить данные: " + Arrays.toString(params));
        }
    }

    protected int[] batchInsert(String query, Long filmId, ArrayList<Genre> params) {
        return jdbc.batchUpdate(query,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, params.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return params.size();
                    }
                });
    }

}
