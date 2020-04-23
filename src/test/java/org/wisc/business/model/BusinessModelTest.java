package org.wisc.business.model;

import org.junit.jupiter.api.Test;
import org.wisc.business.model.BusinessModel.Comment;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BusinessModelTest {

    @Test
    public void testComment() {
        // creation w/ default constructor
        Comment c = new Comment();
        assertNotNull(c);
        assertNull(c.getTermId());
        assertNull(c.getAuthorId());
        assertNull(c.getContent());
        assertNull(c.getId());
        assertNull(c.getLastEditedBy());
        assertNull(c.getLastModifiedDate());

        // all args constructor
    }

    @Test
    public void testCourse() {

    }

    @Test
    public void testProfessor() {

    }

    @Test
    public void testSeason() {

    }

    @Test
    public void testTerm() {

    }

}
