package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.ProfessorDAO;
import org.wisc.business.model.BusinessModel.Professor;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {
    @Autowired
    ProfessorDAO professorDAO;

    public Professor findById(String id) {
        if (id == null)
            return null;
        Optional<Professor> result = professorDAO.findById(id);
        if (!result.isPresent())
            return null;
        return result.get();
    }

    public List<Professor> findByName(String name) {
        return professorDAO.findAllByNameLike(name);
    }

    public Professor findFirstOccuranceByName(String name) {
        return professorDAO.findByName(name);
    }

    public List<Professor> all() {
        return professorDAO.findAll();
    }

    public Professor add(Professor professor) {
        return professorDAO.save(professor);
    }

    public Professor update(Professor professor) {
        Professor oldProfessor = findById(professor.getId());
        if (oldProfessor == null)
            return null;
        if (professor.getName() != null && !professor.getName().equals(oldProfessor.getName()))
            oldProfessor.setName(professor.getName());
        if (professor.getDescription() != null && !professor.getDescription().equals(oldProfessor.getDescription()))
            oldProfessor.setDescription(professor.getDescription());
        if (professor.getTermIds() != null)
            oldProfessor.setTermIds(professor.getTermIds());
        return professorDAO.save(oldProfessor);
    }

    public boolean delete(Professor professor) {
        if (findById(professor.getId()) == null)
            return false;
        professorDAO.delete(professor);
        return true;
    }
}
