package org.wisc.business.model;

import org.junit.jupiter.api.Test;
import org.wisc.business.model.BusinessModel.*;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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
        assertNull(c.getRating());

        c.setLastEditedBy("shit");
        assertEquals("shit", c.getLastEditedBy());
        c.setLastModifiedDate(new Date());
        assertEquals(new Date(), c.getLastModifiedDate());
        c.setAuthorId("111");
        assertEquals("111", c.getAuthorId());
        c.setContent("content");
        assertEquals("content", c.getContent());
        c.setRating((double)2);
        assertEquals(2.0, c.getRating());
        c.setId("111");
        assertEquals("111", c.getId());
        c.setTermId("222");
        assertEquals("222", c.getTermId());
//        assertNull(c.setRating((double)4)); //?

        // all args constructor
        Comment d = new Comment("123","456","content","789","shu",new Date(), 1.0);
        assertNotNull(d);
        assertNotNull(d.equals(c)); //?
        assertNotNull(d.getAuthorId());
        assertNotNull(d.getTermId());
        assertNotNull(d.getContent());
        assertNotNull(d.getId());
        assertNotNull(d.getLastEditedBy());
        assertNotNull(d.getLastModifiedDate());
        assertNotNull(d.getRating());
        assertNotNull(d.getClass());
//        assertNull(d.setRating((double)4));

        d.setLastEditedBy("shit");
        assertEquals("shit", d.getLastEditedBy());
        d.setLastModifiedDate(new Date());
        assertEquals(new Date(), d.getLastModifiedDate());
        d.setAuthorId("111");
        assertEquals("111", d.getAuthorId());
        d.setContent("content");
        assertEquals("content", d.getContent());
        d.setRating((double)2);
        assertEquals(2.0, d.getRating());
        d.setId("111");
        assertEquals("111", d.getId());
        d.setTermId("222");
        assertEquals("222", d.getTermId());
    }

    @Test
    public void testCourse() {
        ArrayList<String> termIds = new ArrayList<String>();
        termIds.add("343");

        Course c = new Course();
        assertNull(c.getDescription());
        assertNull(c.getId());
        assertNull(c.getName());
        assertNull(c.getTermsIds());
        c.setName("name");
        c.setTermsIds(termIds);
        c.setDescription("description");
        c.setId("111");
        assertEquals("name", c.getName());
        assertEquals("description", c.getDescription());
        assertEquals(termIds, c.getTermsIds());
        assertEquals("111", c.getId());

        Course d = new Course("123","CS506","description", termIds);
        assertNotNull(d.getId());
        assertNotNull(d.getName());
        assertNotNull(d.getDescription());
        assertNotNull(d.getTermsIds());
    }

    @Test
    public void testProfessor() {
        ArrayList<String> termIds = new ArrayList<String>();
        termIds.add("343");

        Professor p = new Professor();
        assertNull(p.getName());
        assertNull(p.getTermIds());
        assertNull(p.getId());
        assertNull(p.getDescription());

        p.setName("shit");
        p.setTermIds(termIds);
        p.setDescription("description");
        p.setId("111");
        assertEquals("shit", p.getName());
        assertEquals(termIds, p.getTermIds());
        assertEquals("description", p.getDescription());
        assertEquals("111", p.getId());

        Professor q = new Professor("123", "tracy", "good", termIds);
        assertNotNull(q.getId());
        assertNotNull(q.getName());
        assertNotNull(q.getDescription());
        assertNotNull(q.getTermIds());
    }

    @Test
    public void testSeason() {
        assertNotNull(Season.generateSeason("fall"));
        assertNotNull(Season.SPRING);
//        assertNotNull(Season.valueOf("fall"));
//        assertNotNull(Season.values());

    }

    @Test
    public void testTerm() {
        ArrayList<String> profIds = new ArrayList<String>();
        ArrayList<String> cmtIds = new ArrayList<String>();
        profIds.add("123");
        cmtIds.add("345");

        Term t = new Term();
        assertNull(t.getId());
        assertNull(t.getName());
        assertNull(t.getProfessorIds());
        assertNull(t.getCommentIds());
        assertNull(t.getCourseId());
        assertNull(t.getSeason());
        assertNull(t.getYear());
        assertNull(t.getDescription());
        assertNull(t.getAverageRating());
        t.setId("222");
        t.setName("PSYCH");
        t.setProfessorIds(profIds);
        t.setCourseId("123");
        t.setAverageRating((Double)3.5);
        t.setSeason(Season.SPRING);
        t.setCommentIds(cmtIds);
        t.setDescription("wow");
        t.setYear((Integer)2020);
        assertEquals("222", t.getId());
        assertEquals("PSYCH", t.getName());
        assertEquals(profIds, t.getProfessorIds());
        assertEquals("123", t.getCourseId());
        assertEquals((Double)3.5, t.getAverageRating());
        assertEquals(Season.SPRING, t.getSeason());
        assertEquals(cmtIds, t.getCommentIds());
        assertEquals("wow", t.getDescription());

        Term m = new Term("111", "333", "CS506", "not bad",
                (Double)3.0, (Integer)2011, Season.FALL, profIds, cmtIds);
        assertNotNull(m.getDescription());
        assertNotNull(m.getCommentIds());
        assertNotNull(m.getSeason());
        assertNotNull(m.getAverageRating());
        assertNotNull(m.getCourseId());
        assertNotNull(m.getName());
        assertNotNull(m.getYear());
        assertNotNull(m.getId());
        assertNotNull(m.getProfessorIds());
    }

}
