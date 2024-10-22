package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.inMemory.InMemoryUserRepository;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Валидационные тесты для User")
@SpringBootTest
public class UserValidationTests {

    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("Проверка валидации email, создание User с некорректным email")
    @Test
    void shouldReturnExceptionOnEmptyEmail() {
        User user = new User();
        user.setEmail("mail.ru");
        user.setLogin("dolore ullamco");
        user.setName("");
        user.setBirthday(LocalDate.of(1980, 8, 20));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "электронная почта не может быть пустой и должна содержать символ @");
    }

    @DisplayName("Проверка валидации login, создание User с некорректным login")
    @Test
    void shouldReturnExceptionOnLongLogin() {
        User user = new User();
        user.setEmail("yandex@mail.ru");
        user.setLogin("dolore ullamco");
        user.setName("");
        user.setBirthday(LocalDate.of(1980, 8, 20));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "login не может быть пустым и содержать пробелы");
    }

    @DisplayName("Проверка валидации birthday, создание User с некорректной датой рождения")
    @Test
    void shouldReturnExceptionOnFutureBirthday() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("dolore");
        user.setName("");
        user.setBirthday(LocalDate.of(2446, 8, 20));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "дата рождения не может быть в будущем");
    }

    @DisplayName("Проверка валидации, создание User с корректными полями модели")
    @Test
    void shouldReturnNoUserValidationException() {
        InMemoryUserRepository repository = new InMemoryUserRepository();
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("dolore");
        user.setName("white");
        user.setBirthday(LocalDate.of(2006, 8, 20));
        repository.save(user);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "проверить параметры");
        assertEquals(repository.getAll().size(), 1, "репозиторий не должен быть пустым");
    }
}