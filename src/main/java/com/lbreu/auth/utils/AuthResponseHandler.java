package com.lbreu.auth.utils;

import com.lbreu.auth.exceptions.BadRequestException;
import com.lbreu.auth.exceptions.UnauthorizedException;
import com.lbreu.auth.responses.AuthenticationResponse;

import javax.ws.rs.core.Response;

public class AuthResponseHandler {

    public static void handleErrors(AuthenticationResponse authResponse) throws BadRequestException, UnauthorizedException {
        if (authResponse.getStatus().equals(Response.Status.BAD_REQUEST)) {
            throw new BadRequestException();
        }
        if (authResponse.getStatus().equals(Response.Status.UNAUTHORIZED)) {
            throw new UnauthorizedException();
        }
    }
}
