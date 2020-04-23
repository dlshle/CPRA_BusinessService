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
@Document(collection = "Course")
@ToString
public class Course implements Serializable {

    private static final long serialVersionUID = -8931545025018238754L;

    private String id;

    private String name;
    private String description;
    @Field("terms")
    private List<String> termsIds;

    @Override
    public boolean equals(Object o) {
        try {
            Course c = (Course) o;
            return c.getId().equals(id);
        } catch (Exception ex) {
            return false;
        }
    }
}
