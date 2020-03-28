package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.ProfessorDAO;
import org.wisc.business.model.BusinessModel.Professor;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.ProfessorPV;
import org.wisc.business.model.PVModels.TermPV;

import javax.annotation.Resource;
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
        return convertProfessorToProfessorPV(professorDAO.save(professor));
    }

    public ProfessorPV update(Professor professor) {
        Professor oldProfessor = findRawById(professor.getId());
        if (oldProfessor == null)
            return null;
        if (professor.getName() != null && !professor.getName().equals(oldProfessor.getName()))
            oldProfessor.setName(professor.getName());
        if (professor.getDescription() != null && !professor.getDescription().equals(oldProfessor.getDescription()))
            oldProfessor.setDescription(professor.getDescription());
        if (professor.getTermIds() != null)
            oldProfessor.setTermIds(professor.getTermIds());
        return convertProfessorToProfessorPV(professorDAO.save(oldProfessor));
    }

    public boolean delete(Professor professor) {
        if (findById(professor.getId()) == null)
            return false;
        professorDAO.delete(professor);
        return true;
    }
}
