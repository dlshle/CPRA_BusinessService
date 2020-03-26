package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.service.CommentService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * CommentController
 * TODO: all POST, PUT, DELETE requests need user auth to proceed
 */
@Slf4j
@RestController
@RequestMapping("/v1/comments")
public class CommentController {
    @Resource
    CommentService commentService;

    // TODO user auth
    @PostMapping("")
    public @ResponseBody  AjaxResponse addComment(@RequestBody  Comment comment) {
        Comment savedComment = commentService.add(comment);
        return (AjaxResponse.success(savedComment));
    }

    @GetMapping("")
    public @ResponseBody AjaxResponse getAllComments() {
        List<Comment> allComments = commentService.all();
        return AjaxResponse.success(allComments);
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getComment(@PathVariable String id) {
        Comment comment = commentService.findById(id);
        System.out.println("GET /v1/comments/"+id.toString()+":"+comment);
        if (comment == null)
            return AjaxResponse.error(400, "Invalid comment id.");
        return AjaxResponse.success(comment);
    }

    // TODO user auth
    @PutMapping("")
    public @ResponseBody AjaxResponse updateComment(@RequestBody Comment comment) {
        // update the comment timestamp on server
        comment.setLastModifiedDate(new Date());
        Comment newComment = commentService.update(comment);
        if (newComment == null) {
            return AjaxResponse.error(400, "Comment(" + comment.getId() + ") " +
                    "is invalid.");
        }
        return AjaxResponse.success(newComment);
    }

    // TODO user auth
    @DeleteMapping("")
    public @ResponseBody AjaxResponse deleteComment(@RequestBody Comment comment) {
        if (commentService.delete(comment))
            return AjaxResponse.success();
        return AjaxResponse.error(400, "Invalid comment("+comment.getId()+")");
    }
}
