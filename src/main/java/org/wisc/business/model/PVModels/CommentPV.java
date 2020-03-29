package org.wisc.business.model.PVModels;

import lombok.*;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.BusinessModel.Term;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentPV implements Serializable {
    private String id;
    private NameIdPair term;
    private String content;
    private UserPV author;
    private UserPV lastEditedBy;
    private Date lastModifiedDate;
    private Double rating;

    public CommentPV(Comment comment, Term term, UserPV author,
                     UserPV lastEditedBy) {
        this.id = comment.getId();
        if (term != null)
            this.term = new NameIdPair(term.getName(), term.getId());
        this.content = comment.getContent();
        this.author = author;
        this.lastEditedBy = lastEditedBy;
        this.lastModifiedDate = comment.getLastModifiedDate();
        this.rating = comment.getRating();
    }

    public Comment toRawType() {
        return new Comment(id, (term==null?null:term.getId()), content,
                author==null?null:author.getId(), lastEditedBy==null?null:
                lastEditedBy.getId(),
                lastModifiedDate, rating);
    }
}
