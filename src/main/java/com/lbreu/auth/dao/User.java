package com.lbreu.auth.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;

/**
 * Represents a user
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private BigInteger id;
    private String pub_cred;
    private String priv_cred;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getPub_cred() {
        return pub_cred;
    }

    public void setPub_cred(String pub_cred) {
        this.pub_cred = pub_cred;
    }

    public String getPriv_cred() {
        return priv_cred;
    }

    public void setPriv_cred(String priv_cred) {
        this.priv_cred = priv_cred;
    }

}
