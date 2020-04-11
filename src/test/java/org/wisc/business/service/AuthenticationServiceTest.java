package org.wisc.business.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.model.UserModel.User;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void login() {
        User testU = User.builder().email("badger@wisc.edu").password("123456").build();
        String token = authenticationService.login(testU);
        Assert.assertThat("valid user login test", authenticationService.isValidToken(token), is(true));
        User testU2 = User.builder().email("badger@wisc.edu").password("111111").build();
        String token2 = authenticationService.login(testU2);
        Assert.assertThat("invalid user login test", authenticationService.isValidToken(token2), is(false));
    }

    @Test
    void isValidToken() {
        final String validToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJhdWQiOiI1ZThmZWYwYzk2NjM1YTY1ZGVhYTRlYjgiLCJpYXQiOjE1ODY1NzYxNTZ9.5VXIoy2IpcwP5r8ig6UxcsYKcJGRCZ1_3bGAufLHayU";
        final String validTokenMessage = "Test with a valid user Token";
        final String inValidToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJhdWQiOiI1ZThmZWYwYzk2NjM1YTY1ggVhYTRlYjgiLCJpYXQiOjE1ODY1NzYxNTZ9.5VXIoy2IpcwP5r8ig6UxcsYKcJGRCZ1_3bGAufLHayU";
        final String inValidTokenMessage = "Test with invalid Token";
        Assert.assertThat(validTokenMessage, authenticationService.isValidToken(validToken), is(true));
        Assert.assertThat(inValidTokenMessage, authenticationService.isValidToken(inValidToken), is(false));
    }
}