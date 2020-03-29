package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.wisc.business.model.BusinessModel.Course;

import java.util.List;

@Repository
public interface CourseDAO extends MongoRepository<Course, String> {
    List<Course> findAllByNameLike(String name);
    Course findByName(String name);
}
