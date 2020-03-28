package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.BusinessModel.Course;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.CoursePV;
import org.wisc.business.model.PVModels.TermPV;
import org.wisc.business.model.UserModel.User;
import org.wisc.business.service.AuthenticationService;
import org.wisc.business.service.CourseService;
import org.wisc.business.service.TermService;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/courses")
public class CourseController {
    @Resource
    CourseService courseService;
    @Resource
    AuthenticationService authenticationService;
    @Resource
    TermService termService;

    @PostMapping("")
    public @ResponseBody AjaxResponse addCoursePV(@RequestHeader("token") String token
            ,@RequestBody Course course) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        CoursePV newCoursePV = courseService.add(course);
        return (newCoursePV == null?AjaxResponse.error(400,
                "CoursePV("+course.getId()+") already exists."):
                AjaxResponse.success(newCoursePV));
    }

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

    @PutMapping("/{courseId}/addTerm")
    public @ResponseBody AjaxResponse addTerm(@RequestHeader("token") String token, @PathVariable String courseId, @RequestBody Term term) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        if (courseId == null || courseId.isEmpty())
            return AjaxResponse.error(400, "empty course id");
        Course course = courseService.findRawById(courseId);
        if (course == null)
            return AjaxResponse.error(400, "Invalid course id("+courseId+")");
        if (term == null || term.getName() == null || term.getName().isEmpty() || term.getSeason() == null || term.getYear() == null)
            return AjaxResponse.error(400, "Invalid term");
        TermPV newTerm = termService.add(term);
        List<String> termIds = course.getTermsIds();
        termIds.add(newTerm.getId());
        course.setTermsIds(termIds);
        CoursePV updatedCourse = courseService.update(course);
        if (updatedCourse == null)
            AjaxResponse.error(400, "failed to update the course");
        return AjaxResponse.success(updatedCourse);
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

    @DeleteMapping("")
    public @ResponseBody AjaxResponse deleteCoursePV(@RequestHeader(
            "token") String token, @RequestBody  CoursePV course) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        if (!currentUser.isAdmin())
            return AjaxResponse.error(400, "Only admin can remove this course" +
                    " from database");
        if (courseService.delete(course.toRawType()))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid course("+course.getId()+")");
    }
}
