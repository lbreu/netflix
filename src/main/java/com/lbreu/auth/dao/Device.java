package com.lbreu.auth.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;

/**
 * Represents a device
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {

    private BigInteger user_id;
    private BigInteger device_id;
    private String status;
    private String token;

    public BigInteger getUser_id() {
        return user_id;
    }

    public void setUser_id(BigInteger user_id) {
        this.user_id = user_id;
    }

    public BigInteger getDevice_id() {
        return device_id;
    }

    public void setDevice_id(BigInteger device_id) {
        this.device_id = device_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
