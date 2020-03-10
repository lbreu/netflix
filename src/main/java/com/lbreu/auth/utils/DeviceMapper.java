package com.lbreu.auth.utils;

import com.lbreu.auth.dao.Device;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceMapper implements RowMapper<Device>{
    @Override
    public Device mapRow(ResultSet rs, int row) throws SQLException {
        Device device = new Device();
        device.setDevice_id(new BigInteger(rs.getString("device_id")));

        String userId = rs.getString("user_id");
        if (!StringUtils.isBlank(userId)) {
            device.setUser_id(new BigInteger(userId));
        }

        device.setStatus(rs.getString("status"));
        device.setToken(rs.getString("token"));
        return device;
    }
}
