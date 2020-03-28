package org.wisc.business.model.PVModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.wisc.business.model.BusinessModel.Course;
import org.wisc.business.model.BusinessModel.Term;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ToString
public class CoursePV implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<NameIdPair> terms;

    public CoursePV(Course course, List<Term> terms) {
        this.id = course.getId();
        this.name = course.getName();
        this.description = course.getDescription();
        List<NameIdPair> termPair = new ArrayList<>(terms.size());
        terms.forEach((t)->termPair.add(new NameIdPair(t.getName(), t.getId())));
        this.terms = termPair;
    }

    public Course toRawType() {
        List<String> tIds = new LinkedList<>();
        terms.forEach((t)->tIds.add(t.id));
        return new Course(id, name, description, tIds);
    }
}
