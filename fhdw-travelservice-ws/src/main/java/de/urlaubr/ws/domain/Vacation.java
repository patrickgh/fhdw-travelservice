package de.urlaubr.ws.domain;

import java.util.Date;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 31.07.13
 */
public class Vacation {

    private Integer id;
    private String title;
    private String description;
    private Date creationdate;
    private Date availablefrom;
    private Date availableto;
    private byte[] image;
    private Integer hotelstars;
    private Integer duration;
    private Double price;
    private String country;
    private String city;
    private String airport;
    private String homeairport;
    private Integer catering;
    private Rating[] ratings;

    public String getHomeairport() {
        return homeairport;
    }

    public void setHomeairport(String homeairport) {
        this.homeairport = homeairport;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public Date getAvailablefrom() {
        return availablefrom;
    }

    public void setAvailablefrom(Date availablefrom) {
        this.availablefrom = availablefrom;
    }

    public Date getAvailableto() {
        return availableto;
    }

    public void setAvailableto(Date availableto) {
        this.availableto = availableto;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getHotelstars() {
        return hotelstars;
    }

    public void setHotelstars(Integer hotelstars) {
        this.hotelstars = hotelstars;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public Integer getCatering() {
        return catering;
    }

    public void setCatering(Integer catering) {
        this.catering = catering;
    }

    public Rating[] getRatings() {
        return ratings;
    }

    public void setRatings(Rating[] ratings) {
        this.ratings = ratings;
    }

    public Double getAvgRating() {
        if (ratings != null && ratings.length > 0) {
            Integer sum = 0;
            for (Rating rating : ratings) {
                sum += rating.getRating();
            }
            return sum.doubleValue() / ratings.length;
        }
        return null;
    }
}
