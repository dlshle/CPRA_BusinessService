package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.wisc.business.model.BusinessModel.Season;
import org.wisc.business.model.BusinessModel.Term;

import java.util.List;

@Repository
public interface TermDAO extends MongoRepository<Term, String> {
    List<Term> findAllByAverageRating(Double rating);
    List<Term> findAllBySeason(Season season);
    List<Term> findAllByYear(Integer year);
    List<Term> findAllByCourseId(String courseId);
}
