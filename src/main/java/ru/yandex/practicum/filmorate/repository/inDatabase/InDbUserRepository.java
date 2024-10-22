package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class InDbUserRepository extends InDbBaseRepository<User> implements UserRepository {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
//    private static final String INSERT_FRIEND_QUERY = "INSERT INTO user_friend (user_Id, friend_id, isConfirmed) VALUES (?, ?, ?)";
//    private static final String DELETE_FRIEND_QUERY = "DELETE from user_friend WHERE  user_id = ? AND friend_id = ?";
//    private static final String SELECT_FRIENDS_QUERY = "SELECT * FROM users WHERE id IN " +
//            "(SELECT uf.friend_id FROM users u LEFT JOIN user_friend uf ON u.id = uf.user_id WHERE u.id = ?)";
//    private static final String SELECT_COMMON_FRIENDS_QUERY = "WITH cte AS " +
//            "(SELECT uf.friend_id " +
//            " FROM users u " +
//            " LEFT JOIN user_friend uf ON u.id = uf.user_id " +
//            " WHERE u.id = ?) " +
//            "SELECT u1.* " +
//            "FROM users u " +
//            " LEFT JOIN user_friend uf ON u.id = uf.user_id " +
//            " INNER JOIN cte ON uf.friend_id = cte.friend_id " +
//            " INNER JOIN users u1 ON u1.id = uf.friend_id " +
//            "WHERE u.id = ?";


    public InDbUserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> get(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public List<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public User save(User user) {
        long id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

//    public void addFriend(User user, User friend, boolean isConfirmed) {
//        insert(INSERT_FRIEND_QUERY,
//                user.getId(),
//                friend.getId(),
//                isConfirmed
//        );
//    }

//    public void deleteFriend(User user, User friend) {
//        delete(DELETE_FRIEND_QUERY,
//                user.getId(),
//                friend.getId()
//        );
//
//    }

//    public Set<User> getFriends(User user) {
//        return new HashSet<>(findMany(SELECT_FRIENDS_QUERY, user.getId()));
//    }

//    public List<User> getCommonFriends(long userId, long otherId) {
//        return findMany(SELECT_COMMON_FRIENDS_QUERY, userId, otherId);
//    }

}
