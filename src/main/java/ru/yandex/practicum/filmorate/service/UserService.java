package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> getAll();

    User getById(long id);

    User save(final User user);

    User update(final User user);

    void addFriend(long userId, long friendId, boolean isConfirmed);

    void deleteFriend(long userId, long friendId);

    Set<User> getFriends(long userId);

    List<User> getCommonFriends(long userId, long otherId);
}
