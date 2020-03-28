package org.wisc.business.model.PVModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.wisc.business.model.BusinessModel.Comment;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@ToString
public class CommentPV implements Serializable {
    private String id;
    private String termId;
    private String content;
    private UserPV author;
    private UserPV lastEditedBy;
    private Date lastModifiedDate;
    private Double rating;

    public CommentPV(Comment comment, UserPV author,
                     UserPV lastEditedBy) {
        this.id = comment.getId();
        this.termId = comment.getTermId();
        this.content = comment.getContent();
        this.author = author;
        this.lastEditedBy = lastEditedBy;
        this.lastModifiedDate = comment.getLastModifiedDate();
        this.rating = comment.getRating();
    }

    public Comment toRawType() {
        return new Comment(id, termId, content, author.getId(),
                lastEditedBy.getId(), lastModifiedDate, rating);
    }
}
