package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class InDbUserFriendRepository extends InDbBaseRepository<UserFriend> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM user_friend";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO user_friend (user_id, friend_id, isConfirmed) VALUES (?, ?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE from user_friend WHERE  user_id = ? AND friend_id = ?";
    private static final String SELECT_FRIENDS_QUERY = "SELECT * FROM user_friend WHERE  user_id = ?";
    private static final String SELECT_COMMON_FRIENDS_QUERY = "select * from user_friend where user_id in(?,?)";


    public InDbUserFriendRepository(JdbcTemplate jdbc, RowMapper<UserFriend> mapper) {
        super(jdbc, mapper);
    }

    public List<UserFriend> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public void addFriend(User user, User friend, boolean isConfirmed) {
        insert(INSERT_FRIEND_QUERY,
                user.getId(),
                friend.getId(),
                isConfirmed
        );
    }

    public void deleteFriend(User user, User friend) {
        delete(DELETE_FRIEND_QUERY,
                user.getId(),
                friend.getId()
        );
    }

    public Set<UserFriend> getFriends(User user) {
        return new HashSet<>(findMany(SELECT_FRIENDS_QUERY, user.getId()));
    }

    public List<UserFriend> getCommonFriends(long userId, long otherId) {
        return findMany(SELECT_COMMON_FRIENDS_QUERY, userId, otherId);
    }

}
