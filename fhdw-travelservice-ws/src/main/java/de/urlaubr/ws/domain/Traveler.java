package de.urlaubr.ws.domain;

import java.util.Date;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 07.08.13
 */
public class Traveler {

    private String firstname;
    private String lastname;
    private Date birthday;
    private String passport;

    public Traveler() {
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
