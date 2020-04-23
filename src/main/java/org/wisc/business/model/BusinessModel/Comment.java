package org.wisc.business.model.BusinessModel;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Comment")
@ToString
public class Comment implements Serializable {

    private static final long serialVersionUID = -8985545025018236854L;

    private String id;

    private String termId;
    private String content;
    @Field("author")
    private String authorId;
    @Field("lastEditor")
    private String lastEditedBy;
    private Date lastModifiedDate;
    private Double rating;

    @Override
    public boolean equals(Object o) {
        try {
            Comment c = (Comment)o;
            return id.equals(c.getId());
        } catch (Exception ex) {
            return false;
        }
    }
}
