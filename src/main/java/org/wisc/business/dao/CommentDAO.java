package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.wisc.business.model.BusinessModel.Comment;

import java.util.List;

@Repository
public interface CommentDAO extends MongoRepository<Comment, String> {
    List<Comment> findAllByAuthorId(String authorId);
}
