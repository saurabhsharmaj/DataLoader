package com.ebit.loader.common;

import com.ebit.loader.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        User user = new User();

        user.setId(rs.getLong("id"));
        user.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        user.setCreatedBy(rs.getString("created_by"));
        user.setEmailAddress(rs.getString("email_address"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setUpdatedAt(
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
        user.setUpdatedBy(rs.getString("updated_by"));

        return user;
    }
}