package de.urlaubr.ws;

import de.urlaubr.ws.domain.UserSession;
import de.urlaubr.ws.utils.UrlaubrWsUtils;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TravelService {

    public static final String DEFAULT_URL = "jdbc:mysql://localhost/urlaubr.";
    public static final String DEFAULT_USER = "root";
    public static final String DEFAULT_PASSWORD = "";
    public static final int SESSION_TIMEOUT = 1800000; // 1800 sec => 30 min
    private final Map<Integer, UserSession> sessions = new HashMap<Integer, UserSession>();

    private EntityManager em;

    public TravelService() {
        //use credentials from properties, if available, otherwise use defaults
        String url = System.getProperty("db.url") != null ? System.getProperty("db.url") : DEFAULT_URL;
        String user = System.getProperty("db.user") != null ? System.getProperty("db.user") : DEFAULT_USER;
        String password = System.getProperty("db.password") != null ? System.getProperty("db.password") : DEFAULT_PASSWORD;

        //Database migration
        UrlaubrWsUtils.migrateDatabase(url, user, password);

        //create EntityManger

    }

    public Integer login(String username, String password) {
        String encryptedPassword = UrlaubrWsUtils.md5(password);
        UserSession session = new UserSession(1, new Date());
        sessions.put(session.hashCode(),session);
        return session.hashCode();
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
}