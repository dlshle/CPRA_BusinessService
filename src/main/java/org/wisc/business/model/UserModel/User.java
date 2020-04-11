package org.wisc.business.model.UserModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private static final long serialVersionUID = -8985541232018238754L;

    private String id;

    @Indexed(name="email_index", unique=true)
    private String email;
    @Indexed(name="username_index", unique=true)
    private String username;
    private String name;
    // !! use passwd on the front-end !!
    @JsonIgnore
    @JsonProperty(value="passwd")
    private String password;
    // id list of favorite terms
    private List<String> favorite;
    private Date createdDate;
    @JsonIgnore
    private String salt;
    private boolean isAdmin = false;
}
