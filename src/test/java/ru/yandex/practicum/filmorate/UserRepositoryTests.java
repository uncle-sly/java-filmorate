package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbUserRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({InDbUserRepository.class, UserRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTests {

    private final InDbUserRepository userRepository;
    User newUser = new User();

    @BeforeEach
    void setUp() {
        newUser.setEmail("user6@example.com");
        newUser.setLogin("user6");
        newUser.setName("Bober Kurva");
        newUser.setBirthday(LocalDate.of(2024, 8, 15));
    }

    @Test
    public void shouldGetUserById() {
        Optional<User> userOptional = userRepository.get(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(user).hasFieldOrPropertyWithValue("email", "user1@example.com");
                    assertThat(user).hasFieldOrPropertyWithValue("login", "user1");
                    assertThat(user).hasFieldOrPropertyWithValue("name", "Alice Smith");
                    assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1990, 5, 15));
                        }
                );
    }

    @DisplayName("Проверяем получение всех пользователей")
    @Test
    public void shouldGetAllUsers() {
        List<User> users = userRepository.getAll();
        assertThat(users)
                .isNotEmpty()
                .hasSize(5)
                .allMatch(Objects::nonNull);
    }

    @DisplayName("Добавляем нового пользователя")
    @Test
    public void shouldSaveUser() {
        userRepository.save(newUser);
        List<User> users = userRepository.getAll();

        assertThat(users)
                .isNotEmpty()
                .hasSize(6)
                .anyMatch(user1 -> user1.getName().equals(newUser.getName()));
    }

    @DisplayName("Меняем данные существующего пользователя")
    @Test
    public void shouldUpdateUser() {
        newUser.setName("Bober Davis");
        newUser.setEmail("BoberNekurva@example.com");
        newUser.setId(5L);

        userRepository.update(newUser);
        Optional<User> optionalUser = userRepository.get(newUser.getId());

        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user).hasFieldOrPropertyWithValue("name", "Bober Davis");
                    assertThat(user).hasFieldOrPropertyWithValue("email", "BoberNekurva@example.com");
                });
    }

}