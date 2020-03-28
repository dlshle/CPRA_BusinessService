package org.wisc.business.model.PVModels;

import lombok.*;
import org.wisc.business.model.BusinessModel.*;

import javax.naming.Name;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TermPV implements Serializable {
    private String id;
    private NameIdPair course;
    private String name;
    private String description;
    private Double averageRating;
    private Integer year;
    private Season season;
    private List<NameIdPair> professors;
    private List<CommentPV> comments;

    public TermPV(Term term, Course course, List<Professor> professors,
                  List<CommentPV> comments) {
        this.id = term.getId();
        if (course != null)
            this.course = new NameIdPair(course.getName(), course.getId());
        this.name = term.getName();
        this.description = term.getDescription();
        this.averageRating = term.getAverageRating();
        this.year = term.getYear();
        this.season = term.getSeason();
        List<NameIdPair> pairs = new ArrayList<>(professors.size());
        professors.forEach((p)->pairs.add(new NameIdPair(p.getName(), p.getId())));
        this.professors = pairs;
        this.comments = comments;
    }

    public Term toRawType() {
        List<String> pIds = new ArrayList<>(professors.size());
        List<String> cIds = new ArrayList<>(comments.size());
        if (professors != null)
            professors.forEach((p)->pIds.add(p.id));
        if (comments != null)
            comments.forEach((c)->cIds.add(c.getId()));
        return new Term(id, course==null?null:course.id, name, description,
                averageRating,
                year, season, pIds, cIds);
    }

}
