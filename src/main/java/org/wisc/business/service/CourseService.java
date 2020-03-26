package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.CourseDAO;
import org.wisc.business.model.BusinessModel.Course;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    CourseDAO courseDAO;

    public Course findById(String id) {
        if (id == null)
            return null;
        Optional<Course> result =  courseDAO.findById(id);
        if (!result.isPresent())
            return null;
        return result.get();
    }

    public Course findByName(String name) {
        return courseDAO.findByName(name);
    }

    public List<Course> all() {
        return courseDAO.findAll();
    }

    public Course add(Course course) {
        return courseDAO.save(course);
    }

    public Course update(Course course) {
        Course oldCourse = findById(course.getId());
        if (oldCourse == null)
            return null;
        if (course.getName() != null && !course.getName().equals(oldCourse.getName()))
            oldCourse.setName(course.getName());
        if (course.getDescription() != null && !course.getDescription().equals(oldCourse.getDescription()))
            oldCourse.setDescription(course.getDescription());
        if (course.getTermsIds() != null)
            oldCourse.setTermsIds(course.getTermsIds());
        return courseDAO.save(oldCourse);
    }

    public boolean delete(Course course) {
        if (findById(course.getId()) == null)
            return false;
        courseDAO.delete(course);
        return true;
    }
}
