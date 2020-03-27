package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Professor;
import org.wisc.business.service.AuthenticationService;
import org.wisc.business.service.ProfessorService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/v1/professors")
public class ProfessorController {
    @Resource
    ProfessorService professorService;
    @Resource
    AuthenticationService authenticationService;

    // TODO user auth
    @PostMapping("")
    public @ResponseBody
    AjaxResponse addProfessor(@RequestHeader("token") String token,
                              @RequestBody Professor professor) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        Professor newProfessor = professorService.add(professor);
        return (newProfessor == null?AjaxResponse.error(400,
                "Professor("+professor.getId()+") already exists."):
                AjaxResponse.success(newProfessor));
    }

    // TODO user auth
    @PutMapping("")
    public @ResponseBody AjaxResponse updateProfessor(@RequestHeader(
            "token") String token, @RequestBody Professor professor) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        Professor newProfessor = professorService.update(professor);
        if (newProfessor == null) {
            return AjaxResponse.error(400, "Professor("+professor.getId()+") is " +
                    "invalid.");
        }
        return AjaxResponse.success(newProfessor);
    }

    @GetMapping("")
    public @ResponseBody AjaxResponse getAllProfessors() {
        return AjaxResponse.success(professorService.all());
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getProfessor(@PathVariable String id) {
        Professor professor = professorService.findById(id);
        if (professor == null) {
            return AjaxResponse.error(400, "Invalid professor id(" + id + ")");
        }
        return AjaxResponse.success(professor);
    }

    @GetMapping("/name/{name}")
    public @ResponseBody AjaxResponse getCourseByName(@PathVariable String name) {
        return AjaxResponse.success(professorService.findByName(name));
    }

    // TODO user auth
    @DeleteMapping("")
    public @ResponseBody AjaxResponse deleteProfessor(@RequestHeader(
            "token") String token, @RequestBody  Professor professor) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        if (professorService.delete(professor))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid professor("+professor.getId()+")");
    }
}
