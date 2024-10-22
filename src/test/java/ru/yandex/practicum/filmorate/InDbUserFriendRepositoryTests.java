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
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbUserFriendRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.UserFriendRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({InDbUserFriendRepository.class, UserFriendRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InDbUserFriendRepositoryTests {

    private final InDbUserFriendRepository userFriendRepository;
    User user = new User();
    User newFriend = new User();
    User oldFriend = new User();

    @BeforeEach
    void setUp() {
        user.setEmail("user4@example.com");
        user.setLogin("user4");
        user.setName("David Wilson");
        user.setBirthday(LocalDate.of(1988, 2, 10));
        user.setId(4L);

        newFriend.setId(1L);
        oldFriend.setId(3L);
    }

    @DisplayName("Проверяем получение всех записей из user_friend")
    @Test
    public void shouldGetAllUserFriend() {
        List<UserFriend> usersFriends = userFriendRepository.getAll();
        assertThat(usersFriends)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(7)
                .allMatch(Objects::nonNull);
    }

    @DisplayName("Добавляем нового друга")
    @Test
    public void shouldAddNewFriend() {
        userFriendRepository.addFriend(user, newFriend, false);
        Set<UserFriend> userFriends = userFriendRepository.getFriends(user);

        assertThat(userFriends)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(2)
                .anyMatch(userFriend -> userFriend.getFriendId().equals(newFriend.getId()));
    }

    @DisplayName("Удаляем одну запись о дружбе")
    @Test
    public void shouldDeleteFriend() {
        userFriendRepository.deleteFriend(user, oldFriend);
        Set<UserFriend> userFriends = userFriendRepository.getFriends(user);

        assertThat(userFriends)
                .noneMatch(userFriend -> userFriend.getFriendId().equals(oldFriend.getId()));
    }

    @DisplayName("проверяем получение списка друзей одного пользователя")
    @Test
    public void shouldGetUserFriends() {
        Set<UserFriend> userFriends = userFriendRepository.getFriends(oldFriend);

        assertThat(userFriends)
                .isNotEmpty()
                .hasSize(3)
                .anyMatch(userFriend -> userFriend.getFriendId() == 1)
                .anyMatch(userFriend -> userFriend.getFriendId() == 2)
                .anyMatch(userFriend -> userFriend.getFriendId() == 4);
    }

    @DisplayName("проверяем получение списка друзей двух пользователей")
    @Test
    public void shouldGetUserCommonFriends() {
        List<UserFriend> commonFriends = userFriendRepository.getCommonFriends(1, 3);

        assertThat(commonFriends)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(2)
                .anyMatch(userFriend -> userFriend.getUserId() == 1)
                .anyMatch(userFriend -> userFriend.getUserId() == 3);
    }

}