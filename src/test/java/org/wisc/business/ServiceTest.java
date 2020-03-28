package org.wisc.business;

import com.auth0.jwt.JWT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.controller.AuthenticationController;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.CommentPV;
import org.wisc.business.model.PVModels.TermPV;
import org.wisc.business.model.UserModel.User;
import org.wisc.business.service.*;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.Assert.*;

@SpringBootTest(classes = BusinessServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    public void testAuthService() {
        String token = authenticationService.login(testUser);
        // test issueDate in time range 1000 ms
        boolean isWithinRange =
                (System.currentTimeMillis() - JWT.decode(token).getIssuedAt().getTime() <= 1000);
        assertTrue(isWithinRange);

        assertTrue(authenticationService.isValidToken(token));

        assertFalse(authenticationService.isValidToken(null));
    }

    @Test
    public void testCommentService() {
        // no term id author id, fail
        Comment c1 = Comment.builder().content("content").build();
        assertNull(commentService.add(c1));

        // has author id, no tid
        c1.setAuthorId(testUser.getId());
        assertNull(c1);

        TermPV tpv = termService.all().get(0);
        Term t = tpv.toRawType();


        c1.setTermId(t.getId());
        CommentPV commentFromDatabase = commentService.add(c1);
        assertNotNull(commentFromDatabase);

        // check author
        assertEquals(commentFromDatabase.getAuthor().getId(), testUser.getId());

        // check last editor
        assertEquals(commentFromDatabase.getLastEditedBy().getId(), testUser.getId());

        // check date
        assertTrue(commentFromDatabase.getLastModifiedDate().before(new Date()));
    }
}
