package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.ProfessorDAO;
import org.wisc.business.model.BusinessModel.Professor;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.ProfessorPV;
import org.wisc.business.model.PVModels.TermPV;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {
    @Autowired
    ProfessorDAO professorDAO;

    @Resource
    TermService termService;

    public ProfessorPV convertProfessorToProfessorPV(Professor professor) {
        if (professor == null)
            return null;
        List<Term> terms = new LinkedList<>();
        if (professor.getTermIds() != null)
            professor.getTermIds().forEach((tId)->terms.add(termService.findRawById(tId)));
        return new ProfessorPV(professor, terms);
    }

    public List<ProfessorPV> convertProfessorsToProfessorPVs(List<Professor> professors) {
        List<ProfessorPV> results = new LinkedList<>();
        professors.forEach((p)->{results.add(convertProfessorToProfessorPV(p));});
        return results;
    }

    public Professor findRawById(String id) {
        if (id == null)
            return null;
        Optional<Professor> result = professorDAO.findById(id);
        if (!result.isPresent())
            return null;
        return result.get();
    }

    public ProfessorPV findById(String id) {
        return convertProfessorToProfessorPV(findRawById(id));
    }

    public List<ProfessorPV> findByName(String name) {
        return convertProfessorsToProfessorPVs(professorDAO.findAllByNameLike(name));
    }

    public ProfessorPV findFirstOccuranceByName(String name) {
        return convertProfessorToProfessorPV(professorDAO.findByName(name));
    }

    public List<ProfessorPV> all() {
        return convertProfessorsToProfessorPVs(professorDAO.findAll());
    }

    public ProfessorPV add(Professor professor) {
        if (professor.getTermIds() == null)
            professor.setTermIds(new LinkedList<>());
        return convertProfessorToProfessorPV(professorDAO.save(professor));
    }

    public Professor updateRaw(Professor professor) {
        return professorDAO.save(professor);
    }

    public ProfessorPV update(Professor professor) {
        Professor oldProfessor = findRawById(professor.getId());
        if (oldProfessor == null)
            return null;
        if (professor.getName() != null && !professor.getName().equals(oldProfessor.getName()))
            oldProfessor.setName(professor.getName());
        if (professor.getDescription() != null && !professor.getDescription().equals(oldProfessor.getDescription()))
            oldProfessor.setDescription(professor.getDescription());
        if (professor.getTermIds() != null) {
            HashSet<String> originalTids =
                    new HashSet<>(oldProfessor.getTermIds());
            LinkedList<String> newTids = new LinkedList<>();
            professor.getTermIds().forEach((tid)->{
                if (originalTids.contains(tid)) {
                    originalTids.remove(tid);
                    newTids.add(tid);
                } else {
                    // to add
                    Term t = termService.findRawById(tid);
                    // back reference
                    if (t != null)
                        if (t.getProfessorIds().add(professor.getId()) && termService.updateRaw(t) != null) {
                            newTids.add(tid);
                        }
                }
            });
            // to remove
            originalTids.forEach((tid)->{
                Term t = termService.findRawById(tid);
                if (t != null && t.getProfessorIds() != null && t.getProfessorIds().remove(professor.getId()) && termService.updateRaw(t) != null)
                    newTids.remove(tid);
            });
            oldProfessor.setTermIds(newTids);
        }
        return convertProfessorToProfessorPV(professorDAO.save(oldProfessor));
    }

    public boolean delete(Professor professor) {
        if (findById(professor.getId()) == null)
            return false;
        // update terms
        if (professor.getTermIds() != null) {
            professor.getTermIds().forEach((tid)->{
                Term t = termService.findRawById(tid);
                if (t != null && t.getProfessorIds().remove(professor.getId()))
                    termService.updateRaw(t);
            });
        }
        professorDAO.delete(professor);
        return true;
    }
}
