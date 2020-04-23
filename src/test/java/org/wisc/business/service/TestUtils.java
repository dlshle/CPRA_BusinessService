package org.wisc.business.service;

import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.BusinessModel.Course;
import org.wisc.business.model.BusinessModel.Professor;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.*;
import org.wisc.business.model.UserModel.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
    public static UserPV addNewTestUser(UserService userService) {
        try {
            return userService.add(User.builder().email("test@test.test1").password("123456").build());
        } catch (Exception e) {
            return null;
        }
    }

    public static void deleteAndTestUser(UserService userService, User user,
                                  boolean expected) {
        assertEquals(expected, userService.delete(user));
    }

    public static CommentPV addNewTestComment(CommentService commentService,
                                              Term term, User user) {
        return commentService.add(Comment.builder().termId(term.getId()).authorId(user.getId()).build());
    }

    public static void deleteAndTestComment(CommentService commentService,
                                            Comment comment, boolean expected) {
        assertEquals(expected, commentService.deleteRaw(comment));
    }

    public static TermPV addNewTestTerm(TermService termService) {
        return termService.add(Term.builder().name("test term").build());
    }

    public static void deleteAndTestTerm(TermService termService, Term term,
                                         boolean expected) {
        assertEquals(expected, termService.delete(term));
    }

    public static ProfessorPV addNewTestProfessor(ProfessorService professorService) {
        return professorService.add(Professor.builder().name("test professor").build());
    }

    public static void deleteAndTestProfessor(ProfessorService professorService, Professor professor, boolean expected) {
        assertEquals(expected, professorService.delete(professor));
    }

    public static CoursePV addNewTestCourse(CourseService courseService) {
        return courseService.add(Course.builder().name("test course").build());
    }

    public static void deleteAndTestCourse(CourseService courseService,
                                           Course course, boolean expected) {
        assertEquals(expected, courseService.delete(course));
    }
}
