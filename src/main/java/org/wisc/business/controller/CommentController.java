package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.PVModels.CommentPV;
import org.wisc.business.model.UserModel.User;
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

    @PostMapping("")
    public @ResponseBody  AjaxResponse addComment(@RequestHeader(
            "token") String token, @RequestBody  Comment comment) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        comment.setAuthorId(currentUser.getId());
        CommentPV savedComment = commentService.add(comment);
        if (savedComment == null)
            return AjaxResponse.error(400, "Invalid term id");
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

    @PutMapping("")
    public @ResponseBody AjaxResponse updateComment(@RequestHeader(
            "token") String token, @RequestBody CommentPV commentPv) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        Comment comment = commentPv.toRawType();
        comment.setLastEditedBy(currentUser.getId());
        // update the comment timestamp on server
        comment.setLastModifiedDate(new Date());
        CommentPV newComment = commentService.update(comment);
        if (newComment == null) {
            return AjaxResponse.error(400, "Comment(" + comment.getId() + ") " +
                    "is invalid.");
        }
        return AjaxResponse.success(newComment);
    }

    @DeleteMapping("")
    public @ResponseBody AjaxResponse deleteComment(@RequestHeader(
            "token") String token, @RequestBody CommentPV commentPv) {
        User currentUser = authenticationService.getCurrentUser(token);
        if (currentUser == null)
            return AjaxResponse.notLoggedIn();
        Comment comment = commentPv.toRawType();
        if (!currentUser.getId().equals(comment.getAuthorId()))
            return AjaxResponse.error(400,
                    "Invalid credential. Only " + comment.getAuthorId() + " " +
                            "can remove this comment.");
        if (commentService.delete(comment))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid comment("+comment.getId()+")");
    }
}
