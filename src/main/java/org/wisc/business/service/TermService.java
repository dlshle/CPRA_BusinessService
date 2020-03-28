package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.TermDAO;
import org.wisc.business.model.BusinessModel.Course;
import org.wisc.business.model.BusinessModel.Professor;
import org.wisc.business.model.BusinessModel.Season;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.CommentPV;
import org.wisc.business.model.PVModels.CoursePV;
import org.wisc.business.model.PVModels.ProfessorPV;
import org.wisc.business.model.PVModels.TermPV;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TermService {
    @Autowired
    TermDAO termDAO;

    @Resource
    CourseService courseService;

    @Resource
    CommentService commentService;

    @Resource
    ProfessorService professorService;

    public TermPV convertTermToTermPV(Term term) {
        if (term == null)
            return null;
        List<CommentPV> comments = new LinkedList<>();
        if (term.getCommentIds() != null)
            term.getCommentIds().forEach((cId)->commentService.findById(cId));
        Course course = courseService.findRawById(term.getCourseId());
        List<Professor> professors = new LinkedList<>();
        if (term.getProfessorIds() != null)
            term.getProfessorIds().forEach((pId)->professors.add(professorService.findRawById(pId)));
        return new TermPV(term, course, professors, comments);
    }

    public List<TermPV> convertTermsToTermPVs(List<Term> terms) {
        List<TermPV> results = new LinkedList<>();
        for (Term term:terms) {
            results.add(convertTermToTermPV(term));
        }
        return results;
    }

    public Term findRawById(String id) {
        if (id == null)
            return null;
        Optional<Term> t = termDAO.findById(id);
        if (!t.isPresent())
            return null;
        return t.get();
    }

    public TermPV findById(String id) {
        return convertTermToTermPV(findRawById(id));
    }

    public List<TermPV> findBySeason(Season season) {
        return convertTermsToTermPVs(termDAO.findAllBySeason(season));
    }

    public List<TermPV> findByYear(Integer year) {
        return convertTermsToTermPVs(termDAO.findAllByYear(year));
    }

    public List<TermPV> findByAverageRating(Double rating) {
        return convertTermsToTermPVs(termDAO.findAllByAverageRating(rating));
    }

    public List<TermPV> findByCourseId(String courseId) {
        return convertTermsToTermPVs(termDAO.findAllByCourseId(courseId));
    }

    public List<TermPV> all() {
        return convertTermsToTermPVs(termDAO.findAll());
    }

    public TermPV add(Term term) {
        return convertTermToTermPV(termDAO.save(term));
    }

    public Term update(Term term) {
        Term oldTerm = findRawById(term.getId());
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
