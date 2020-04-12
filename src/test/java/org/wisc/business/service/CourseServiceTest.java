package org.wisc.business.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.model.BusinessModel.Course;
import org.wisc.business.model.PVModels.CoursePV;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CourseServiceTest {
    @Autowired
    CourseService courseService;

    @Test
    void findRawById() {
        final String validId = "5e7c4abe36019b0430f44417";
        final String invalidId = "5e7c4abe36019b0430f4321";
        assertNotNull(courseService.findRawById(validId));
        assertNull(courseService.findRawById(invalidId));
        assertNull(courseService.findRawById(null));
    }

    @Test
    void findById() {
        final String validId = "5e7c4abe36019b0430f44417";
        final String invalidId = "5e7c4abe36019b0430f4321";
        assertNotNull(courseService.findById(validId));
        assertNull(courseService.findById(invalidId));
        assertNull(courseService.findById(null));
    }

    @Test
    void findByName() {
        final String validName = "CS 506";
        final String invalidName = "CSS";
        assertNotNull(courseService.findByName(validName));
        assertNull(courseService.findByName(invalidName));
        assertNull(courseService.findByName(null));
    }

    @Test
    void findAllByName() {
        final String validQueryName = "CS";
        final String emptyQueryName = "B";
        final List<CoursePV> notEmptyList =
                courseService.findAllByName(validQueryName);
        assertNotNull(notEmptyList);
        assertFalse(notEmptyList.isEmpty());
        final List<CoursePV> emptyList =
                courseService.findAllByName(emptyQueryName);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
        assertNotNull(courseService.findAllByName(null));
    }

    @Test
    void all() {
        final List<CoursePV> allCourses = courseService.all();
        assertNotNull(allCourses);
        assertFalse(allCourses.isEmpty());
    }

    @Test
    void add() {
        final Course validCourse =
                Course.builder().name("CS TEST").description("For testing").build();
        final Course invalidCourse = new Course();
        final CoursePV newCoursePV = courseService.add(validCourse);
        assertNotNull(newCoursePV);
        // clean up after adding new course
        assertTrue(courseService.delete(validCourse));

        assertNull(courseService.add(invalidCourse));
        assertNull(courseService.add(null));
    }

    @Test
    void updateRaw() {
        final String validId = "5e7c4abe36019b0430f44417";
        final Course oldCourse = courseService.findRawById(validId);
        final String oldName = oldCourse.getName();
        final String newName = "SC 605";

        assertNotNull(oldCourse);
        oldCourse.setName(newName);
        Course updatedCourse = courseService.updateRaw(oldCourse);
        assertEquals(updatedCourse.getName(), newName);
        oldCourse.setName(oldName);
        updatedCourse = courseService.updateRaw(oldCourse);
        assertNotNull(updatedCourse);
        assertEquals(updatedCourse.getName(), oldName);

        assertNull(courseService.updateRaw(new Course()));
        assertNull(courseService.updateRaw(null));
    }

    @Test
    void update() {
        final String validId = "5e7c4abe36019b0430f44417";
        final Course oldCourse = courseService.findRawById(validId);
        final String oldName = oldCourse.getName();
        final String newName = "SC 605";

        assertNotNull(oldCourse);
        oldCourse.setName(newName);
        CoursePV updatedCoursePV = courseService.update(oldCourse);
        assertEquals(updatedCoursePV.getName(), newName);
        oldCourse.setName(oldName);
        updatedCoursePV = courseService.update(oldCourse);
        assertNotNull(updatedCoursePV);
        assertEquals(updatedCoursePV.getName(), oldName);

        assertNull(courseService.update(new Course()));
        assertNull(courseService.update(null));
    }

    @Test
    void delete() {
        final Course validCourse = Course.builder().name("Test Course").build();
        final CoursePV newCourse = courseService.add(validCourse);
        assertNotNull(newCourse);
        assertTrue(courseService.delete(newCourse.toRawType()));
        assertFalse(courseService.delete(new Course()));
        assertFalse(courseService.delete(null));
    }
}