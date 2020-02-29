package org.wisc.business.model.BusinessModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Term")
public class Term implements Serializable {
    @Id
    @Indexed
    private Long id;
    private String description;
    private double averageRating;
    private Season season;
    @Field("comments")
    private List<Long> commentIds;
}
