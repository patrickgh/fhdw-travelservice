package de.urlaubr.ws.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 14.08.13
 */
public class SearchParams {
    /*
    * titel (enthält)
    * land
    * sterne
    * verpflegung
    * datum
    * reisedauer
    * */

    private String title;
    private List<String> country = new ArrayList<String>();
    private List<String> homeairport = new ArrayList<String>();
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

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
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

    public List<String> getHomeairport() {
        return homeairport;
    }

    public void setHomeairport(List<String> homeairport) {
        this.homeairport = homeairport;
    }
}
