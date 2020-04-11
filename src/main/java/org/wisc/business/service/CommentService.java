package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.CommentDAO;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.CommentPV;
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

    @Resource
    TermService termService;

    public CommentPV convertCommentToCommentPV(Comment c) {
        if (c == null)
            return null;
        User author = userService.findRawById(c.getAuthorId());
        User lastEditor = null;
        if (c.getLastEditedBy() != null)
            lastEditor = userService.findRawById(c.getLastEditedBy());
        Term term = null;
        if (c.getTermId() == null)
            return null;
        term = termService.findRawById(c.getTermId());
        if (term == null)
            return null;
        return new CommentPV(c, term, author, lastEditor);
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

    public List<CommentPV> all() {
        return convertCommentsToCommentPVs(commentDAO.findAll());
    }

    public CommentPV add(Comment comment) {
        if (comment.getTermId() == null || comment.getAuthorId() == null)
            return null;
        comment.setLastModifiedDate(new Date());
        comment.setLastEditedBy(comment.getAuthorId());
        CommentPV saved = convertCommentToCommentPV(commentDAO.save(comment));
        if (saved == null)
            return null;
        if (!termService.addRawComment(comment)) {
            return null;
        }
        return saved;
    }

    public CommentPV update(Comment comment) {
        Comment oldComment = findRawById(comment.getId());
        if (oldComment == null)
            return null;
        if (comment.getRating() != null && !comment.getRating().equals(oldComment.getRating())) {
            oldComment.setRating(comment.getRating());
        }
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
        if (comment.getTermId() != null) {
            Term term = termService.findRawById(comment.getTermId());
            if (term != null) {
                if (term.getCommentIds().remove(comment.getId()) && comment.getRating() != null) {
                    term.setAverageRating((term.getAverageRating() * term.getCommentIds().size() - comment.getRating()) / (term.getCommentIds().size() - 1));
                    termService.updateRaw(term);
                }
            }
        }
        commentDAO.delete(comment);
        return true;
    }

    public boolean deleteRaw(Comment comment) {
         if (findById(comment.getId()) == null)
            return false;
        commentDAO.delete(comment);
        return true;
    }
}
