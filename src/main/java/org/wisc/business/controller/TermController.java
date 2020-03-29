package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Season;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.*;
import org.wisc.business.model.UserModel.User;
import org.wisc.business.service.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.LinkedList;

@Slf4j
@RestController
@RequestMapping("/v1/terms")
public class TermController {
    @Resource
    TermService termService;

    @Resource
    CourseService courseService;

    @Resource
    ProfessorService professorService;

    @Resource
    AuthenticationService authenticationService;

    @Resource
    UserService userService;

    @Autowired
    MongoTemplate mongoTemplate;

    // TODO user auth
    @PostMapping("")
    public @ResponseBody
    AjaxResponse addTerm(@RequestHeader("token") String token,
                         @RequestBody Term term) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        TermPV newTerm = termService.add(term);
        return (newTerm == null?AjaxResponse.error(400,
                "Term("+term.getId()+") already exists."):
                AjaxResponse.success(newTerm));
    }

    @PutMapping("")
    public @ResponseBody AjaxResponse updateTerm(@RequestHeader("token") String token
            ,@RequestBody TermPV term) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        TermPV newTerm = termService.update(term.toRawType());
        if (newTerm == null) {
            return AjaxResponse.error(400, "Term("+term.getId()+") is " +
                    "invalid.");
        }
        return AjaxResponse.success(newTerm);
    }

    @GetMapping("")
    public @ResponseBody AjaxResponse getAllTerms() {
        return AjaxResponse.success(termService.all());
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getTerm(@PathVariable String id) {
        TermPV term = termService.findById(id);
        if (term == null) {
            return AjaxResponse.error(400, "Invalid term id(" + id + ")");
        }
        return AjaxResponse.success(term);
    }

    @GetMapping("/courseId/{courseId}")
    public @ResponseBody AjaxResponse getTermsByCourseId(@PathVariable String courseId) {
        if (courseId == null || courseId.isEmpty())
            return AjaxResponse.error(400, "Invalid course Id");
         return AjaxResponse.success(termService.findByCourseId(courseId));
    }

    @GetMapping("/season/{season}")
    public @ResponseBody AjaxResponse findBySeason(@PathVariable String season) {
        Season s = Season.generateSeason(season);
        if (s == null)
            return AjaxResponse.error(400, "Invalid parameter:" + season);
        return AjaxResponse.success(termService.findBySeason(s));
    }

    @GetMapping("/year/{year}")
    public @ResponseBody AjaxResponse findByYear(@PathVariable Integer year) {
        return AjaxResponse.success(termService.findByYear(year));
    }

    @GetMapping("/rating/{rating}")
    public @ResponseBody AjaxResponse findByRating(@PathVariable Double rating) {
        return AjaxResponse.success(termService.findByAverageRating(rating));
    }

    /**
     * query terms by params
     * @param year year taught
     * @param fromYear range from
     * @param toYear range to
     * @param seasonStr season string
     * @param courseName course name
     * @param averageRating average rating is
     * @param ratingFrom average rating from
     * @param ratingTo average rating to
     * @param taughtBy professor names separated by comma
     * @return
     */
    @GetMapping("/query")
    public @ResponseBody AjaxResponse queryTerm(@PathParam("year") Integer year,
                                                @PathParam("from") Integer fromYear,
                                                @PathParam("to") Integer toYear,
                                                @PathParam("season") String seasonStr,
                                                @PathParam("courseName") String courseName,
                                                @PathParam("averageRating") Double averageRating,
                                                @PathParam("ratingFrom") Double ratingFrom,
                                                @PathParam("ratingTo") Double ratingTo,
                                                @PathParam("taughtBy") String taughtBy) {
        CoursePV quriedCourse = null;
        if (courseName != null && !courseName.isEmpty()) {
            quriedCourse = courseService.findByName(courseName);
        }
        if (fromYear != null && toYear != null) {
            if (year != null)
                return AjaxResponse.error(400, "Invalid year attributes" +
                        "(Multiple definition of year params)");
            if (fromYear > toYear)
                return AjaxResponse.error(400, "From year is greater than to " +
                        "year");
        }
        if (ratingFrom != null && ratingTo != null) {
            if (averageRating != null)
                return AjaxResponse.error(400, "Invalid rating attributes" +
                        "(Multiple definition of rating params)");
            if (ratingFrom > ratingTo)
                return AjaxResponse.error(400, "From rating is greater than " +
                        "to rating");
        }
        Criteria headCriteria = new Criteria();
        if (quriedCourse != null) headCriteria.and("courseId").is(quriedCourse.getId());
        if (year != null && fromYear == null && toYear == null)
            headCriteria.and("year").is(year);
        if (year == null && fromYear != null && toYear == null)
            headCriteria.and("year").gte(fromYear);
        if (year == null && toYear != null && fromYear == null)
            headCriteria.and("year").lte(toYear);
        if (year == null && fromYear != null && toYear != null)
            headCriteria.and("year").gte(fromYear).lte(toYear);
        if (seasonStr != null && Season.generateSeason(seasonStr) != null)
            headCriteria.and("season").is(Season.generateSeason(seasonStr));
        if (averageRating != null && ratingFrom == null && ratingTo == null)
            headCriteria.and("averageRating").is(averageRating);
        if (averageRating == null && ratingFrom != null && ratingTo == null)
            headCriteria.and("averageRating").gte(ratingFrom);
        if (averageRating == null && ratingFrom == null && ratingTo != null)
            headCriteria.and("averageRating").lte(ratingTo);
        if (averageRating == null && ratingFrom != null && ratingTo != null)
            headCriteria.and("averageRating").gte(ratingFrom).lte(ratingTo);
        if (taughtBy != null) {
            // TODO use professor ids to query
            String[] professorNames = taughtBy.split(",");
            for (String pName:professorNames) {
                ProfessorPV p =
                        professorService.findFirstOccuranceByName(pName);
            }
        }
        Query query = new Query(headCriteria);
        return AjaxResponse.success(mongoTemplate.find(query, Term.class));
    }

    public AjaxResponse doFavoriteOrUnFavorite(String token, TermPV term,
                                         boolean isFavorite) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        if (term == null || term.getId() == null)
            return AjaxResponse.error(400, "Invalid term or term id");
        UserPV responseData = (isFavorite?userService.favorite(currentUser,
                term.getId()):userService.unfavorite(currentUser,
                term.getId()));
        if (responseData == null)
            return AjaxResponse.error(400, "failed to update user favorite " +
                    "list.");
        return AjaxResponse.success(responseData);
    }

    @PutMapping("/favorite")
    public @ResponseBody AjaxResponse favoriteTerm(@RequestHeader(
            "token") String token, @RequestBody TermPV term) {
       return doFavoriteOrUnFavorite(token, term, true);
    }

    @PutMapping("/unfavorite")
    public @ResponseBody AjaxResponse unfavoriteTerm(@RequestHeader(
            "token") String token, @RequestBody TermPV term) {
        return doFavoriteOrUnFavorite(token, term, false);
    }

    @DeleteMapping("")
    public @ResponseBody
    AjaxResponse deleteTerm(@RequestHeader("token") String token,
                            @RequestBody TermPV term) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        if (!currentUser.isAdmin())
            return AjaxResponse.error(400, "Only admin can remove this term" +
                    " from database");
        if (termService.delete(term.toRawType()))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid term("+term.getId()+")");
    }
}
