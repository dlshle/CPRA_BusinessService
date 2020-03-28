package org.wisc.business.model.PVModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.wisc.business.model.BusinessModel.Professor;
import org.wisc.business.model.BusinessModel.Term;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ProfessorPV implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<NameIdPair> terms;

    public ProfessorPV(Professor professor, List<Term> terms) {
        this.id = professor.getId();
        this.name = professor.getName();
        this.description = professor.getDescription();
        List<NameIdPair> termPair = new ArrayList<>(terms.size());
        terms.forEach((t)->termPair.add(new NameIdPair(t.getName(),
                t.getId())));
        this.terms = termPair;
    }

    public Professor toRawType() {
        List<String> tIds = new LinkedList<>();
        terms.forEach((t)->tIds.add(t.id));
        return new Professor(id, name, description, tIds);
    }
}
