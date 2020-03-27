package org.wisc.business.service;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.authentication.SecurityUtil;
import org.wisc.business.model.UserModel.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class AuthenticationService {
    @Autowired
    UserService userService;

    public String login(User user) {
        User userFromDatabase = null;
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            // login with email
            userFromDatabase = userService.findByEmail(user.getEmail());
        } else if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            userFromDatabase = userService.findByUsername(user.getUsername());
        }
        if (userFromDatabase == null)
            return null;
        if (!SecurityUtil.hashPassword(user.getPassword(),
                userFromDatabase.getSalt()).equals(userFromDatabase.getPassword()))
            return "Invalid Password";
        return SecurityUtil.generateToken(user);
    }

    public boolean isValidToken(String token) {
        if (token == null) return false;
        Date issueDate = JWT.decode(token).getIssuedAt();
        if (System.currentTimeMillis() - issueDate.getTime() > SecurityUtil.EXPIRATION_PERIOD)
            return false;
        return true;
    }

    @Deprecated
    public boolean isLoggedIn(HttpServletRequest request) {
        if (request == null)
            return false;
        String token  =request.getHeader("token");
        if (token == null)
            return false;
        return isValidToken(token);
    }
}
