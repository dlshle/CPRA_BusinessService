package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.PVModels.CoursePV;
import org.wisc.business.service.AuthenticationService;
import org.wisc.business.service.CourseService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/v1/courses")
public class CourseController {
    @Resource
    CourseService courseService;
    @Resource
    AuthenticationService authenticationService;

    // TODO user auth
    @PostMapping("")
    public @ResponseBody AjaxResponse addCoursePV(@RequestHeader("token") String token
            ,@RequestBody CoursePV course) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        CoursePV newCoursePV = courseService.add(course.toRawType());
        return (newCoursePV == null?AjaxResponse.error(400,
                "CoursePV("+course.getId()+") already exists."):
                AjaxResponse.success(newCoursePV));
    }

    // TODO user auth
    @PutMapping("")
    public @ResponseBody AjaxResponse updateCoursePV(@RequestHeader(
            "token") String token, @RequestBody CoursePV course) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        CoursePV newCoursePV = courseService.update(course.toRawType());
        if (newCoursePV == null) {
            return AjaxResponse.error(400, "CoursePV("+course.getId()+") is " +
                    "invalid.");
        }
        return AjaxResponse.success(newCoursePV);
    }

    @GetMapping("")
    public @ResponseBody AjaxResponse getAllCoursePVs() {
        return AjaxResponse.success(courseService.all());
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getCoursePV(@PathVariable String id) {
        CoursePV course = courseService.findById(id);
        if (course == null) {
            return AjaxResponse.error(400, "Invalid course id(" + id + ")");
        }
        return AjaxResponse.success(course);
    }

    @GetMapping("/name/{name}")
    public @ResponseBody AjaxResponse getCoursePVByName(@PathVariable String name) {
        return AjaxResponse.success(courseService.findByName(name));
    }

    // TODO user auth
    @DeleteMapping("")
    public @ResponseBody AjaxResponse deleteCoursePV(@RequestHeader(
            "token") String token, @RequestBody  CoursePV course) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        if (courseService.delete(course.toRawType()))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid course("+course.getId()+")");
    }
}
