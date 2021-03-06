package de.urlaubr.ws;

import com.truemesh.squiggle.SelectQuery;
import com.truemesh.squiggle.Table;
import com.truemesh.squiggle.criteria.InCriteria;
import com.truemesh.squiggle.criteria.MatchCriteria;
import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.BookingState;
import de.urlaubr.ws.domain.Customer;
import de.urlaubr.ws.domain.Rating;
import de.urlaubr.ws.domain.SearchParams;
import de.urlaubr.ws.domain.Traveler;
import de.urlaubr.ws.domain.UserSession;
import de.urlaubr.ws.domain.Vacation;
import de.urlaubr.ws.utils.UrlaubrWsUtils;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.axis2.AxisFault;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The webservice implementation.
 * This class contains implementations for the methods from the TravelService interface.
 * It handles the database connection, validates the parameters and returns the results.
 * @author Patrick Gross-Holtwick
 */
public class TravelServiceImpl implements TravelService {

    //default configuration paramter
    public static final String DEFAULT_URL = "jdbc:mysql://localhost/urlaubr.";
    public static final String DEFAULT_USER = "root";
    public static final String DEFAULT_PASSWORD = "";
    public static final int SESSION_TIMEOUT = 1800000; // 1800 sec => 30 min
    //private attributes
    private static Map<Integer, UserSession> sessions = new HashMap<Integer, UserSession>();
    private Connection dbConnection;

    /**
     * Constructor.
     * It establishes the database connection object, which is used in the methods.
     */
    public TravelServiceImpl() {
        //use credentials from properties, if available, otherwise use defaults
        String url = System.getProperty("db.url") != null ? System.getProperty("db.url") : DEFAULT_URL;
        String user = System.getProperty("db.user") != null ? System.getProperty("db.user") : DEFAULT_USER;
        String password = System.getProperty("db.password") != null ? System.getProperty("db.password") : DEFAULT_PASSWORD;

        //create Connection
        try {
            // register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            // create connection
            dbConnection = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException err) {
            System.out.println("DB-Driver not found");
            err.printStackTrace();
        }
        catch (SQLException err) {
            System.out.println("Connect not possible");
            err.printStackTrace();
        }

        //Database migration (only for use in IDE)
        //UrlaubrWsUtils.migrateDatabase(url, user, password);
    }

    /**
     * authenticates a user and returns a sessionKey which can be used for further requests
     * @param username
     * @param password
     * @return sessionKey which is valid for 30 minutes
     * @throws AxisFault
     */
    public Integer login(String username, String password) throws AxisFault {
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
                throw new AxisFault("an unexpected error happened. please contact webservice administrator");
            }
        }
        throw new AxisFault("invalid parameter");
    }

    /**
     * removes a session (logout)
     * @param sessionKey
     */
    public void logout(Integer sessionKey) {
        if (sessions.containsKey(sessionKey)) {
            sessions.remove(sessionKey);
        }
    }

    /**
     * convenience method which checks if a given sessionKey is valid.
     * @param sessionKey
     * @return true if the user is logged in, false if not (or sessionKey expired)
     */
    private boolean isAuthenticated(Integer sessionKey) {
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

    /**
     * returns the 5 best rated vacations.
     * @return a list with vacation objects.
     */
    public Vacation[] getTopseller() throws AxisFault {
        try {
            List<Vacation> vacations = new ArrayList<Vacation>();
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT `fk_vacation` as id FROM rating GROUP BY `fk_vacation` ORDER BY AVG(`rating`) DESC LIMIT 5;");
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                vacations.add(getVacationById(result.getInt("id")));
            }
            return vacations.toArray(new Vacation[vacations.size()]);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
    }

    /**
     * returns the 5 last bookings a user has made.
     * @param sessionKey
     * @return a list with booking objects.
     */
    public Booking[] getMyVacations(Integer sessionKey) throws AxisFault {
        if (isAuthenticated(sessionKey)) {
            try {
                List<Booking> bookings = new ArrayList<Booking>();
                PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM booking WHERE fk_customer = ? ORDER BY creationdate DESC LIMIT 5;");
                stmt.setInt(1, sessions.get(sessionKey).getUserId());
                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    bookings.add(createBookingFromResultSet(result));
                }
                return bookings.toArray(new Booking[bookings.size()]);
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new AxisFault("an unexpected error happened. please contact webservice administrator");
            }
        }
        throw new AxisFault("not authenticated");
    }

    /**
     * returns a vacation with a specific id.
     * @param id the vacation id
     * @return the vacation object
     */
    public Vacation getVacationById(Integer id) throws AxisFault {
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
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
        throw new AxisFault("vacation id not found");
    }

    /**
     * Returns the all ratings for a given vacation id.
     * @param id the vacation id.
     * @return a list with rating objects.
     */
    private Rating[] getRatingsByVacationId(Integer id) throws AxisFault {
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
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
        return result.toArray(new Rating[result.size()]);
    }

    /**
     * returns all traveler for a specific booking
     * @param bookingId the booking id
     * @return a list with Traveler objects.
     */
    private Traveler[] getTravelerList(Integer bookingId) throws AxisFault {
        List<Traveler> result = new ArrayList<Traveler>();
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM traveler WHERE fk_booking = ?");
            stmt.setInt(1, bookingId);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Traveler temp = new Traveler();
                temp.setId(resultSet.getInt("id"));
                temp.setBirthday(new Date(resultSet.getTimestamp("birthdate").getTime()));
                temp.setPassport(resultSet.getString("passport"));
                temp.setFirstname(resultSet.getString("firstname"));
                temp.setLastname(resultSet.getString("lastname"));
                result.add(temp);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
        return result.toArray(new Traveler[result.size()]);
    }

    /**
     * this method creates a new rating for a vacation.
     * @param sessionKey a valid session key
     * @param bookingId a booking id (which must be booked by the same user as the one in the active session)
     * @param rating the rating (1-5 stars)
     * @param comment the comment
     */
    public void rateVacation(Integer sessionKey, Integer bookingId, Integer rating, String comment) throws AxisFault {
        if (isAuthenticated(sessionKey)) {
            Booking booking = getBookingById(bookingId);
            if (booking != null
                && booking.getCustomer() != null
                && booking.getVacation() != null
                && booking.getCustomer().getId().intValue() == sessions.get(sessionKey).getUserId()
                && booking.getState() != BookingState.FINISHED.ordinal()) {
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
                    throw new AxisFault("an unexpected error happened. please contact webservice administrator");
                }
                changeBookingState(bookingId, BookingState.FINISHED);
            }
        }
    }

    /**
     * this method cancels a booking
     * @param sessionKey a valid session key
     * @param bookingId a booking id (which must be booked by the same user as the one in the active session)
     */
    public void cancelBooking(Integer sessionKey, Integer bookingId) throws AxisFault {
        if (isAuthenticated(sessionKey)) {
            Booking booking = getBookingById(bookingId);
            if (booking != null && booking.getCustomer().getId().intValue() == sessions.get(sessionKey).getUserId()) {
                changeBookingState(bookingId, BookingState.CANCELED);
            }
        }
        else {
            throw new AxisFault("not authenticated");
        }
    }

    /**
     * this methods searches for vacations in the database
     * @param params a SearchParams object
     * @return an Array with vacation objects
     * @throws AxisFault
     */
    public Vacation[] findVacations(SearchParams params) throws AxisFault {
        SelectQuery select = new SelectQuery();
        Table vacation = new Table("vacation");
        select.addColumn(vacation, "*");
        if (params.getTitle() != null) {
            select.addCriteria(new MatchCriteria(vacation, "title", MatchCriteria.LIKE, "%" + params.getTitle() + "%"));
        }

        if (params.getCountry() != null && params.getCountry().length > 0) {
            select.addCriteria(new InCriteria(vacation, "country", params.getCountry()));
        }

        if (params.getHomeairport() != null && params.getHomeairport().length > 0) {
            select.addCriteria(new InCriteria(vacation, "homeairport", params.getHomeairport()));
        }

        if (params.getCatering() != null) {
            select.addCriteria(new MatchCriteria(vacation, "catering", MatchCriteria.GREATEREQUAL, params.getCatering().ordinal()));
        }

        if (params.getDuration() != null) {
            select.addCriteria(new MatchCriteria(vacation, "duration", MatchCriteria.EQUALS, params.getDuration()));
        }

        if (params.getHotelstars() != null) {
            select.addCriteria(new MatchCriteria(vacation, "hotelstars", MatchCriteria.GREATEREQUAL, params.getHotelstars()));
        }
        select.addOrder(vacation, "price", true);
        try {
            PreparedStatement stmt = dbConnection.prepareStatement(select.toString());
            ResultSet resultSet = stmt.executeQuery();
            List<Vacation> result = new ArrayList<Vacation>();
            while (resultSet.next()) {
                result.add(createVacationFromResultSet(resultSet));
            }
            return result.toArray(new Vacation[result.size()]);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
    }

    /**
     * this method creates a new booking in the database.
     * @param sessionKey a valid sessionKey
     * @param vacationId a valid vacation id
     * @param startdate the desired start date
     * @param travelers an array with the travelers
     * @return the generated booking id
     */
    public Integer createBooking(Integer sessionKey, Integer vacationId, Date startdate, Traveler[] travelers) throws AxisFault {
        if (isAuthenticated(sessionKey)) {
            try {
                Vacation vacation = getVacationById(vacationId);
                if (vacation != null && startdate != null && startdate.getTime() > System.currentTimeMillis() && travelers != null && travelers.length > 0) {
                    if (UrlaubrWsUtils.checkDateRange(startdate, vacation.getAvailablefrom(), vacation.getAvailableto())) {
                        PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO booking VALUES (null,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                        stmt.setInt(1, vacationId);
                        stmt.setInt(2, sessions.get(sessionKey).getUserId());
                        stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                        stmt.setTimestamp(4, new Timestamp(startdate.getTime()));
                        stmt.setTimestamp(5, new Timestamp(startdate.getTime() + vacation.getDuration() * 24 * 60 * 60 * 1000));
                        stmt.setInt(6, travelers.length);
                        stmt.setInt(7, BookingState.CREATED.ordinal());
                        stmt.executeUpdate();
                        ResultSet result = stmt.getGeneratedKeys();
                        if (result.next() && result.isLast()) {
                            int bookingId = result.getInt(1);
                            for (Traveler traveler : travelers) {
                                stmt = dbConnection.prepareStatement("INSERT INTO traveler VALUES (null,?,?,?,?,?)");
                                stmt.setString(1, traveler.getFirstname());
                                stmt.setString(2, traveler.getLastname());
                                stmt.setDate(3, new java.sql.Date(traveler.getBirthday().getTime()));
                                stmt.setInt(4, bookingId);
                                stmt.setString(5, traveler.getPassport());
                                stmt.executeUpdate();
                            }
                            return bookingId;
                        }
                    }
                    else {
                        throw new AxisFault("vacation ist not available for given start date");
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new AxisFault("an unexpected error happened. please contact webservice administrator");
            }
        }
        throw new AxisFault("invalid parameter");
    }

    /**
     * this method returns the user info of the user which is logged in
     * @param sessionKey a valid session key
     * @return the Customer object which belongs to the session
     */
    public Customer getUserInfo(Integer sessionKey) throws AxisFault {
        if (isAuthenticated(sessionKey)) {
            return getCustomerById(sessions.get(sessionKey).getUserId());
        }
        return null;
    }

    /**
     * this method generates an e-ticket as a qr-code.
     * @param sessionKey a valid session key
     * @param bookingId a booking id  (which must be booked by the same user as the one in the active session)
     * @param travelerId a traveler id (which must belong to the booking)
     * @return the qr-code image as a byte-array
     */
    public byte[] createTicket(Integer sessionKey, Integer bookingId, Integer travelerId) throws AxisFault {
        if (isAuthenticated(sessionKey)) {
            Booking booking = getBookingById(bookingId);
            if (booking.getCustomer().getId() == sessions.get(sessionKey).getUserId().intValue()) {
                for (Traveler traveler : booking.getTraveler()) {
                    if (travelerId.equals(traveler.getId())) {
                        return QRCode.from("passenger:" + traveler.getFirstname() + traveler.getLastname() + ";bookingId:" + bookingId + "origin:" + booking.getVacation().getHomeairport() + ";destination:" + booking.getVacation().getAirport() + ";flightdate:" + booking.getStartdate())
                                     .to(ImageType.PNG).withSize(300, 300).stream().toByteArray();
                    }
                }
            }
        }
        throw new AxisFault("invalid parameter");
    }

    /**
     * this method returns a booking with a specific id
     * @param sessionKey a valid session key
     * @param bookingId a booking id  (which must be booked by the same user as the one in the active session)
     * @return a booking object with the booking informations
     */
    public Booking getBookingById(Integer sessionKey, Integer bookingId) throws AxisFault {
        if (isAuthenticated(sessionKey)) {
            Booking booking = getBookingById(bookingId);
            if (booking.getCustomer().getId().intValue() == sessions.get(sessionKey).getUserId()) {
                return booking;
            }
        }
        throw new AxisFault("invalid parameter");
    }

    /**
     * this methods registers a new user with the given parameters.
     * @param firstname
     * @param lastname
     * @param username
     * @param email
     * @param password
     */
    public void registerCustomer(String firstname, String lastname, String username, String email, String password) throws AxisFault {
        if (firstname != null && lastname != null && username != null && email != null && password != null) {
            String encryptedPassword = UrlaubrWsUtils.md5(password);
            try {
                PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM customer WHERE username = ?");
                stmt.setString(1, username);
                ResultSet result = stmt.executeQuery();
                if (!result.next()) {
                    PreparedStatement insertStmt = dbConnection.prepareStatement("INSERT INTO customer VALUES(null,?,?,?,?,?,?)");
                    insertStmt.setString(1, firstname);
                    insertStmt.setString(2, lastname);
                    insertStmt.setString(3, username);
                    insertStmt.setString(4, email);
                    insertStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    insertStmt.setString(6, encryptedPassword);
                    insertStmt.execute();
                    return;
                }
                else {
                    throw new AxisFault("username already taken");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new AxisFault("an unexpected error happened. please contact webservice administrator");
            }
        }
        throw new AxisFault("invalid parameters");
    }

    /**
     * returns a customer with a specific id (only intended to use in other methodes, therefore private)
     * @param id the customer id.
     * @return a customer object.
     */
    private Customer getCustomerById(Integer id) throws AxisFault {
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
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
        throw new AxisFault("customer id not found");
    }

    /**
     * returns a booking with a specific id (only intended to use in other methodes, therefore private)
     * @param bookingId the customer id.
     * @return a booking object.
     */
    private Booking getBookingById(Integer bookingId) throws AxisFault {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM booking WHERE id = ?");
            stmt.setInt(1, bookingId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next() && resultSet.isLast()) {
                return createBookingFromResultSet(resultSet);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
        throw new AxisFault("booking id not found");
    }

    /**
     * this method changes the state of a booking (only intended to use in other methodes, therefore private)
     * @param bookingId the booking id.
     * @param newState the new booking-state.
     */
    private void changeBookingState(Integer bookingId, BookingState newState) throws AxisFault {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("UPDATE `booking` SET `state` = ? WHERE `id` = ?;");
            stmt.setInt(1, newState.ordinal());
            stmt.setInt(2, bookingId);
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
    }

    /**
     * this method creates a vacation object from a mysql result row (only intended to use in other methodes, therefore private)
     * @param result the result row.
     * @return a vacation object.
     */
    private Vacation createVacationFromResultSet(ResultSet result) throws AxisFault {
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
            vac.setCatering(result.getInt("catering"));
            vac.setRatings(getRatingsByVacationId(vac.getId()));
            return vac;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
    }

    /**
     * this method creates a rating object from a mysql result row (only intended to use in other methodes, therefore private)
     * @param result the result row.
     * @return a rating object.
     */
    private Rating createRatingFromResultSet(ResultSet result) throws AxisFault {
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
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
    }

    /**
     * this method creates a Booking object from a mysql result row (only intended to use in other methodes, therefore private)
     * @param result the result row.
     * @return a booking object.
     */
    private Booking createBookingFromResultSet(ResultSet result) throws AxisFault {
        Booking booking = new Booking();
        try {
            booking.setId(result.getInt("id"));
            booking.setCreationdate(new Date(result.getTimestamp("creationdate").getTime()));
            booking.setStartdate(new Date(result.getTimestamp("startdate").getTime()));
            booking.setEnddate(new Date(result.getTimestamp("returndate").getTime()));
            booking.setState(result.getInt("state"));
            booking.setVacation(getVacationById(result.getInt("fk_vacation")));
            booking.setCustomer(getCustomerById(result.getInt("fk_customer")));
            booking.setTraveler(getTravelerList(result.getInt("id")));
            return booking;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new AxisFault("an unexpected error happened. please contact webservice administrator");
        }
    }

}