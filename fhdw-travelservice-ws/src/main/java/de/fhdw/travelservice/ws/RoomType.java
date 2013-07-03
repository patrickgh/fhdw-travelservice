package de.fhdw.travelservice.ws;

public class RoomType {

    private String roomCode;
    private int numberOfBeds;
    private boolean isRoomWithTV;
    private float priceInEuros;

    // Der Default-Konstruktor ist sehr wichtig, da Axis2
    // immer den Default-Konstruktur aufruft!!!
    public RoomType() {
    }

    public RoomType(String roomCode, int numberOfBeds, boolean isRoomWithTV, float priceInEuros) {
        this.roomCode = roomCode;
        this.numberOfBeds = numberOfBeds;
        this.isRoomWithTV = isRoomWithTV;
        this.priceInEuros = priceInEuros;
    }

    public boolean isRoomWithTV() {
        return isRoomWithTV;
    }

    public void setRoomWithTV(boolean isRoomWithTV) {
        this.isRoomWithTV = isRoomWithTV;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public float getPriceInEuros() {
        return priceInEuros;
    }

    public void setPriceInEuros(float priceInEuros) {
        this.priceInEuros = priceInEuros;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}
