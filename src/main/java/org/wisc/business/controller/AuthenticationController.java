package org.wisc.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.UserModel.User;
import org.wisc.business.service.AuthenticationService;

/**
 * Auth Controller
 */
@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public @ResponseBody
    AjaxResponse login(@RequestBody User user) {
        if (user == null || (user.getUsername() == null && user.getEmail() == null) ||
                user.getPassword() == null || user.getPassword().isEmpty())
            return AjaxResponse.error(400, "Invalid login parameter.");
        String loginResult = authenticationService.login(user);
        if (loginResult == null || loginResult.equals("Invalid Password")) {
            return AjaxResponse.error(400, (user.getEmail() == null?
                    "Incorrect username or password":"Incorrect email or " +
                    "password."));
        }
        return AjaxResponse.success(loginResult);
    }

    @GetMapping("/token/{token}")
    public @ResponseBody AjaxResponse validateToken(@PathVariable String token) {
        if (token == null || token.isEmpty())
            return AjaxResponse.error(400, "Invalid token(empty or null)");
        return AjaxResponse.success(authenticationService.isValidToken(token));
    }
}
