package de.urlaubr.ws.domain;

import java.util.Date;

/**
 * The objects of this class hold information for an active session. The code generated by the hashCode method becomes the session-key used in the webservice.
 * @author Patrick Gross-Holtwick
 *         Date: 30.07.13
 */
public class UserSession {

    private Date timestamp; //logindate / date of last request
    private Integer userId; //id of the user logged in

    public UserSession(Integer userId, Date timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        UserSession that = (UserSession) o;

        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) { return false; }
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
