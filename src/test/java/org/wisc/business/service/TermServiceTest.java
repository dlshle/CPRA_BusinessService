package org.wisc.business.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.model.BusinessModel.*;
import org.wisc.business.model.PVModels.*;
import org.wisc.business.model.UserModel.User;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TermServiceTest {
    @Autowired
    TermService termService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    ProfessorService professorService;

    @Test
    void findRawById() {
        final String validId = "5e8ffd935b6cdc554730cb4a";
        final String invalidId = "5e92a70d0c0435386683210";
        assertNotNull(termService.findRawById(validId));
        assertNull(termService.findRawById(invalidId));
        assertNull(termService.findRawById(null));
    }

    @Test
    void findById() {
        final String validId = "5e8ffd935b6cdc554730cb4a";
        final String invalidId = "5e92a70d0c0435386683210";
        assertNotNull(termService.findById(validId));
        assertNull(termService.findById(invalidId));
        assertNull(termService.findById(null));
    }

    @Test
    void findAllByName() {
        final String validQueryName = "CS";
        final String emptyQueryName = "DAMN";
        final List<TermPV> validQueryResult =
                termService.findAllByName(validQueryName);
        assertNotNull(validQueryResult);
        assertFalse(validQueryResult.isEmpty());
        assertTrue(termService.findAllByName(emptyQueryName).isEmpty());
        assertTrue(termService.findAllByName(null).isEmpty());
    }

    @Test
    void findBySeason() {
        Season fallSeason = Season.FALL;
        final List<TermPV> notEmpty = termService.findBySeason(fallSeason);
        assertNotNull(notEmpty.isEmpty());
        assertFalse(notEmpty.isEmpty());
        assertTrue(termService.findBySeason(null).isEmpty());
    }

    @Test
    void findByYear() {
        int validYear = 2020;
        int invalidYear = 1900;
        final List<TermPV> notEmpty = termService.findByYear(validYear);
        assertNotNull(notEmpty);
        assertFalse(notEmpty.isEmpty());
        final List<TermPV> emptyList = termService.findByYear(invalidYear);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void findByAverageRating() {
        Double validRating = 4.5;
        Double inValidRating = -1.0;
        final List<TermPV> notEmpty =
                termService.findByAverageRating(validRating);
        final List<TermPV> empty =
                termService.findByAverageRating(inValidRating);
        assertNotNull(notEmpty);
        assertFalse(notEmpty.isEmpty());
        assertTrue(empty.isEmpty());
        assertTrue(termService.findByAverageRating(null).isEmpty());
    }

    @Test
    void findByCourseId() {
        final String validCourseId = "5e7f12b4b704f53c6aa922dd";
        final String invalidCourseId = "5e7f12b4b704f53c6aa9123d";
        final List<TermPV> notEmpty = termService.findByCourseId(validCourseId);
        final List<TermPV> empty = termService.findByCourseId(invalidCourseId);

        assertNotNull(notEmpty);
        assertNotNull(empty);
        assertFalse(notEmpty.isEmpty());
        assertTrue(empty.isEmpty());
        assertTrue(termService.findByCourseId(null).isEmpty());
    }

    @Test
    void all() {
        final List<TermPV> all = termService.all();
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    void add() {
        final Term valid = Term.builder().name("Testerm").build();
        final Term invalid = new Term();
        final Term newTerm = termService.add(valid).toRawType();
        assertNotNull(newTerm);
        assertNull(termService.add(invalid));
        assertTrue(termService.delete(newTerm));
        assertNull(termService.add(null));
    }

    @Test
    void updateRaw() {
        final String validId = "5e8ffd935b6cdc554730cb4a";
        final Term oldTerm = termService.findRawById(validId);
        assertNotNull(oldTerm);
        final String oldName = oldTerm.getName();
        final String newName = "New Name";
        oldTerm.setName(newName);
        Term updatedTerm = termService.updateRaw(oldTerm);
        assertEquals(updatedTerm.getName(), newName);
        oldTerm.setName(oldName);
        updatedTerm = termService.updateRaw(oldTerm);
        assertNotNull(updatedTerm);
        assertEquals(updatedTerm.getName(), oldName);
        assertNull(termService.updateRaw(null));
    }

    @Test
    void addRawComment() {
        // c
        UserPV user = TestUtils.addNewTestUser(userService);
        TermPV term = TestUtils.addNewTestTerm(termService);
        CommentPV valid = TestUtils.addNewTestComment(commentService,
                term.toRawType(), user.toRawType());

        assertFalse(termService.addRawComment(null));
        // tid
        Comment c = new Comment();
        assertFalse(termService.addRawComment(c));
        // t(invalid tid)
        c.setTermId("asdjfjoiwfjeoiwfj");
        assertFalse(termService.addRawComment(c));
        //success
        assertTrue(termService.addRawComment(valid.toRawType()));
        // cleanup
        TestUtils.deleteAndTestUser(userService, user.toRawType(), true);
        TestUtils.deleteAndTestTerm(termService, term.toRawType(), true);
        assertFalse(commentService.deleteRaw(valid.toRawType()));
    }

    @Test
    void update() {
        final String validId = "5e8ffd935b6cdc554730cb4a";
        final TermPV oldTerm = termService.findById(validId);
        assertNotNull(oldTerm);
        final String oldName = oldTerm.getName();
        final String newName = "New Name";
        oldTerm.setName(newName);
        TermPV updatedTerm = termService.update(oldTerm.toRawType());
        assertEquals(updatedTerm.getName(), newName);
        oldTerm.setName(oldName);
        updatedTerm = termService.update(oldTerm.toRawType());
        assertNotNull(updatedTerm);
        assertEquals(updatedTerm.getName(), oldName);
        assertNull(termService.updateRaw(null));
    }

    @Test
    void delete() {
        Term newTerm = Term.builder().name("Testerm").build();
        TermPV newTermPV = termService.add(newTerm);
        assertNotNull(newTermPV);
        assertTrue(termService.delete(newTermPV.toRawType()));
        assertFalse(termService.delete(newTermPV.toRawType()));
        assertFalse(termService.delete(null));

        // course professor commentid
        Term t = TestUtils.addNewTestTerm(termService).toRawType();
        Course c = TestUtils.addNewTestCourse(courseService).toRawType();
        Professor p = TestUtils.addNewTestProfessor(professorService).toRawType();
        User u = TestUtils.addNewTestUser(userService).toRawType();
        Comment cm = TestUtils.addNewTestComment(commentService, t, u).toRawType();
        LinkedList<String> professors = new LinkedList<>();
        LinkedList<String> comments = new LinkedList<>();
        professors.add(p.getId());
        comments.add(c.getId());
        t.setCourseId(c.getId());
        t.setProfessorIds(professors);
        t.setCommentIds(comments);
        termService.updateRaw(t);
        LinkedList<String> termIds = new LinkedList<>();
        termIds.add(t.getId());
        c.setTermsIds(termIds);
        courseService.updateRaw(c);
        p.setTermIds(termIds);
        professorService.updateRaw(p);

        // test add(for branch cov.)
        Term x = termService.add(t).toRawType();
        assertTrue(termService.delete(x));

        assertFalse(termService.delete(t));
        // cleanup
        assertTrue(professorService.delete(p));
        assertTrue(courseService.delete(c));
        assertTrue(userService.delete(u));
        assertFalse(commentService.deleteRaw(cm));
    }
}