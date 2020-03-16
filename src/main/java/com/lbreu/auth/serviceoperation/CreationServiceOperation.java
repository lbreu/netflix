package com.lbreu.auth.serviceoperation;

import com.lbreu.auth.responses.AuthenticationResponse;
import com.lbreu.auth.utils.DBServiceUtil;
import com.lbreu.auth.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.math.BigInteger;

@Component
public class CreationServiceOperation {

    @Autowired
    DBServiceUtil dbServiceUtil;

    private static final Logger log = LoggerFactory.getLogger(CreationServiceOperation.class);

    /**
     * Create a user with the given public and private credentials.
     *
     * @param publicCredential
     * @param privateCredential
     */
    public void createUser(String publicCredential, String privateCredential) {
        dbServiceUtil.createUser(publicCredential, privateCredential);
        log.info("user created");
    }

    /**
     * Creates a device with the given device id.
     *
     * @param deviceId
     * @return
     */
    public AuthenticationResponse createDevice(String deviceId) {
        BigInteger dId;
        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus(Response.Status.OK);
        try {
            dId = new BigInteger(deviceId);
        } catch (Exception e) {
            response.setStatus(Response.Status.BAD_REQUEST);
            return response;
        }

        String token = TokenUtil.createToken();
        response.setToken(token);

        dbServiceUtil.createDevice(dId, token);
        return response;
    }
}
