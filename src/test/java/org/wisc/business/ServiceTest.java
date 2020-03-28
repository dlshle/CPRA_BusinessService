package org.wisc.business;

import com.auth0.jwt.JWT;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wisc.business.model.UserModel.User;
import org.wisc.business.service.*;

import javax.annotation.Resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class ServiceTest {
    @Resource
    AuthenticationService authenticationService;

    @Resource
    CommentService commentService;

    @Resource
    CourseService courseService;

    @Resource
    ProfessorService professorService;

    @Resource
    TermService termService;

    @Resource
    UserService userService;

    public static final User testUser =
            User.builder().username("admin").password("123456").build();

    @Test
    void testAuthService() {
        String token = authenticationService.login(testUser);
        // test issueDate in time range 1000 ms
        boolean isWithinRange =
                (System.currentTimeMillis() - JWT.decode(token).getIssuedAt().getTime() <= 1000);
        assertTrue(isWithinRange);

        assertTrue(authenticationService.isValidToken(token));

        assertFalse(authenticationService.isValidToken(null));
    }

    @Test
    void testCommentService() {

    }
}
