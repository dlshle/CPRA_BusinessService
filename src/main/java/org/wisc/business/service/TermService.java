package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.TermDAO;
import org.wisc.business.model.BusinessModel.*;
import org.wisc.business.model.PVModels.CommentPV;
import org.wisc.business.model.PVModels.CoursePV;
import org.wisc.business.model.PVModels.ProfessorPV;
import org.wisc.business.model.PVModels.TermPV;

import javax.annotation.Resource;
import java.util.HashSet;
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
        if (term.getProfessorIds() == null)
            term.setProfessorIds(new LinkedList<>());
        if (term.getCourseId() == null)
            term.setCourseId("");
        if (term.getCommentIds() == null)
            term.setCommentIds(new LinkedList<>());
        term.setAverageRating(0.0);
        return convertTermToTermPV(termDAO.save(term));
    }

    public Term updateRaw(Term term) {
         Term oldTerm = findRawById(term.getId());
        if (oldTerm == null)
            return null;
        if (term.getYear() != null && !term.getYear().equals(oldTerm.getYear()))
            oldTerm.setYear(term.getYear());
        if (term.getSeason() != null && !term.getSeason().equals(oldTerm.getSeason()))
            oldTerm.setSeason(term.getSeason());
        if (term.getDescription() != null && !term.getDescription().equals(oldTerm.getDescription()))
            oldTerm.setDescription(term.getDescription());
        if (term.getCourseId() != null && !term.getCourseId().equals(oldTerm.getCourseId())) {
            oldTerm.setCourseId(term.getCourseId());
        }
        if (term.getProfessorIds() != null) {
            HashSet<String> originalPids = new HashSet<>(oldTerm.getCommentIds());
            List<String> newPids = new LinkedList<>();
            term.getProfessorIds().forEach((pid)->{
                if (!originalPids.contains(pid)) {
                    // to add
                    Professor p = professorService.findRawById(pid);
                    if (p != null) {
                        newPids.add(pid);
                    }
                } else {
                    //
                    originalPids.remove(pid);
                    newPids.add(pid);
                }
            });
            // to remove
            originalPids.forEach((pid)->{
                Professor p = professorService.findRawById(pid);
                if (p != null)
                        newPids.remove(pid);
            });
            oldTerm.setProfessorIds(newPids);
        }
        if (term.getName() != null && !term.getName().equals(oldTerm.getName()))
            oldTerm.setName(term.getName());
        return termDAO.save(oldTerm);
    }

    public TermPV update(Term term) {
        Term oldTerm = findRawById(term.getId());
        if (oldTerm == null)
            return null;
        if (term.getYear() != null && !term.getYear().equals(oldTerm.getYear()))
            oldTerm.setYear(term.getYear());
        if (term.getSeason() != null && !term.getSeason().equals(oldTerm.getSeason()))
            oldTerm.setSeason(term.getSeason());
        if (term.getDescription() != null && !term.getDescription().equals(oldTerm.getDescription()))
            oldTerm.setDescription(term.getDescription());
        if (term.getCourseId() != null && !term.getCourseId().equals(oldTerm.getCourseId())) {
            // remove tid from old
            Course course = courseService.findRawById(oldTerm.getCourseId());
            if (course != null && course.getTermsIds() != null && course.getTermsIds().remove(term.getId()))
                courseService.updateRaw(course);
            // add tid to new
            course = courseService.findRawById(term.getCourseId());
            if (course != null && course.getTermsIds() != null && course.getTermsIds().add(term.getId()))
                courseService.updateRaw(course);
            oldTerm.setCourseId(term.getCourseId());
        }
        if (term.getProfessorIds() != null) {
            // check difference and take action
            HashSet<String> originalPids = new HashSet<>(oldTerm.getCommentIds());
            List<String> newPids = new LinkedList<>();
            term.getProfessorIds().forEach((pid)->{
                if (!originalPids.contains(pid)) {
                    // to add
                    Professor p = professorService.findRawById(pid);
                    if (p != null) {
                        // back-reference
                        if (p.getTermIds().add(term.getId()) && professorService.updateRaw(p) != null)
                            newPids.add(pid);
                    }
                } else {
                    //
                    originalPids.remove(pid);
                    newPids.add(pid);
                }
            });
            // to remove
            originalPids.forEach((pid)->{
                Professor p = professorService.findRawById(pid);
                if (p != null && p.getTermIds().remove(pid) && professorService.updateRaw(p) != null)
                        newPids.remove(pid);
            });
            oldTerm.setProfessorIds(newPids);
        }
        if (term.getName() != null && !term.getName().equals(oldTerm.getName()))
            oldTerm.setName(term.getName());
        return convertTermToTermPV(termDAO.save(oldTerm));
    }

    public boolean delete(Term term) {
        if (findById(term.getId()) == null)
            return false;
        if (term.getCourseId() != null) {
            Course c = courseService.findRawById(term.getCourseId());
            if (c.getTermsIds() != null)
                c.getTermsIds().remove(term.getId());
            courseService.updateRaw(c);
        }
        if (term.getProfessorIds() != null) {
            term.getProfessorIds().forEach((pid)->{
                Professor p = professorService.findRawById(pid);
                if (p != null && p.getTermIds().remove(term.getId()))
                    professorService.updateRaw(p);
            });
        }
        if (term.getCommentIds() != null) {
            term.getCommentIds().forEach((cid)->{
                Comment c = commentService.findRawById(cid);
                if (c != null)
                    commentService.deleteRaw(c);
            });
        }
        termDAO.delete(term);
        return true;
    }
}
