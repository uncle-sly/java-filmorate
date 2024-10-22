package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbFilmRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.FilmRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({InDbFilmRepository.class, FilmRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryTests {

    private final InDbFilmRepository filmRepository;
    Film updatedFilm = new Film();
    Film newFilm = new Film();
    User user = new User();

    @BeforeEach
    void setUp() {
//        user
        user.setId(1L);
//        updatedFilm
        Mpa mpa = new Mpa();
        mpa.setId(5L);
        Genre genre = new Genre();
        genre.setId(4L);
        updatedFilm.setId(8L);
        updatedFilm.setName("Gladiator 2");
        updatedFilm.setDescription("A general seeks revenge against a corrupt emperor.");
        updatedFilm.setReleaseDate(LocalDate.of(2010, 5, 5));
        updatedFilm.setDuration(180);
        updatedFilm.setMpa(mpa);
        updatedFilm.setGenres(Set.of(genre));

//        newFilm
        Mpa newMpa = new Mpa();
        newMpa.setId(2L);
        Genre newGenre = new Genre();
        newGenre.setId(6L);
        newFilm.setId(11L);
        newFilm.setName("Comandos");
        newFilm.setDescription("Saving the world.");
        newFilm.setReleaseDate(LocalDate.of(1985, 6, 20));
        newFilm.setDuration(90);
        newFilm.setMpa(newMpa);
        newFilm.setGenres(Set.of(newGenre));
    }

    @DisplayName("Проверяем получение Фильма по ID")
    @Test
    public void shouldReturnFilmById() {
        Optional<Film> filmOptional = filmRepository.get(8);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film).hasFieldOrPropertyWithValue("id", 8L);
                    assertThat(film).hasFieldOrPropertyWithValue("name", "Gladiator");
                    assertThat(film).hasFieldOrPropertyWithValue("description", "A general seeks revenge against a corrupt emperor.");
                    assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 5, 5));
                    assertThat(film).hasFieldOrPropertyWithValue("duration", 155);
                    assertThat(film).hasFieldOrPropertyWithValue("mpa", film.getMpa());
                        }
                );
    }

    @DisplayName("Проверяем получение всех фильмов")
    @Test
    public void shouldReturnAllFilms() {
        List<Film> films = filmRepository.getAll();

        assertThat(films)
                .isNotEmpty()
                .hasSize(10)
                .allMatch(Objects::nonNull);
    }

    @DisplayName("Проверяем добавление нового фильма, метод save")
    @Test
    public void shouldSaveNewFilm() {
        filmRepository.save(newFilm);
        Optional<Film> optionalFilm = filmRepository.get(11);
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film).hasFieldOrPropertyWithValue("id", 11L);
                    assertThat(film).hasFieldOrPropertyWithValue("name", "Comandos");
                });
    }

    @DisplayName("Проверяем обновление данных существующего фильма, метод update")
    @Test
    public void shouldUpdateFilm() {
        filmRepository.update(updatedFilm);
        Optional<Film> optionalFilm = filmRepository.get(updatedFilm.getId());
        System.out.println(optionalFilm);

        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film).hasFieldOrPropertyWithValue("id", 8L);
                    assertThat(film).hasFieldOrPropertyWithValue("name", "Gladiator 2");
                });
    }

    @DisplayName("Проверяем удаление like для фильма")
    @Test
    public void shouldDeleteLike() {
        updatedFilm.setId(6L);
        boolean delLike = filmRepository.deleteLike(updatedFilm, user);

        assertThat(delLike).isNotEqualTo(false);
    }

    @DisplayName("Проверяем получение списка популярных фильмов")
    @Test
    public void shouldGetPopularFilms() {
        List<Film> popular = filmRepository.getPopular(3);
        System.out.println(popular);

        assertThat(popular)
                .isNotEmpty()
                .hasSize(3)
                .allMatch(Objects::nonNull)
                .extracting(Film::getName)
                .contains("The Shawshank Redemption", "The Godfather", "Interstellar");
    }

}