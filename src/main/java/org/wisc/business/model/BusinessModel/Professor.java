package org.wisc.business.model.BusinessModel;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Professor")
@ToString
public class Professor implements Serializable {

    private static final long serialVersionUID = -8985545025018238334L;

    private String id;

    private String name;
    private String description;
    @Field("terms")
    private List<String> termIds;

    @Override
    public boolean equals(Object o) {
        try {
            Professor p = (Professor) o;
            return p.getId().equals(id);
        } catch (Exception ex) {
            return false;
        }
    }
}
