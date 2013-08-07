package de.urlaubr.ws;

import de.urlaubr.ws.domain.Customer;
import de.urlaubr.ws.domain.Rating;
import de.urlaubr.ws.domain.UserSession;
import de.urlaubr.ws.domain.Vacation;
import de.urlaubr.ws.utils.UrlaubrWsUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final Map<Integer, UserSession> sessions = new HashMap<Integer, UserSession>();
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
                return true;
            }
            sessions.remove(sessionKey);
        }
        return false;
    }

    public List<Vacation> getTopseller() {
        try {
            List<Vacation> vacations = new ArrayList<Vacation>();
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT `fk_vacation` as id FROM ratings GROUP BY `fk_vacation` ORDER BY AVG(`rating`) DESC;");
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

    public List<Vacation> getMyVacations(Integer sessionKey) {
        if (!isAuthenticated(sessionKey)) {
            return null;
        }
        return Collections.emptyList();
    }

    public Vacation getVacationById(Integer id) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM vacations WHERE id = ?");
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
            final PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM ratings WHERE `fk_vacation` = ? ORDER BY creationdate DESC");
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
            return rating;
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
}