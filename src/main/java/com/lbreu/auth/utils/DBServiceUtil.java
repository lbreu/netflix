package com.lbreu.auth.utils;

import com.lbreu.auth.dao.Device;
import com.lbreu.auth.dao.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class DBServiceUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(DBServiceUtil.class);

    /**
     * Get the list of users that have the given publicCredential and privateCredential
     *
     * @param publicCredential
     * @param privateCredential
     * @return
     */
    public List<User> getUser(String publicCredential, String privateCredential) {
        log.info("get user");
        List<User> listOfUsers = jdbcTemplate.query("SELECT * FROM user WHERE pub_cred = \'" + publicCredential + "\' AND priv_cred = \'" + privateCredential + "\'", new UserMapper());

        return listOfUsers;
    }

    /**
     * Get the list of devices that have the given token
     *
     * @param token
     * @return list of devices
     */
    public List<Device> getDeviceWithToken(String token) {
        log.info("get device with token");
        List<Device> devices = jdbcTemplate.query("SELECT * FROM device WHERE token = \'" + token + "\'", new DeviceMapper());

        return devices;
    }

    /**
     * Get the list of devices that have the given deviceId
     *
     * @param deviceId
     * @return
     */
    public List<Device> getDevice(BigInteger deviceId) {
        log.info("get device with device id");
        List<Device> listOfDevices = jdbcTemplate.query("SELECT * FROM device WHERE device_id = "+ deviceId,  new DeviceMapper());

        return listOfDevices;
    }

    /**
     * Add a device to the device table.
     * This method is unused now that we no longer support user authentication on unauthenticated devices.
     *
     * @param deviceId
     * @param userId
     * @param token
     */
    public void addDevice(BigInteger deviceId, BigInteger userId, String token) {
        jdbcTemplate.execute("INSERT INTO device (user_id, device_id, status, token) VALUES (\'"+ userId +"\', \'"+ deviceId +"\', \'A\', \'"+ token +"\')");
        log.info("device inserted into db");
    }

    /**
     * Updates a device to be associated with the given user id.
     *
     * @param userId
     * @param deviceId
     */
    public void associateDevice(BigInteger userId, BigInteger deviceId) {
        jdbcTemplate.execute("UPDATE device SET user_id = \'"+ userId +"\' WHERE device_id = "+ deviceId);
        log.info("deviceid="+ deviceId + "associated with userid="+ userId);
    }

    /**
     * Remove a device to the device table
     *
     * @param deviceId
     */
    public void disassociateDevice(BigInteger deviceId) {
        //jdbcTemplate.execute("DELETE FROM device WHERE user_id = \'"+ userId +"\', device_id = \'"+ deviceId +"\', token = \'"+ token +"\')");
        jdbcTemplate.execute("UPDATE device SET user_id = NULL WHERE device_id = "+ deviceId);
        log.info("deviceId="+ deviceId +" disassociated from user");
    }

    /**
     * Create a user with the given public and private credentials
     *
     * @param publicCredential
     * @param privateCredential
     */
    public void createUser(String publicCredential, String privateCredential) {
        jdbcTemplate.execute("INSERT INTO user (pub_cred, priv_cred) VALUES (\'"+ publicCredential +"\', \'"+ privateCredential +"\')");
    }

    /**
     * Create a device with the given device id.
     *
     * @param deviceId
     * @param token
     */
    public void createDevice(BigInteger deviceId, String token ) {
        jdbcTemplate.execute("INSERT INTO device (device_id, status, token) VALUES ("+ deviceId +",\'A\', \'"+ token  +"\')");
    }

    public List<User> retrieveUserWithId(BigInteger userId) {
        List<User> users = jdbcTemplate.query("SELECT * FROM user WHERE id = \'"+ userId +"\'", new UserMapper());
        return users;
    }

    /**
     * Get the list of users that are associated with the given userId.
     *
     * @param userId
     * @return user
     */
    public List<User> getUser(BigInteger userId) {
        log.info("get user");

        List<User> listOfUsers = jdbcTemplate.query("SELECT * FROM user WHERE id = \'" + userId + "\'", new UserMapper());

        return listOfUsers;
    }

    /**
     * Get the list of devices that have the userId
     *
     * Note: this may return more then one device.
     *
     * @param userId
     * @return devices
     */
    public List<Device> getDeviceWithUserId(BigInteger userId) {
        log.info("get device with user id");
        List<Device> listOfDevices = jdbcTemplate.query("SELECT * FROM device WHERE user_id = "+ userId,  new DeviceMapper());

        return listOfDevices;
    }


}
