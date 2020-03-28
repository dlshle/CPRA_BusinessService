package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.PVModels.CommentPV;
import org.wisc.business.service.AuthenticationService;
import org.wisc.business.service.CommentService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * CommentController
 */
@Slf4j
@RestController
@RequestMapping("/v1/comments")
public class CommentController {
    @Resource
    CommentService commentService;
    @Resource
    AuthenticationService authenticationService;

    // TODO user auth
    @PostMapping("")
    public @ResponseBody  AjaxResponse addComment(@RequestHeader(
            "token") String token, @RequestBody  Comment comment) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        CommentPV savedComment = commentService.add(comment);
        return (AjaxResponse.success(savedComment));
    }

    @GetMapping("")
    public @ResponseBody AjaxResponse getAllComments() {
        List<CommentPV> allComments = commentService.all();
        return AjaxResponse.success(allComments);
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getComment(@PathVariable String id) {
        CommentPV comment = commentService.findById(id);
        System.out.println("GET /v1/comments/"+id.toString()+":"+comment);
        if (comment == null)
            return AjaxResponse.error(400, "Invalid comment id.");
        return AjaxResponse.success(comment);
    }

    // TODO user auth
    @PutMapping("")
    public @ResponseBody AjaxResponse updateComment(@RequestHeader(
            "token") String token, @RequestBody Comment comment) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        // update the comment timestamp on server
        comment.setLastModifiedDate(new Date());
        CommentPV newComment = commentService.update(comment);
        if (newComment == null) {
            return AjaxResponse.error(400, "Comment(" + comment.getId() + ") " +
                    "is invalid.");
        }
        return AjaxResponse.success(newComment);
    }

    // TODO user auth
    @DeleteMapping("")
    public @ResponseBody AjaxResponse deleteComment(@RequestHeader(
            "token") String token, @RequestBody Comment comment) {
        if (!authenticationService.isValidToken(token))
            return AjaxResponse.notLoggedIn();
        if (commentService.delete(comment))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid comment("+comment.getId()+")");
    }
}
