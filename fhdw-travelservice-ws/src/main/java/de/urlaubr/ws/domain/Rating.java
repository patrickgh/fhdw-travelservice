package de.urlaubr.ws.domain;

import java.util.Date;

/**
 * This class represents the <code>rating</code> object.
 * This class is a model class. Instances of this class represent <code>rating</code> objects.
 * The instance of the object stores the data of an <code>rating</code> during the runtime.
 *
 * @author Patrick Gross-Holtwick
 *         Date: 07.08.13
 */
public class Rating {

    private Integer rating;
    private String comment;
    private Date creationdate;
    private Customer author;

    public Rating() {
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public Customer getAuthor() {
        return author;
    }

    public void setAuthor(Customer author) {
        this.author = author;
    }
}
