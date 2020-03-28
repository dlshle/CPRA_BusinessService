package org.wisc.business.model.PVModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.UserModel.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ToString
public class UserPV implements Serializable {
    private String id;
    private String email;
    private String username;
    private String name;
    private String passwd;
    private List<TermPV> favorite;
    private Date createdDate;

    public UserPV(User user, List<TermPV> favorite) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.favorite = favorite;
        this.createdDate = user.getCreatedDate();
        this.passwd = user.getPassword();
    }

    public User toRawType() {
        List<String> tIds = new ArrayList<>(favorite.size());
        favorite.forEach((f)->tIds.add(f.getId()));
        return User.builder().id(id).email(email).username(username).name(name).favorite(tIds).createdDate(createdDate).password(passwd).build();
    }
}
