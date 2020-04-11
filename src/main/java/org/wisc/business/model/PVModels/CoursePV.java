package org.wisc.business.model.PVModels;

import lombok.*;
import org.wisc.business.model.BusinessModel.Course;
import org.wisc.business.model.BusinessModel.Term;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
        if (terms != null)
            terms.forEach((t)->{
                if (t != null && t.id != null)
                    tIds.add(t.id);
            });
        return new Course(id, name, description, tIds);
    }
}
