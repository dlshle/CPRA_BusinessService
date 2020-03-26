package org.wisc.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.authentication.UserAuthManager;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.UserModel.User;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    @PostMapping("/login")
    public @ResponseBody
    AjaxResponse login(@RequestBody User user) {
        if (user.getEmail() != null) {
            // get user
        }
        if (user.getUsername() == null) {
            AjaxResponse.error(400, "Invalid email or username.");
        }
        // get user by username

        // set jwt

        // use userAuthManager to manage the loggedin user
    }
}
