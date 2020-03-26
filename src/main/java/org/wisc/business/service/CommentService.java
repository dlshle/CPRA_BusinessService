package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.CommentDAO;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.UserModel.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public Comment findById(String id) {
        if (id == null)
            return null;
        Optional<Comment> result =  commentDAO.findById(id);
        if (!result.isPresent())
            return null;
        return result.get();
    }

    public List<Comment> findByAuthor(User author) {
        return commentDAO.findAllByAuthorId(author.getId());
    }

    public List<Comment> all() {
        return commentDAO.findAll();
    }

    public Comment add(Comment comment) {
        comment.setLastModifiedDate(new Date());
        comment.setLastEditedBy(comment.getAuthorId());
        return commentDAO.save(comment);
    }

    public Comment update(Comment comment) {
        Comment oldComment = findById(comment.getId());
        if (oldComment == null)
            return null;
        if (comment.getRating() != null && comment.getRating().equals(oldComment.getRating()))
            oldComment.setRating(comment.getRating());
        if (comment.getContent() != null && !comment.getContent().equals(oldComment.getContent()))
            oldComment.setContent(comment.getContent());
        if (comment.getLastEditedBy() != null && !comment.getLastEditedBy().equals(oldComment.getLastEditedBy()))
            oldComment.setLastEditedBy(comment.getLastEditedBy());
        oldComment.setLastModifiedDate(new Date());
        return commentDAO.save(oldComment);
    }

    public boolean delete(Comment comment) {
        if (findById(comment.getId()) == null)
            return false;
        commentDAO.delete(comment);
        return true;
    }
}
