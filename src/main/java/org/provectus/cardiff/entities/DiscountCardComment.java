package org.provectus.cardiff.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "discount_card_history")
public class DiscountCardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String comment;
    @Column(name = "comment_document")
    private Date commentDocument;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentDocument() {
        return commentDocument;
    }

    public void setCommentDocument(Date commentDocument) {
        this.commentDocument = commentDocument;
    }
}
