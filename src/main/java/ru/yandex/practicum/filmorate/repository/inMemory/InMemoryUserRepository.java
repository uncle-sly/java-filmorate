package ru.yandex.practicum.filmorate.repository.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<User>> usersFriends = new HashMap<>();
    private Long userId = 0L;


    public Optional<User> get(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User save(final User user) {
        user.setId(generateUserId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        User currentUser = users.get(user.getId());
        currentUser.setEmail(user.getEmail());
        currentUser.setLogin(user.getLogin());
        currentUser.setName(user.getName());
        currentUser.setBirthday(user.getBirthday());
        return currentUser;
    }

    public void addFriend(User user, User friend, boolean isConfirmed) {

        Set<User> uFriends = usersFriends.computeIfAbsent(user.getId(), id -> new HashSet<>());
        uFriends.add(friend);

        Set<User> fFriends = usersFriends.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        fFriends.add(user);
    }

    public void deleteFriend(User user, User friend) {

        Set<User> uFriends = usersFriends.computeIfAbsent(user.getId(), id -> new HashSet<>());
        uFriends.remove(friend);

        Set<User> fFriends = usersFriends.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        fFriends.remove(user);
    }

    public Set<User> getFriends(User user) {
        return new HashSet<>(usersFriends.getOrDefault(user.getId(), Collections.emptySet()));
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        return new ArrayList<>(usersFriends.getOrDefault(userId, Collections.emptySet()).stream()
                .filter(user -> usersFriends.get(otherId).contains(user))
                .toList());
    }

    private long generateUserId() {
        return ++userId;
    }

}
