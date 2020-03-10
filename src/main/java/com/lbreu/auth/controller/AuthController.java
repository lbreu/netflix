package com.lbreu.auth.controller;

import java.util.concurrent.ExecutionException;

import com.lbreu.auth.exceptions.BadRequestException;
import com.lbreu.auth.exceptions.UnauthorizedException;
import com.lbreu.auth.responses.AuthenticationResponse;
import com.lbreu.auth.serviceoperation.AuthenticationServiceOperation;
import com.lbreu.auth.serviceoperation.CreationServiceOperation;
import com.lbreu.auth.utils.AuthResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;


@RestController
public class AuthController {

    @Autowired
    AuthenticationServiceOperation authenticationServiceOperation;

    @Autowired
    CreationServiceOperation creationServiceOperation;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    /**
     * Authenticates a user with a public + private credential pair.
     * The device the user is authenticating on, will be associated with the user.
     *
     * @param publicCredential
     * @param privateCredential
     * @param deviceId
     * @return
     * @throws BadRequestException
     * @throws UnauthorizedException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @RequestMapping(value = "/user/auth", method = RequestMethod.POST, params = {"publicCredential", "privateCredential", "deviceId"})
    @ResponseBody Response authenticateUser(@RequestParam(value = "publicCredential") String publicCredential,
                                            @RequestParam(value = "privateCredential") String privateCredential,
                                            @RequestParam(value = "deviceId") String deviceId) throws BadRequestException, UnauthorizedException, InterruptedException, ExecutionException {

        log.info("Entering authenticate user");

        AuthenticationResponse authResponse = authenticationServiceOperation.authenticateUser(publicCredential, privateCredential, deviceId);

        AuthResponseHandler.handleErrors(authResponse);

        Response response = Response.status(authResponse.getStatus()).entity(authResponse.getToken()).build();

        log.info("Exiting authenticate user");
        return response;
    }

    /**
     * Logs a user out and disassociates the device used to logout.
     * Verifies that the user is authenticated and associated with the device before logging out
     *
     * @return response
     */
    @RequestMapping(value =  "/user/logout", method = RequestMethod.POST, params = {"token"})
    Response logOut(@RequestParam(value = "token") String token) {
        log.info("Entering logout ");

        AuthenticationResponse authResponse = authenticationServiceOperation.logoutUser(token);

        AuthResponseHandler.handleErrors(authResponse);

        Response response = Response.status(authResponse.getStatus()).build();
        log.info("Exiting logout ");
        return response;
    }

    /**
     * Takes an authentication token and a device id
     * Checks if the token is a valid token paired with an authenticated user
     * to disassociate from the authenticated user.
     *
     * Note: the device id does not need to be the device associated with the given authentication token,
     * however it does need to be assoicated with the user associated with the authentication token.
     *
     * @param token
     * @param deviceId
     * @return response
     */
    @RequestMapping(value =  "/device/disassociate", method = RequestMethod.POST, params = {"token", "deviceId"})
    Response disassociateDevice(@RequestParam(value = "token") String token,
                                @RequestParam(value = "deviceId") String deviceId) {
        log.info("Entering disassociate device");

        AuthenticationResponse authResponse = authenticationServiceOperation.disassociateDevice(token, deviceId);

        AuthResponseHandler.handleErrors(authResponse);

        Response response = Response.status(authResponse.getStatus()).build();
        log.info("Exiting disassociate device");
        return response;
    }

    /**
     * Takes a authentication token and device id
     * Checks if the token is a valid token paired with an authenticated user
     * and associates the given device with the user.
     *
     * Returns the newly associated device's authentication token.
     *
     * @param token
     * @param deviceId
     * @return token
     */
    @RequestMapping(value = "/device/associate", method = RequestMethod.POST, params = {"token", "deviceId"})
    Response associateDevice(@RequestParam(value = "token") String token,
                             @RequestParam(value = "deviceId") String deviceId) {
        log.info("Entering associate device");

        AuthenticationResponse authResponse = authenticationServiceOperation.associateDevice(token, deviceId);

        AuthResponseHandler.handleErrors(authResponse);

        Response response = Response.status(authResponse.getStatus()).entity(authResponse.getToken()).build();

        log.info("Exiting associate device");
        return response;
    }


    /**
     * Creates a user
     *
     * @param publicCredential
     * @param privateCredential
     */
    @RequestMapping(value = "user/create", method = RequestMethod.POST, params = {"publicCredential", "privateCredential"})
    void createUser(@RequestParam(value = "publicCredential") String publicCredential,
                    @RequestParam(value = "privateCredential") String privateCredential) {
        creationServiceOperation.createUser(publicCredential, privateCredential);
    }

    /**
     * Creates a device.
     *
     * @param deviceId
     */
    @RequestMapping(value = "device/create", method = RequestMethod.POST, params = {"deviceId"})
    Response createDevice(@RequestParam(value = "deviceId") String deviceId) {
        log.info("Entering create device");
        AuthenticationResponse authResponse = creationServiceOperation.createDevice(deviceId);
        AuthResponseHandler.handleErrors(authResponse);
        Response response = Response.status(authResponse.getStatus()).entity(authResponse.getToken()).build();
        log.info("Exiting create device");
        return response;
    }


}