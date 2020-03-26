package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.wisc.business.model.BusinessModel.Course;

@Repository
public interface CourseDAO extends MongoRepository<Course, String> {
    Course findByName(String name);
}
