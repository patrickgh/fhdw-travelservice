package de.urlaubr.ws;

import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.BookingState;
import de.urlaubr.ws.domain.Customer;
import de.urlaubr.ws.domain.Rating;
import de.urlaubr.ws.domain.SearchParams;
import de.urlaubr.ws.domain.UserSession;
import de.urlaubr.ws.domain.Vacation;
import de.urlaubr.ws.utils.UrlaubrWsUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelServiceImpl implements TravelService {

    public static final String DEFAULT_URL = "jdbc:mysql://localhost/urlaubr.";
    public static final String DEFAULT_USER = "root";
    public static final String DEFAULT_PASSWORD = "";
    public static final int SESSION_TIMEOUT = 1800000; // 1800 sec => 30 min
    private static Map<Integer, UserSession> sessions = new HashMap<Integer, UserSession>();
    private Connection dbConnection;

    public TravelServiceImpl() {
        //use credentials from properties, if available, otherwise use defaults
        String url = System.getProperty("db.url") != null ? System.getProperty("db.url") : DEFAULT_URL;
        String user = System.getProperty("db.user") != null ? System.getProperty("db.user") : DEFAULT_USER;
        String password = System.getProperty("db.password") != null ? System.getProperty("db.password") : DEFAULT_PASSWORD;

        //Database migration
        UrlaubrWsUtils.migrateDatabase(url, user, password);

        //create Connection
        try {
            // register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            // create connection
            dbConnection = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException err) {
            System.out.println("DB-Driver nicht gefunden!");
            err.printStackTrace();
        }
        catch (SQLException err) {
            System.out.println("Connect nicht möglich");
            err.printStackTrace();
        }
    }

    public Integer login(String username, String password) {
        String encryptedPassword = UrlaubrWsUtils.md5(password);
        if (encryptedPassword != null) {
            try {
                // use PreparedStatement to prevent SQL-Injection (hopefully :))
                PreparedStatement stmt = dbConnection.prepareStatement("select id, password from customer where username like ?");
                stmt.setString(1, username);
                stmt.execute();
                ResultSet result = stmt.getResultSet();
                if (result.next() && result.isLast()) {
                    if (result.getString("password").equals(encryptedPassword)) {
                        UserSession session = new UserSession(result.getInt("id"), new Date());
                        sessions.put(session.hashCode(), session);
                        return session.hashCode();
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void logout(Integer sessionKey) {
        if (sessions.containsKey(sessionKey)) {
            sessions.remove(sessionKey);
        }
    }

    public boolean isAuthenticated(Integer sessionKey) {
        if (sessions.containsKey(sessionKey)) {
            UserSession session = sessions.get(sessionKey);
            if (session.getTimestamp().getTime() > System.currentTimeMillis() - SESSION_TIMEOUT) {
                session.setTimestamp(new Date());
                sessions.put(sessionKey, session);
                return true;
            }
            sessions.remove(sessionKey);
        }
        return false;
    }

    public List<Vacation> getTopseller() {
        try {
            List<Vacation> vacations = new ArrayList<Vacation>();
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT `fk_vacation` as id FROM rating GROUP BY `fk_vacation` ORDER BY AVG(`rating`) DESC;");
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                vacations.add(getVacationById(result.getInt("id")));
            }
            return vacations;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Booking> getMyVacations(Integer sessionKey) {
        if (isAuthenticated(sessionKey)) {
            try {
                List<Booking> bookings = new ArrayList<Booking>();
                PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM booking WHERE fk_customer = ? ORDER BY creationdate DESC;");
                stmt.setInt(1, sessions.get(sessionKey).getUserId());
                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    bookings.add(createBookingFromResultSet(result));
                }
                return bookings;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    public Vacation getVacationById(Integer id) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM vacation WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next() && result.isLast()) {
                return createVacationFromResultSet(result);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Vacation createVacationFromResultSet(ResultSet result) {
        try {
            Vacation vac = new Vacation();
            vac.setId(result.getInt("id"));
            vac.setTitle(result.getString("title"));
            vac.setDescription(result.getString("description"));
            vac.setCreationdate(new Date(result.getTimestamp("creationdate").getTime()));
            vac.setAirport(result.getString("airport"));
            vac.setAvailablefrom(result.getDate("availablefrom"));
            vac.setAvailableto(result.getDate("availableto"));
            vac.setImage(result.getBytes("image"));
            vac.setHotelstars(result.getInt("hotelstars"));
            vac.setDuration(result.getInt("duration"));
            vac.setPrice(result.getDouble("price"));
            vac.setCountry(result.getString("country"));
            vac.setCity(result.getString("city"));
            vac.setHomeairport(result.getString("homeairport"));
            vac.setCatering(UrlaubrWsUtils.getCateringTypeFromInteger(result.getInt("catering")));
            vac.setRatings(getRatingsByVacationId(vac.getId()));
            return vac;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Rating> getRatingsByVacationId(Integer id) {
        List<Rating> result = new ArrayList<Rating>();
        try {
            final PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM rating WHERE `fk_vacation` = ? ORDER BY creationdate DESC");
            stmt.setInt(1, id);
            final ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                result.add(createRatingFromResultSet(resultSet));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Rating createRatingFromResultSet(ResultSet result) {
        Rating rating = new Rating();
        try {
            rating.setCreationdate(new Date(result.getTimestamp("creationdate").getTime()));
            rating.setAuthor(getCustomerById(result.getInt("fk_customer")));
            rating.setComment(result.getString("comment"));
            rating.setRating(result.getInt("rating"));
            return rating;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Booking createBookingFromResultSet(ResultSet result) {
        Booking booking = new Booking();
        try {
            booking.setCreationdate(new Date(result.getTimestamp("creationdate").getTime()));
            booking.setStartdate(new Date(result.getTimestamp("startdate").getTime()));
            booking.setEnddate(new Date(result.getTimestamp("returndate").getTime()));
            booking.setState(UrlaubrWsUtils.getBookingStateFromInteger(result.getInt("state")));
            booking.setVacation(getVacationById(result.getInt("fk_vacation")));
            booking.setCustomer(getCustomerById(result.getInt("fk_customer")));
            return booking;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Customer getCustomerById(Integer id) {
        final Customer customer = new Customer();
        try {
            final PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM customer WHERE id = ?");
            stmt.setInt(1, id);
            final ResultSet result = stmt.executeQuery();
            if (result.next() && result.isLast()) {
                customer.setCreationdate(new Date(result.getTimestamp("creationdate").getTime()));
                customer.setEmail(result.getString("email"));
                customer.setFirstname(result.getString("firstname"));
                customer.setLastname(result.getString("lastname"));
                customer.setUsername(result.getString("username"));
                customer.setId(id.longValue());
                customer.setPassword(result.getString("password"));
                return customer;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Booking getBookingById(Integer bookingId) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM booking WHERE id = ?");
            stmt.setInt(1, bookingId);
            ResultSet resultSet = stmt.getResultSet();
            if (resultSet.next() && resultSet.isLast()) {
                return createBookingFromResultSet(resultSet);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void rateVacation(Integer sessionKey, Integer bookingId, Integer rating, String comment) {
        if (isAuthenticated(sessionKey)) {
            Booking booking = getBookingById(bookingId);
            if (booking != null && booking.getCustomer().getId().intValue() == sessions.get(sessionKey).getUserId()) {
                changeBookingState(bookingId, BookingState.FINISHED);
                try {
                    PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO rating VALUES(null,?,?,?,?,?)");
                    stmt.setInt(1, booking.getCustomer().getId().intValue());
                    stmt.setInt(2, booking.getVacation().getId());
                    stmt.setInt(3, rating);
                    stmt.setString(4, comment);
                    stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    stmt.execute();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void cancelBooking(Integer sessionKey, Integer bookingId) {
        if (isAuthenticated(sessionKey)) {
            Booking booking = getBookingById(bookingId);
            if (booking != null && booking.getCustomer().getId().intValue() == sessions.get(sessionKey).getUserId()) {
                changeBookingState(bookingId, BookingState.CANCELED);
            }
        }
    }

    private void changeBookingState(Integer bookingId, BookingState newState) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("UPDATE `booking` SET `state` = ? WHERE `id` = ?;");
            stmt.setInt(1, newState.ordinal());
            stmt.setInt(2, bookingId);
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vacation> findVacations(SearchParams params) {
        return Collections.emptyList();
    }
}