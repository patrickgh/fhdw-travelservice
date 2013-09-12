package de.urlaubr.ws.domain;

import java.util.Date;

/**
 * This class represents the <code>rating</code> object.
 * This class is a model class. Instances of this class represent <code>rating</code> objects.
 * The instance of the object stores the data of an <code>rating</code> during the runtime.
 *
 * @author Patrick Groß-Holtwick
 *         Date: 07.08.13
 */
public class Traveler {

    private Integer id;
    private String firstname;
    private String lastname;
    private Date birthday;
    private String passport;

    public Traveler() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
