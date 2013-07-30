package de.fhdw.travelservice.ws;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationInfo;
import de.fhdw.travelservice.ws.domain.UserSession;
import de.fhdw.travelservice.ws.utils.UrlaubrWsUtils;

import java.util.HashMap;
import java.util.Map;

public class TravelService {
    public static final String DEFAULT_URL = "jdbc:mysql://localhost/urlaubr.";
    public static final String DEFAULT_USER = "root";
    public static final String DEFAULT_PASSWORD = "";
    public static final int SESSION_TIMEOUT = 1800000; // 1800 sec => 30 min

    private final Map<String, UserSession> sessions = new HashMap<String, UserSession>();

    public TravelService() {
        //use credentials from properties, if available, otherwise use defaults
        String url = System.getProperty("db.url") != null ? System.getProperty("db.url") : DEFAULT_URL;
        String user = System.getProperty("db.user") != null ? System.getProperty("db.user") : DEFAULT_USER;
        String password = System.getProperty("db.password") != null ? System.getProperty("db.password") : DEFAULT_PASSWORD;

        //Database migration
        Flyway flyway = new Flyway();
        flyway.setDataSource(url,user,password);
        flyway.migrate();
        MigrationInfo[] migrationInfos = flyway.info().all();
        final StringBuilder builder = new StringBuilder("migration status:\n");
        builder.append("--------------------------------------------------------------------------------------------- \n");
        builder.append(String.format("| %12s | %8s | %20s | %-40s | \n", "version", "state", "installed", "description"));
        builder.append("--------------------------------------------------------------------------------------------- \n");
        for (MigrationInfo patch : migrationInfos) {
            builder.append(
                String.format("| %12s | %8s | %20s | %-40s | \n",
                              patch.getVersion(),
                              patch.getState().getDisplayName(),
                              patch.getInstalledOn(),
                              patch.getDescription()));
        }
        builder.append("--------------------------------------------------------------------------------------------- ");
        System.out.println(builder.toString());


    }

    public String login(String username, String password) {
        String encryptedPassword = UrlaubrWsUtils.md5(password);
        return "";
    }

    public void logout(String sessionKey) {
        if(sessions.containsKey(sessionKey)) {
            sessions.remove(sessionKey);
        }
    }

    private boolean isAuthenticated(String sessionKey) {
        if(sessions.containsKey(sessionKey)) {
            UserSession session = sessions.get(sessionKey);
            if(session.getTimestamp().getTime() > System.currentTimeMillis() - SESSION_TIMEOUT) {
                return true;
            }
            sessions.remove(sessionKey);
        }
        return false;
    }
}