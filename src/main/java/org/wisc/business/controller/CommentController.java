package org.wisc.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;
import org.wisc.business.Utils.CommentUtils;
import org.wisc.business.model.AjaxResponse;
import org.wisc.business.model.BusinessModel.Comment;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * CommentController
 * TODO: all POST, PUT, DELETE requests need user auth to proceed
 */
@Slf4j
@RestController
@RequestMapping("v1/comments")
public class CommentController {
    @Resource
    MongoRepository commentRepository;

    // TODO user auth
    @PostMapping("/")
    public @ResponseBody  AjaxResponse saveComment(@RequestBody  Comment comment) {
        if (!CommentUtils.isValidComment(comment, true))
            return AjaxResponse.error(400, "Invalid comment body.");
        commentRepository.save(comment);
        return AjaxResponse.success(comment);
    }

    @GetMapping("/")
    public @ResponseBody AjaxResponse getAllComments() {
        List<Comment> allComments = commentRepository.findAll();
        return AjaxResponse.success(allComments);
    }

    @GetMapping("/{id}")
    public @ResponseBody AjaxResponse getComment(@PathVariable String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment == null)
            return AjaxResponse.error(400, "Invalid comment id.");
        return AjaxResponse.success(comment);
    }

    // TODO user auth
    @PutMapping("/")
    public @ResponseBody AjaxResponse updateComment(@RequestBody Comment comment) {
        if (!CommentUtils.isValidComment(comment, false))
            return AjaxResponse.error(400, "Invalid comment body.");
        // update the comment timestamp on server
        comment.setLastModifiedDate(new Date());
        commentRepository.save(comment);
        return AjaxResponse.success(comment);
    }

    // TODO user auth
    @DeleteMapping("/")
    public @ResponseBody AjaxResponse deleteComment(@RequestBody Comment comment) {
        return AjaxResponse.error(400, "Unknown command.");
    }
}
