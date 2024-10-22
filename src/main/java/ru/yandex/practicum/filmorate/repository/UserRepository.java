package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> get(long userId);

    List<User> getAll();

    User save(User user);

    User update(User user);

//    void addFriend(User user, User friend, boolean isConfirmed);

//    void deleteFriend(User user, User friend);

//    Set<User> getFriends(User user);

//    List<User> getCommonFriends(long userId, long otherId);

}
