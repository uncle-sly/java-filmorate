package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbGenreRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.GenreRowMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({InDbGenreRepository.class, GenreRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InDbGenreRepositoryTests {

    private final InDbGenreRepository genreRepository;

    @DisplayName("Проверяем получение всех записей из таблицы Жанров(genre)")
    @Test
    public void shouldGetAllGenre() {
        List<Genre> genres = genreRepository.getAll();

        assertThat(genres)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(6)
                .allMatch(Objects::nonNull);
    }

    @DisplayName("Проверяем получение одной записи из таблицы Жанров(genre)")
    @Test
    public void shouldGetGenreById() {
        Optional<Genre> optionalGenre = genreRepository.getById(3);

        assertThat(optionalGenre)
                .isPresent()
                .hasValueSatisfying(genre -> {
                    assertThat(genre).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(genre).hasFieldOrPropertyWithValue("name", "Мультфильм");
                });
    }

}