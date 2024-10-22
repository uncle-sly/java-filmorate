package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.inMemory.InMemoryFilmRepository;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Валидационные тесты для Film")
@SpringBootTest
public class FilmValidationTests {

    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("Проверка валидации name, создание Film с пустым именем")
    @Test
    void shouldReturnExceptionOnEmptyName() {
        Film film = new Film();
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "название не может быть пустым");
    }

    @DisplayName("Проверка валидации description, создание Film с длинным описанием")
    @Test
    void shouldReturnExceptionOnLongDescription() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать " +
                "господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время " +
                "«своего отсутствия», стал кандидатом Коломбани.");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "максимальная длина описания не может быть больше— 200 символов");
    }

    @DisplayName("Проверка валидации releaseDate, создание Film с некорректной датой релиза фильма")
    @Test
    void shouldReturnExceptionOnEmptyReleaseDate() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1890, 3, 25));
        film.setDuration(200);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "дата релиза — не может быть раньше 28 декабря 1895 года");
    }

    @DisplayName("Проверка валидации duration, создание Film с некорректной продолжительностью фильма")
    @Test
    void shouldReturnExceptionOnEmptyDuration() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1980, 3, 25));
        film.setDuration(-100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        System.out.println(violations);
        assertFalse(violations.isEmpty(), "продолжительность фильма должна быть положительным числом");
    }

    @DisplayName("Проверка валидации, создание Film с корректными полями модели")
    @Test
    void shouldReturnNoException() {
        InMemoryFilmRepository repository = new InMemoryFilmRepository();
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1980, 3, 25));
        film.setDuration(100);
        repository.save(film);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "проверить параметры");
        assertEquals(repository.getAll().size(), 1, "репозиторий не должен быть пустым");
    }
}
