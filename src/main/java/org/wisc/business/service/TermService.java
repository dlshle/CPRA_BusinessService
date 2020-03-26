package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.TermDAO;
import org.wisc.business.model.BusinessModel.Season;
import org.wisc.business.model.BusinessModel.Term;

import java.util.List;

@Service
public class TermService {
    @Autowired
    TermDAO termDAO;

    public Term findById(String id) {
        if (id == null)
            return null;
        return termDAO.findById(id).get();
    }

    public List<Term> findBySeason(Season season) {
        return termDAO.findAllBySeason(season);
    }

    public List<Term> findByYear(Integer year) {
        return termDAO.findAllByYear(year);
    }

    public List<Term> findByAverageRating(Double rating) {
        return termDAO.findAllByAverageRating(rating);
    }

    public List<Term> findByCourseId(String courseId) {
        return termDAO.findAllByCourseId(courseId);
    }

    public List<Term> all() {
        return termDAO.findAll();
    }

    public Term add(Term term) {
        return termDAO.save(term);
    }

    public Term update(Term term) {
        Term oldTerm = findById(term.getId());
        if (oldTerm == null)
            return null;
        if (term.getYear() != null && !term.getYear().equals(oldTerm.getYear()))
            oldTerm.setYear(term.getYear());
        if (term.getSeason() != null && !term.getSeason().equals(oldTerm.getSeason()))
            oldTerm.setSeason(term.getSeason());
        if (term.getAverageRating() != null && !term.getAverageRating().equals(oldTerm.getAverageRating()))
            oldTerm.setAverageRating(term.getAverageRating());
        if (term.getCourseId() != null && !term.getCourseId().equals(oldTerm.getCourseId()))
            oldTerm.setCourseId(term.getCourseId());
        if (term.getDescription() != null && !term.getDescription().equals(oldTerm.getDescription()))
            oldTerm.setDescription(term.getDescription());
        if (term.getCommentIds() != null)
            oldTerm.setCommentIds(term.getCommentIds());
        if (term.getName() != null && !term.getName().equals(oldTerm.getName()))
            oldTerm.setName(term.getName());
        if (term.getProfessorIds() != null)
            oldTerm.setProfessorIds(term.getProfessorIds());
        return termDAO.save(oldTerm);
    }

    public boolean delete(Term term) {
        if (findById(term.getId()) == null)
            return false;
        termDAO.delete(term);
        return true;
    }
}
