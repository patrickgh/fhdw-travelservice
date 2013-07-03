package de.fhdw.travelservice.ws;

public class HotelService {

    Hotel[] hotels;

    public HotelService() {

        RoomType avantgarde = new RoomType("Avantgarde",
                                           2, true, 5000f);
        RoomType business = new RoomType("Business", 2, true, 200f);
        RoomType basic = new RoomType("Basic", 1, false, 75f);

        Hotel hotel1 = new Hotel("AX001",
                                 "Axis2 Grand Hotel",
                                 "München", 5,
                                 new RoomType[]{avantgarde,
                                     business,
                                     basic});

        RoomType vip = new RoomType("VIP", 1, true, 2500f);
        RoomType manager = new RoomType("Manager", 1, true, 175f);
        RoomType basic4two = new RoomType("Basic4Two", 2, true, 80f);

        Hotel hotel2 = new Hotel("AX010",
                                 "Axis2 Plaza",
                                 "Hamburg", 4,
                                 new RoomType[]{vip,
                                     manager,
                                     basic4two});

        RoomType bettenLager = new RoomType("Bettenlager", 4,
                                            false, 15f);
        RoomType matrazenLager = new RoomType("Matrazenlager", 6,
                                              false, 5f);

        Hotel hotel3 = new Hotel("AX050",
                                 "Achsenhütte",
                                 "Unterammergau", 1,
                                 new RoomType[]{bettenLager,
                                     matrazenLager});

        hotels = new Hotel[]{hotel1, hotel2, hotel3};
    }

    public Hotel[] getHotels() {
        return hotels;
    }

    public Hotel findHotel(String hotelCode) {

        for (int i = 0; i < hotels.length; i++) {
            if (hotels[i].getHotelCode().equals(hotelCode)) { return hotels[i]; }
        }
        return null;
    }
}

