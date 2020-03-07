package org.wisc.business.Utils;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wisc.business.model.BusinessModel.Comment;

import javax.annotation.Resource;
import java.util.Date;

public class CommentUtils {
    @Resource
    private static MongoRepository commentRepository;

    public static boolean isValidComment(Comment comment, boolean isNew) {
        if (comment == null)
            return false;
        if ((isNew && commentRepository.findById(comment.getId()) != null) || (!isNew && commentRepository.findById(comment.getId()) == null))
            return false;
        if (comment.getAuthor() == null)
            return false;
        if (comment.getContent() == null)
            comment.setContent("");
        if (comment.getRating() == null)
            comment.setRating(new Double(0.0));
        if (comment.getLastModifiedDate() == null)
            comment.setLastModifiedDate(new Date());
        return true;
    }

}
