package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbMpaRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.MpaRowMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({InDbMpaRepository.class, MpaRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InDbMpaRepositoryTests {

    private final InDbMpaRepository mpaRepository;

    @DisplayName("Проверяем получение всех записей из таблицы mpa")
    @Test
    public void shouldGetAllMpa() {
        List<Mpa> mpaAll = mpaRepository.getAll();

        assertThat(mpaAll)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(5)
                .allMatch(Objects::nonNull);
    }

    @DisplayName("Проверяем получение одной записи из таблицы mpa")
    @Test
    public void shouldGetMpaById() {
        Optional<Mpa> optionalMpa = mpaRepository.getById(4);

        assertThat(optionalMpa)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                    assertThat(mpa).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(mpa).hasFieldOrPropertyWithValue("name", "R");
                });
    }

}