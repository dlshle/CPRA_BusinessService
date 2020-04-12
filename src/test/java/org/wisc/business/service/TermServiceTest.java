package org.wisc.business.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.BusinessModel.Season;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.TermPV;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TermServiceTest {
    @Autowired
    TermService termService;
    @Autowired
    CommentService commentService;

    @Test
    void findRawById() {
        final String validId = "5e92a70d0c0435386682b20f";
        final String invalidId = "5e92a70d0c0435386683210";
        assertNotNull(termService.findRawById(validId));
        assertNull(termService.findRawById(invalidId));
        assertNull(termService.findRawById(null));
    }

    @Test
    void findById() {
        final String validId = "5e92a70d0c0435386682b20f";
        final String invalidId = "5e92a70d0c0435386683210";
        assertNotNull(termService.findById(validId));
        assertNull(termService.findById(invalidId));
        assertNull(termService.findById(null));
    }

    @Test
    void findAllByName() {
        final String validQueryName = "CS";
        final String emptyQueryName = "SC";
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
        final String validId = "5e92a70d0c0435386682b20f";
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
    void update() {
        final String validId = "5e92a70d0c0435386682b20f";
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
    }
}