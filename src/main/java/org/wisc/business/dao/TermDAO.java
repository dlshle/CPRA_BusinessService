package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wisc.business.model.BusinessModel.Season;
import org.wisc.business.model.BusinessModel.Term;

public interface TermDAO extends MongoRepository<Term, String> {
    Term findByAverageRating(double rating);
    Term findBySeason(Season season);
}
