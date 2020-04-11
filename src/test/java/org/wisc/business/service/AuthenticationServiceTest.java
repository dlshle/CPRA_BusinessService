package org.wisc.business.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wisc.business.model.UserModel.User;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void login() {
        User testU = User.builder().email("badger@wisc.edu").password("123456").build();
        String token = authenticationService.login(testU);
        Assert.assertThat(authenticationService.isValidToken(token), is(true));
    }

    @Test
    void isValidToken() {
    }

    @Test
    void getCurrentUser() {
    }

    @Test
    void isLoggedIn() {
    }
}