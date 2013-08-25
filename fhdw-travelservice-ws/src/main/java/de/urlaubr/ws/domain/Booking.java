package de.urlaubr.ws.domain;

import java.util.Date;
import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 07.08.13
 */
public class Booking {
    private Vacation vacation;
    private Customer customer;
    private Date creationdate;
    private Date startdate;
    private Date enddate;
    private Integer state;
    private Traveler[] traveler;


    public Booking() {
    }

    public Traveler[] getTraveler() {
        return traveler;
    }

    public void setTraveler(Traveler[] traveler) {
        this.traveler = traveler;
    }

    public Vacation getVacation() {
        return vacation;
    }

    public void setVacation(Vacation vacation) {
        this.vacation = vacation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
