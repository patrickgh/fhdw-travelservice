package de.urlaubr.ws.domain;

import java.io.Serializable;

/**
 * This class holds informations for a vacation search. It is used as a parameter of the "findVacations" webservice method.
 * @author Patrick Gross-Holtwick
 *         Date: 14.08.13
 */
public class SearchParams implements Serializable {

    private String title;
    private String[] country = new String[0];
    private String[] homeairport = new String[0];
    private Integer hotelstars;
    private CateringType catering;
    private Integer duration;

    public SearchParams() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getHotelstars() {
        return hotelstars;
    }

    public void setHotelstars(Integer hotelstars) {
        this.hotelstars = hotelstars;
    }

    public CateringType getCatering() {
        return catering;
    }

    public void setCatering(CateringType catering) {
        this.catering = catering;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String[] getCountry() {
        return country;
    }

    public void setCountry(String[] country) {
        this.country = country;
    }

    public String[] getHomeairport() {
        return homeairport;
    }

    public void setHomeairport(String[] homeairport) {
        this.homeairport = homeairport;
    }
}
