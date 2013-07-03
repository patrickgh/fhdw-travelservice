package de.fhdw.travelservice.ws;

public class Hotel {

    private String hotelCode;
    private String hotelName;
    private String city;
    private int numberOfStars;
    private RoomType[] roomTypes;

    // Der Default-Konstruktor ist sehr wichtig, da Axis2
    // immer den Default-Konstruktur aufruft!!!
    public Hotel() {
    }

    public Hotel(String hotelCode, String hotelName,
                 String city, int numberOfStars,
                 RoomType[] roomTypes) {

        this.hotelCode = hotelCode;
        this.hotelName = hotelName;
        this.city = city;
        this.numberOfStars = numberOfStars;
        this.roomTypes = roomTypes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(int numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public RoomType[] getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(RoomType[] roomTypes) {
        this.roomTypes = roomTypes;
    }
}
