package com.lbreu.auth.responses;

import javax.ws.rs.core.Response;

public class AuthenticationResponse {

    private Response.Status status;
    private String token;

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
