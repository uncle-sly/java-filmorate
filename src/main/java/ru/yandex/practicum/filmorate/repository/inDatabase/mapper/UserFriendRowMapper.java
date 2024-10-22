package ru.yandex.practicum.filmorate.repository.inDatabase.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserFriend;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserFriendRowMapper implements RowMapper<UserFriend> {


    @Override
    public UserFriend mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserFriend uf = new UserFriend();
        uf.setUserId(rs.getLong("user_id"));
        uf.setFriendId(rs.getLong("friend_id"));
        uf.setConfirmed(rs.getBoolean("isConfirmed"));

        return uf;
    }
}
