package de.urlaubr.ws;

import de.urlaubr.ws.domain.UserSession;
import de.urlaubr.ws.domain.Vacation;
import de.urlaubr.ws.utils.UrlaubrWsUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<Vacation> getTopseller(Integer sessionKey) {
        if(!isAuthenticated(sessionKey)) {
            return null;
        }
        return Collections.emptyList();
    }
}