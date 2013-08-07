package de.urlaubr.ws.domain;

import java.util.Date;

/**
 * @author Patrick Groß-Holtwick
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
