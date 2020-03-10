package com.lbreu.auth.utils;

import com.lbreu.auth.dao.User;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User>{
    @Override
    public User mapRow(ResultSet rs, int row) throws SQLException {
        User user = new User();
        user.setId(new BigInteger(rs.getString("id")));
        user.setPub_cred(rs.getString("pub_cred"));
        user.setPriv_cred(rs.getString("priv_cred"));
        return user;
    }
}
