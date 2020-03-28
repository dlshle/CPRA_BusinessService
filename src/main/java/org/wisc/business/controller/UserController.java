package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.PVModels.UserPV;
import org.wisc.business.model.UserModel.User;
import org.wisc.business.service.AuthenticationService;
import org.wisc.business.service.DuplicateEmailException;
import org.wisc.business.service.DuplicateUserNameException;
import org.wisc.business.service.UserService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Resource
    UserService userService;

    @Resource
    AuthenticationService authenticationService;

    @PostMapping("")
    public @ResponseBody
    AjaxResponse addUser(@RequestBody User user) {
        UserPV newUser;
        if (user.getUsername() == null || user.getUsername().length() < 5)
            return AjaxResponse.error(400,
                    "Invalid username" + user.getUsername());
        if (user.getEmail() == null || user.getEmail().length() < 6)
            return AjaxResponse.error(400, "Invalid email("+user.getEmail()+
                    ")");
        try {
            newUser = userService.add(user);
        } catch (DuplicateUserNameException dune) {
            return AjaxResponse.error(400,
                    "Duplicate username(" + user.getUsername()+")");
        } catch (DuplicateEmailException dee) {
            return AjaxResponse.error(400,
                    "Duplicate email("+user.getEmail()+")");
        }
        return (newUser == null?AjaxResponse.error(400,
                "User("+user.getId()+") already exists."):
                AjaxResponse.success(newUser));
    }

    @PutMapping("")
    public @ResponseBody AjaxResponse updateUser(@RequestHeader("token") String token
            ,@RequestBody UserPV user) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        if (!currentUser.getId().equals(user.getId()) && !currentUser.isAdmin())
            return AjaxResponse.error(400,
                    "Only the user("+user.getUsername()+") or admin can " +
                            "update this account.");
        UserPV newUser = null;
        try {
            newUser = userService.update(user.toRawType());
        } catch (DuplicateUserNameException dune) {
            return AjaxResponse.error(400,
                    "Duplicate username(" + user.getUsername()+")");
        } catch (DuplicateEmailException dee) {
            return AjaxResponse.error(400,
                    "Duplicate email("+user.getEmail()+")");
        }
        if (newUser == null) {
            return AjaxResponse.error(400, "User("+user.getId()+") is " +
                    "invalid.");
        }
        newUser.setPasswd("");
        return AjaxResponse.success(newUser);
    }

    @GetMapping("")
    public @ResponseBody AjaxResponse getAllUsers() {
        return AjaxResponse.success(userService.all());
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getUser(@PathVariable String id) {
        UserPV user = userService.findById(id);
        if (user == null) {
            return AjaxResponse.error(400, "Invalid user id(" + id + ")");
        }
        return AjaxResponse.success(user);
    }

    @GetMapping("/username/{username}")
    public @ResponseBody AjaxResponse getCourseByUserName(@PathVariable String username) {
        UserPV user = userService.findByUsername(username);
        if (user == null) {
            return AjaxResponse.error(400, "Invalid username(" + username + ")");
        }
        return AjaxResponse.success(user);
    }

    @GetMapping("/email/{email}")
    public @ResponseBody AjaxResponse getCourseByEmail(@PathVariable String email) {
        UserPV user = userService.findByEmail(email);
        if (user == null) {
            return AjaxResponse.error(400, "Invalid email(" + email + ")");
        }
        return AjaxResponse.success(user);
    }

    @GetMapping("/name/{name}")
    public @ResponseBody AjaxResponse getCourseByName(@PathVariable String name) {
        return AjaxResponse.success(userService.findByName(name));
    }

    @DeleteMapping("")
    public @ResponseBody
    AjaxResponse deleteUser(@RequestHeader("token") String token,
                            @RequestBody UserPV user) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        if (!currentUser.getId().equals(user.getId()) && !currentUser.isAdmin())
            return AjaxResponse.error(400,
                    "Only the user("+user.getUsername()+") or admin can " +
                            "resign this account.");
        if (userService.delete(user.toRawType()))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid user("+user.getId()+")");
    }
}
