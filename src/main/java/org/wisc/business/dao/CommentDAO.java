package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.UserModel.User;

public interface CommentDAO extends MongoRepository<Comment, String> {
    Comment findByAuthor(User author);
}
