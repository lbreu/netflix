package com.lbreu.auth.serviceoperation;

import com.lbreu.auth.dao.Device;
import com.lbreu.auth.dao.User;
import com.lbreu.auth.utils.DBServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class RetrievalServiceOperation {

    @Autowired
    private DBServiceUtil dbServiceUtil;

    private static final Logger log = LoggerFactory.getLogger(RetrievalServiceOperation.class);

    /**
     * Returns the token associated user's id and public credentials.
     *
     * @param token
     * @return user
     */
    public String retrieveUser(String token) {

        // Get device associated with token
        List<Device> devices = dbServiceUtil.getDeviceWithToken(token);

        if (devices.isEmpty()) {
            return null;
        }

        // Only runs once
        for (Device device : devices) {
            // Get user associated with device associated with token
            List<User> users = dbServiceUtil.getUser(device.getUser_id());

            if (users.isEmpty()) {
                return null;
            }

            // Only runs once
            for (User user : users) {
                return user.toString();
            }

        }



        return null;
    }

    /**
     * Returns all the devices that are associated with the user associated with the given token.
     *
     * @param token
     * @return devices
     */
    public String retrieveDevices(String token) {

        // Get device associated with token
        List<Device> devices = dbServiceUtil.getDeviceWithToken(token);

        if (devices.isEmpty()) {
            return null;
        }

        // Only runs once
        for (Device device : devices) {
            // Get all devices associated with the user id associated with the device associated with the token
            List<Device> allDevices = dbServiceUtil.getDeviceWithUserId(device.getUser_id());

            if (allDevices.isEmpty()) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (Device d : allDevices) {
                sb.append(d.toString() + " \n");
            }
            return sb.toString();
        }


        return null;
    }

}
