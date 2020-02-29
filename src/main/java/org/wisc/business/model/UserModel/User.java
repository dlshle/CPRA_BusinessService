package org.wisc.business.model.UserModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wisc.business.model.BusinessModel.Term;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private List<Term> favorite;
    private Date createdDate;

    public boolean addFavoriate(Term term) {
        if (term != null && !favorite.contains(term)) {
            favorite.add(term);
            return true;
        }
        return false;
    }
}
