package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbUserFriendRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final InDbUserFriendRepository userFriendRepository;


    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User getById(long id) {

        return userRepository.get(id).orElseThrow(() -> new ValidationException("Пользователь c ID - " + id + ", не найден."));
    }

    public User save(final User user) {

        checkUserName(user);
        return userRepository.save(user);
    }

    public User update(final User user) {
        long userId = user.getId();
        userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c ID - " + user.getId() + ", не найден."));

        checkUserName(user);
        return userRepository.update(user);
    }

    public void addFriend(long userId, long friendId, boolean isConfirmed) {
        final User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        final User friend = userRepository.get(friendId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + friendId + " не найден"));

        userFriendRepository.addFriend(user, friend, isConfirmed);
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        User friend = userRepository.get(friendId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + friendId + " не найден"));

        userFriendRepository.deleteFriend(user, friend);
    }

    public Set<User> getFriends(long userId) {

        final User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        Set<Long> uf = userFriendRepository.getFriends(user).stream()
                .map(UserFriend::getFriendId)
                .collect(Collectors.toSet());

        return userRepository.getAll().stream()
                .filter(u -> uf.contains(u.getId()))
                .collect(Collectors.toSet());
    }

    public List<User> getCommonFriends(long userId, long otherId) {

        User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        User other = userRepository.get(otherId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + otherId + " не найден"));

        Set<Long> uf = userFriendRepository.getCommonFriends(user.getId(), other.getId()).stream()
                .map(UserFriend::getFriendId)
                .collect(Collectors.groupingBy(friendId -> friendId, Collectors.counting())) // Группируем по friendId и считаем
                .entrySet().stream() // Преобразуем в поток пар (friendId, count)
                .filter(entry -> entry.getValue() > 1) // Оставляем только те, где count > 1
                .map(Map.Entry::getKey) // Извлекаем friendId
                .collect(Collectors.toSet()); // Собираем в Set

        return userRepository.getAll().stream()
                .filter(u -> uf.contains(u.getId()))
                .toList();
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
