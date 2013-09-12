package de.urlaubr.ws.domain;

import java.util.Date;

/**
 * This class represents the <code>customer</code> object.
 * This class is a model class. Instances of this class represent <code>customer</code> objects.
 * The instance of the object stores the data of an <code>customer</code> during the runtime.
 *
 * @author Patrick Groß-Holtwick
 *         Date: 30.07.13
 */

public class Customer {

    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private String password;

    private String email;

    private Date creationdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }
}
