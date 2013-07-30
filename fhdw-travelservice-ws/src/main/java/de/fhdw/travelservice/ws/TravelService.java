package de.fhdw.travelservice.ws;

import de.fhdw.travelservice.ws.domain.UserSession;

import java.util.HashMap;
import java.util.Map;

public class TravelService {

    public static final int SESSION_TIMEOUT = 1800000; // 1800 sec => 30 min

    private final Map<String, UserSession> sessions = new HashMap<String, UserSession>();

    public TravelService() {

    }

    public String login(String username, String password) {
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

