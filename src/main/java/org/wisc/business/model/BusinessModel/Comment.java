package org.wisc.business.model.BusinessModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.wisc.business.model.UserModel.User;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = -8985545025018236854L;

    @Id
    @Indexed
    private String id;
    private String content;
    private User author;
    private Date lastModifiedDate;
    private Double rating;
}
