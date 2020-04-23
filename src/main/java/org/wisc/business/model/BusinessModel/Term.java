package org.wisc.business.model.BusinessModel;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Term")
@ToString
public class Term implements Serializable {
    private static final long serialVersionUID = -8985545042018238754L;

    private String id;

    private String courseId;
    private String name;
    private String description;
    private Double averageRating;
    private Integer year;
    private Season season;
    private List<String> professorIds;
    @Field("comments")
    private List<String> commentIds;

    @Override
    public boolean equals(Object o) {
        try {
            Term t = (Term) o;
            return t.getId().equals(id);
        } catch (Exception ex) {
            return false;
        }
    }
}
