package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wisc.business.model.BusinessModel.Course;

public interface CourseDAO extends MongoRepository<Course, String> {
}
