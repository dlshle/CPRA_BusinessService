package org.wisc.business.model.UserModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User
 * User class represents the user for CPR Mobile app.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "User")
public class User implements Serializable {
    @Id
    @Indexed
    private String id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    @Field("favorite")
    private List<Long> favoriteIds;
    private Date createdDate;
}
