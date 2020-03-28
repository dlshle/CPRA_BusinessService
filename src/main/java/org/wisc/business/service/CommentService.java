package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.CommentDAO;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.PVModels.CommentPV;
import org.wisc.business.model.PVModels.UserPV;
import org.wisc.business.model.UserModel.User;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Resource
    UserService userService;

    public CommentPV convertCommentToCommentPV(Comment c) {
        if (c == null)
            return null;
        UserPV author = userService.findById(c.getAuthorId());
        UserPV lastEditor = null;
        if (c.getLastEditedBy() != null)
            lastEditor = userService.findById(c.getLastEditedBy());
        return new CommentPV(c, author, lastEditor);
    }

    public List<CommentPV> convertCommentsToCommentPVs(List<Comment> comments) {
        List<CommentPV> results = new LinkedList<>();
        comments.forEach((c)->results.add(convertCommentToCommentPV(c)));
        return results;
    }

    public Comment findRawById(String id) {
        if (id == null)
            return null;
        Optional<Comment> result =  commentDAO.findById(id);
        if (!result.isPresent())
            return null;
        return result.get();
    }

    public CommentPV findById(String id) {
        return convertCommentToCommentPV(findRawById(id));
    }

    public List<CommentPV> findByAuthor(User author) {
        return convertCommentsToCommentPVs(commentDAO.findAllByAuthorId(author.getId()));
    }

    public List<CommentPV> all() {
        return convertCommentsToCommentPVs(commentDAO.findAll());
    }

    public CommentPV add(Comment comment) {
        comment.setLastModifiedDate(new Date());
        comment.setLastEditedBy(comment.getAuthorId());
        return convertCommentToCommentPV(commentDAO.save(comment));
    }

    public CommentPV update(Comment comment) {
        Comment oldComment = findRawById(comment.getId());
        if (oldComment == null)
            return null;
        if (comment.getRating() != null && comment.getRating().equals(oldComment.getRating()))
            oldComment.setRating(comment.getRating());
        if (comment.getContent() != null && !comment.getContent().equals(oldComment.getContent()))
            oldComment.setContent(comment.getContent());
        if (comment.getLastEditedBy() != null && !comment.getLastEditedBy().equals(oldComment.getLastEditedBy()))
            oldComment.setLastEditedBy(comment.getLastEditedBy());
        oldComment.setLastModifiedDate(new Date());
        return convertCommentToCommentPV(commentDAO.save(oldComment));
    }

    public boolean delete(Comment comment) {
        if (findById(comment.getId()) == null)
            return false;
        commentDAO.delete(comment);
        return true;
    }
}
