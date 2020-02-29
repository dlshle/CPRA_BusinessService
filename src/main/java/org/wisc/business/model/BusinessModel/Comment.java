package org.wisc.business.model.BusinessModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wisc.business.model.UserModel.User;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private String id;
    private String content;
    private User author;
    private Date lastModifiedDate;
    private double rating;
}
