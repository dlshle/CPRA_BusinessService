package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wisc.business.model.BusinessModel.Professor;

public interface ProfessorDAO extends MongoRepository<Professor, String> {
    Professor findByName(String name);
}
