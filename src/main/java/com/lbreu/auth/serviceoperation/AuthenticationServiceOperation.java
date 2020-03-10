package com.lbreu.auth.serviceoperation;

import com.lbreu.auth.dao.Device;
import com.lbreu.auth.dao.User;
import com.lbreu.auth.responses.AuthenticationResponse;
import com.lbreu.auth.utils.DBServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class AuthenticationServiceOperation {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DBServiceUtil dbServiceUtil;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceOperation.class);

    /**
     * Authenticates a user with a public private credential pair
     * and then associates the device that is being used with the user.
     * Returns a token that can be used to authenticate post authentication.
     *
     * @param publicCredential
     * @param privateCredential
     * @param deviceId
     * @return token
     */
    public AuthenticationResponse authenticateUser(String publicCredential, String privateCredential, String deviceId) {
        // todo: implement private credential encryption

        log.info("Executing authenticateUser SQL query");
        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(Response.Status.OK);

        BigInteger dId;
        try {
            dId = new BigInteger(deviceId);
        } catch (Exception e) {
            // 400 invalid device id
            response.setStatus(Response.Status.BAD_REQUEST);
            return response;
        }

        // todo: check that publicCredential and privateCredential do not contain malicious sql


        // get all users with the value of pub_cred equal to the user's public credential
        List<User> users = dbServiceUtil.getUser(publicCredential, privateCredential);

        // there is no registered user with the given public private credential pair
        if (users.isEmpty()) {
            log.info("User is unauthorized");
            // 401 unauthorized
            response.setStatus(Response.Status.UNAUTHORIZED);
            return response;
        }

        // check if the provided private credential matches with the one in the db
        for (User user : users) {
            log.info("user authenticated");

            // check if the device exists in the db
            List<Device> devices = dbServiceUtil.getDevice(dId);

            // if the device is not already in the db
            if (devices.isEmpty()) {
                response.setStatus(Response.Status.UNAUTHORIZED);
            } else {
                // should only run once with the current given constraints
                for (Device device : devices) {
                    dbServiceUtil.associateDevice(user.getId(), device.getDevice_id());
                    response.setToken(device.getToken());
                }
            }

            // the code below is for the case where users can come in on unauthenticated, unassociated devices
            // or devices that are already associated with another user. With the clarifications given, we
            // can assume that users are authenticating on authenticated but unassociated devices, so the code below is
            // unnecessary. However I am keeping it commented because it's neat.
            /*if (devices.isEmpty()) {
                // add device into device table
                String token = TokenUtil.createToken();
                dbServiceUtil.addDevice(dId, user.getId(), token);
                response.setStatus(Response.Status.OK);
                response.setToken(token);
            } else {
                // otherwise check if the device is associated with the user
                response.setStatus(Response.Status.UNAUTHORIZED); // change this if we want to log out previous user
                for (Device device : devices) {
                    if (device.getUser_id() != null && device.getUser_id().equals(user.getId())) {
                        response.setStatus(Response.Status.OK);
                        response.setToken(device.getToken());
                    }
                    // if a device present in the db but it is not associated with the current user
                }
            }*/
        }
        return response;
    }

    /**
     * Logs a user out. Disassociates the device that is being used to log out from the user.
     *
     * @param token
     */
    public AuthenticationResponse logoutUser(String token) {
        List<Device> devices = dbServiceUtil.getDeviceWithToken(token);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(Response.Status.OK);
        if (devices.isEmpty()) {
            // 401 unauthorized
            response.setStatus(Response.Status.UNAUTHORIZED);
            return response;
        }

        // disassociates the device being used to logout from the user
        for (Device device : devices) {
            if (device.getUser_id() == null) {
                // 401 unauthorized
                response.setStatus(Response.Status.UNAUTHORIZED);
                return response;
            }
            // this invalidates the token, thus logging out the user
            dbServiceUtil.disassociateDevice(device.getDevice_id());
        }
        log.info("user logged out");
        return response;
    }

    /**
     * Disassociates a device from the user.
     * This does not necessarily have to be the device that the user is currently authenticated on.
     *
     * @param token
     * @param deviceId
     * @return
     */
    public AuthenticationResponse disassociateDevice(String token, String deviceId) {

        // Check if token is valid
        List<Device> devices = dbServiceUtil.getDeviceWithToken(token);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(Response.Status.OK);
        if (devices.isEmpty()) {
            // 401 unauthorized
            response.setStatus(Response.Status.UNAUTHORIZED);
            return response;
        }

        // Token is valid
        // Find device we want to disassociate, it is not necessarily the device associated with the token

        BigInteger userId = null;
        for (Device device : devices) {
            // find the device and user id associated with the token
            userId = device.getUser_id();

            // if the device being disassociated is the device associated with the current token, disassociate it.
            if (device.getDevice_id().equals(deviceId)) {
                dbServiceUtil.disassociateDevice(device.getDevice_id());
                return response;
            }
        }
        if (userId == null) {
            // 401 unauthorized
            // token in not associated with any user
            response.setStatus(Response.Status.UNAUTHORIZED);
            return response;
        }
        BigInteger dId;
        try {
            dId = new BigInteger(deviceId);
        } catch (Exception e) {
            // 400 bad request
            response.setStatus(Response.Status.BAD_REQUEST);
            return response;
        }

        // we have verified that the token is valid
        // get the device we want to disassociate
        devices = dbServiceUtil.getDevice(dId);
        boolean authorized = false;
        for (Device device : devices) {
            // check that the authenticated user is associated with the device that they want to disassociate
            if (userId.equals(device.getUser_id())) {
                authorized = true;
                dbServiceUtil.disassociateDevice(device.getDevice_id());
            }
        }

        if (!authorized) {
            response.setStatus(Response.Status.UNAUTHORIZED);
        }
        return response;
    }

    /**
     * Associates a device with the user
     * This will automatically disassociate any other user already associated with the device
     *
     * @param token
     * @return
     */
    public AuthenticationResponse associateDevice(String token, String deviceId) {
        List<Device> devices = dbServiceUtil.getDeviceWithToken(token);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(Response.Status.OK);

        BigInteger dId;
        try {
            dId = new BigInteger(deviceId);
        } catch (Exception e) {
            // 400 bad request
            response.setStatus(Response.Status.BAD_REQUEST);
            return response;
        }

        if (devices.isEmpty()) {
            // 401 unauthorized
            response.setStatus(Response.Status.UNAUTHORIZED);
            return response;
        } else {
            for (Device device : devices) {
                // check if the token is associated with a logged in user
                if (device.getUser_id() == null) {
                    // 401 unauthorized
                    response.setStatus(Response.Status.UNAUTHORIZED);
                    return response;
                }
                if (device.getDevice_id().equals(dId)) {
                    // 400 bad request
                    // The device is already associated with the user
                    response.setStatus(Response.Status.BAD_REQUEST);
                    return response;
                }
                dbServiceUtil.associateDevice(device.getUser_id(), dId);
            }
        }

        // return the newly associated devices token in the response
        devices = dbServiceUtil.getDevice(dId);
        for (Device device : devices) {
            response.setToken(device.getToken());
        }


        return response;
    }
}
