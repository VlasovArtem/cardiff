package com.provectus.cardiff.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "discount_card_comment")
@AttributeOverride(name = "createdDate", column = @Column(name = "comment_date", insertable = false, updatable =
        false))
public class DiscountCardComment extends BaseEntity{
    @Column(name = "comment_text", length = 500, nullable = false)
    private String comment;

    public DiscountCardComment() {}

    public DiscountCardComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
