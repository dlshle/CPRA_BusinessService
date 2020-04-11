package org.wisc.business.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.model.UserModel.User;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void login() {
        User testU = User.builder().email("badger@wisc.edu").password("123456").build();
        String token = authenticationService.login(testU);
        Assert.assertThat(authenticationService.isValidToken(token), is(true));
        User testU2 = User.builder().email("badger@wisc.edu").password("111111").build();
        String token2 = authenticationService.login(testU2);
        Assert.assertThat(authenticationService.isValidToken(token2), is(false));
    }

    @Test
    void isValidToken() {
    }
}