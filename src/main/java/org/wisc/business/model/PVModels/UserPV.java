package org.wisc.business.model.PVModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.UserModel.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserPV implements Serializable {
    private String id;
    private String email;
    private String username;
    private String name;
    private String passwd;
    private List<TermPV> favorite;
    private Date createdDate;
    private boolean isAdmin = false;

    public UserPV(User user, List<TermPV> favorite) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.favorite = favorite;
        this.createdDate = user.getCreatedDate();
        this.passwd = user.getPassword();
    }

    public User toRawType() {
        List<String> tIds = new ArrayList<>(favorite==null?0:favorite.size());
        if (favorite != null)
            favorite.forEach((f)->tIds.add(f.getId()));
        return User.builder().id(id).email(email).username(username).name(name).favorite(tIds).createdDate(createdDate).password(passwd).isAdmin(isAdmin).build();
    }
}
