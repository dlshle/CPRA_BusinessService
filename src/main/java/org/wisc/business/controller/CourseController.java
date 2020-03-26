package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.dao.CourseDAO;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Course;
import org.wisc.business.service.CourseService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/v1/courses")
public class CourseController {
    @Resource
    CourseService courseService;

    // TODO user auth
    @PostMapping("")
    public @ResponseBody AjaxResponse addCourse(@RequestBody Course course) {
        Course newCourse = courseService.add(course);
        return (newCourse == null?AjaxResponse.error(400,
                "Course("+course.getId()+") already exists."):
                AjaxResponse.success(newCourse));
    }

    // TODO user auth
    @PutMapping("")
    public @ResponseBody AjaxResponse updateCourse(@RequestBody Course course) {
        Course newCourse = courseService.update(course);
        if (newCourse == null) {
            return AjaxResponse.error(400, "Course("+course.getId()+") is " +
                    "invalid.");
        }
        return AjaxResponse.success(newCourse);
    }

    @GetMapping("")
    public @ResponseBody AjaxResponse getAllCourses() {
        return AjaxResponse.success(courseService.all());
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getCourse(@PathVariable String id) {
        Course course = courseService.findById(id);
        if (course == null) {
            return AjaxResponse.error(400, "Invalid course id(" + id + ")");
        }
        return AjaxResponse.success(course);
    }

    @GetMapping("/name/{name}")
    public @ResponseBody AjaxResponse getCourseByName(@PathVariable String name) {
        return AjaxResponse.success(courseService.findByName(name));
    }

    // TODO user auth
    @DeleteMapping("")
    public @ResponseBody AjaxResponse deleteCourse(@RequestBody  Course course) {
        if (courseService.delete(course))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid course("+course.getId()+")");
    }
}
