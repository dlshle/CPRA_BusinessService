package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Professor;
import org.wisc.business.model.PVModels.ProfessorPV;
import org.wisc.business.model.UserModel.User;
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

    @PostMapping("")
    public @ResponseBody
    AjaxResponse addProfessorPV(@RequestHeader("token") String token,
                              @RequestBody Professor professor) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        ProfessorPV newProfessorPV = professorService.add(professor);
        return (newProfessorPV == null?AjaxResponse.error(400,
                "ProfessorPV("+professor.getId()+") already exists."):
                AjaxResponse.success(newProfessorPV));
    }

    @PutMapping("")
    public @ResponseBody AjaxResponse updateProfessorPV(@RequestHeader(
            "token") String token, @RequestBody ProfessorPV professor) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        ProfessorPV newProfessorPV = professorService.update(professor.toRawType());
        if (newProfessorPV == null) {
            return AjaxResponse.error(400, "ProfessorPV("+professor.getId()+") is " +
                    "invalid.");
        }
        return AjaxResponse.success(newProfessorPV);
    }

    @GetMapping("")
    public @ResponseBody AjaxResponse getAllProfessorPVs() {
        return AjaxResponse.success(professorService.all());
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getProfessorPV(@PathVariable String id) {
        ProfessorPV professor = professorService.findById(id);
        if (professor == null) {
            return AjaxResponse.error(400, "Invalid professor id(" + id + ")");
        }
        return AjaxResponse.success(professor);
    }

    @GetMapping("/name/{name}")
    public @ResponseBody AjaxResponse getCourseByName(@PathVariable String name) {
        return AjaxResponse.success(professorService.findByName(name));
    }

    @DeleteMapping("")
    public @ResponseBody AjaxResponse deleteProfessorPV(@RequestHeader(
            "token") String token, @RequestBody  ProfessorPV professorPv) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        Professor professor = professorPv.toRawType();
        if (!currentUser.isAdmin())
            return AjaxResponse.error(400, "Only admin can remove this " +
                    "professor from database.");
        if (professorService.delete(professor))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid professor("+professor.getId()+")");
    }
}
