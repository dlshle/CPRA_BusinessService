package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Course;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/v1/courses")
public class CourseController {
    @Resource
    MongoRepository courseRepository;

    @PostMapping("/")
    public @ResponseBody AjaxResponse saveCourse(@RequestBody Course course) {
        // TODO continue finishing this class
        return AjaxResponse.error(200, "Unknown request.");
    }
}
