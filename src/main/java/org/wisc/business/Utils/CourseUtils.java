package org.wisc.business.Utils;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wisc.business.model.BusinessModel.Course;

import javax.annotation.Resource;

@Deprecated
public class CourseUtils {
    @Resource
    private static MongoRepository courseRepository;

    public static boolean isValidCourse(Course course, boolean newCourse) {
        if (course == null)
            return false;
        return true;
    }
}
