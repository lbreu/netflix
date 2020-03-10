package com.lbreu.auth;

import com.google.gson.Gson;
import com.lbreu.auth.controller.AuthController;
import com.lbreu.auth.response.AuthServiceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Functional Tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    AuthController authController;

    /**
     * Test the entire flow
     *
     * @throws Exception
     */
	@Test
	public void testFlow() throws Exception{

	    // create a user
        mvc.perform(MockMvcRequestBuilders.post("/user/create").param("publicCredential", "hello").param("privateCredential", "world"));

        // create a device
        mvc.perform(MockMvcRequestBuilders.post("/device/create").param("deviceId", "123456"));

        // authenticate the user with that device
        ResultActions r = mvc.perform(MockMvcRequestBuilders.post("/user/auth").param("publicCredential", "hello").param("privateCredential", "world").param("deviceId", "123456"));

        MockHttpServletResponse response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(200, response.getStatus());

        // retrieve the authentication token
        Gson gson = new Gson();
        AuthServiceResponse authServiceResponse = gson.fromJson(response.getContentAsString(), AuthServiceResponse.class);
        String token = authServiceResponse.getEntity();
        Assert.assertNotNull(token);

        // create another device
        mvc.perform(MockMvcRequestBuilders.post("/device/create").param("deviceId", "123457"));

        // associate the new device with the user using the token from authentication
        r = mvc.perform(MockMvcRequestBuilders.post("/device/associate").param("token", token).param("deviceId", "123457"));

        response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(200, response.getStatus());

        // retrieve the authentication token;
        authServiceResponse = gson.fromJson(response.getContentAsString(), AuthServiceResponse.class);
        String token2 = authServiceResponse.getEntity();

        Assert.assertNotNull(token2);
        Assert.assertNotEquals(token, token2);

        // disassociate the original device using the new token
        r = mvc.perform(MockMvcRequestBuilders.post("/device/disassociate").param("token", token2).param("deviceId", "123456"));

        response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(200, response.getStatus());

        // try to disassociate a non-associated device and check if the original device got disassociated, two birds with one call
        r = mvc.perform(MockMvcRequestBuilders.post("/device/disassociate").param("token", token2).param("deviceId", "123456"));

        response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(401, response.getStatus());

        // try to log out using the old token
        r = mvc.perform(MockMvcRequestBuilders.post("/user/logout").param("token", token));

        response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(401, response.getStatus());

        // log out
        r = mvc.perform(MockMvcRequestBuilders.post("/user/logout").param("token", token2));

        response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(200, response.getStatus());
	}

    /**
     * Test logout fail cases
     *
     * @throws Exception
     */
	@Test
    public void testLogoutFail() throws Exception {

	    // create device
        mvc.perform(MockMvcRequestBuilders.post("/device/create").param("deviceId", "1234578"));

        // log out
        ResultActions r = mvc.perform(MockMvcRequestBuilders.post("/user/logout").param("token", "faketoken"));

        MockHttpServletResponse response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(401, response.getStatus());
    }

    /**
     * Test authentication fail cases
     * @throws Exception
     */
    @Test
    public void testAuthFail() throws Exception {

        // create a user
        mvc.perform(MockMvcRequestBuilders.post("/user/create").param("publicCredential", "testinguser1").param("privateCredential", "password!"));

        // create device
        mvc.perform(MockMvcRequestBuilders.post("/device/create").param("deviceId", "123456789"));

        // authenticate the user with that device
        ResultActions r = mvc.perform(MockMvcRequestBuilders.post("/user/auth").param("publicCredential", "testinguser1").param("privateCredential", "wrongpassword").param("deviceId", "123456789"));

        MockHttpServletResponse response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(401, response.getStatus());

    }

    /**
     * Test associating user fail cases
     * @throws Exception
     */
    @Test
    public void testAssociateUserFail() throws Exception {

        // create a user
        mvc.perform(MockMvcRequestBuilders.post("/user/create").param("publicCredential", "testinguser2").param("privateCredential", "password!"));

        // create device
        mvc.perform(MockMvcRequestBuilders.post("/device/create").param("deviceId", "1234567892"));

        // authenticate the user with that device
        ResultActions r = mvc.perform(MockMvcRequestBuilders.post("/user/auth").param("publicCredential", "testinguser2").param("privateCredential", "password!").param("deviceId", "1234567892"));

        MockHttpServletResponse response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(200, response.getStatus());

        // retrieve the authentication token
        Gson gson = new Gson();
        AuthServiceResponse authServiceResponse = gson.fromJson(response.getContentAsString(), AuthServiceResponse.class);
        String token = authServiceResponse.getEntity();
        Assert.assertNotNull(token);

        // associate the already associated device
        r = mvc.perform(MockMvcRequestBuilders.post("/device/associate").param("token", token).param("deviceId", "1234567892"));

        response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(400, response.getStatus());

        // create new device
        mvc.perform(MockMvcRequestBuilders.post("/device/create").param("deviceId", "1234567890"));

        r = mvc.perform(MockMvcRequestBuilders.post("/device/associate").param("token", "faketoken").param("deviceId", "1234567890"));


        response = r.andReturn().getResponse();

        // check the status
        Assert.assertEquals(401, response.getStatus());

    }

}
