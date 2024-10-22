package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbFilmGenreRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.FilmGenreRowMapper;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({InDbFilmGenreRepository.class, FilmGenreRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InDbFilmGenreRepositoryTests {

    private final InDbFilmGenreRepository filmGenreRepository;

    @DisplayName("Проверяем получение всех жанров всех фильмов из таблицы(film_genre)")
    @Test
    public void shouldGetAllFilmsGenres() {
        List<FilmGenre> filmsGenres = filmGenreRepository.getAll();

        assertThat(filmsGenres)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(11)
                .allMatch(Objects::nonNull);
    }

    @DisplayName("Проверяем получение жанров одного фильма из таблицы(film_genre)")
    @Test
    public void shouldGetGenresByFilmId() {
        List<FilmGenre> filmGenres = filmGenreRepository.getById(1);

        assertThat(filmGenres)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(2)
                .anyMatch(filmGenre -> filmGenre.getFilmId() == 1 && filmGenre.getGenreId() == 1)
                .anyMatch(filmGenre -> filmGenre.getFilmId() == 1 && filmGenre.getGenreId() == 4);
    }

    @DisplayName("Проверяем сохранение жанров фильма в таблицу(film_genre) с помощью batchUpdate")
    @Test
    public void shouldSaveFilmGenres() {
        Genre genre1 = new Genre();
        genre1.setId(1L);
        Genre genre2 = new Genre();
        genre2.setId(5L);
        Film film = new Film();
        film.setId(10L);
        film.setGenres(Set.of(genre1, genre2));

        filmGenreRepository.save(film);
        List<FilmGenre> filmGenres = filmGenreRepository.getById(10);

        assertThat(filmGenres)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(3)
                .anyMatch(filmGenre -> filmGenre.getFilmId() == 10 && filmGenre.getGenreId() == 1)
                .anyMatch(filmGenre -> filmGenre.getFilmId() == 10 && filmGenre.getGenreId() == 3)
                .anyMatch(filmGenre -> filmGenre.getFilmId() == 10 && filmGenre.getGenreId() == 5);
    }
}
