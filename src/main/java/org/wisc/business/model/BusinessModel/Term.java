package org.wisc.business.model.BusinessModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Term {
    private String id;
    private String description;
    private double averageRating;
    private Season season;
    private List<Comment> comments;
}
