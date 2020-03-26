package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.wisc.business.model.BusinessModel.Professor;

import java.util.List;

@Repository
public interface ProfessorDAO extends MongoRepository<Professor, String> {
    List<Professor> findAllByNameLike(String name);
    Professor findByName(String name);
}
