package org.wisc.business.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void login() {

    }

    @Test
    void isValidToken() {
        final String validToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJhdWQiOiI1ZThmZWYwYzk2NjM1YTY1ZGVhYTRlYjgiLCJpYXQiOjE1ODY1NzYxNTZ9.5VXIoy2IpcwP5r8ig6UxcsYKcJGRCZ1_3bGAufLHayU";
        final String validTokenMessage = "Test with a valid user Token";
        final String inValidToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJhdWQiOiI1ZThmZWYwYzk2NjM1YTY1ggVhYTRlYjgiLCJpYXQiOjE1ODY1NzYxNTZ9.5VXIoy2IpcwP5r8ig6UxcsYKcJGRCZ1_3bGAufLHayU";
        final String inValidTokenMessage = "Test with invalid Token";
        assertTrue(authenticationService.isValidToken(validToken), validTokenMessage);
        assertFalse(authenticationService.isValidToken(inValidToken), inValidTokenMessage);
    }

}