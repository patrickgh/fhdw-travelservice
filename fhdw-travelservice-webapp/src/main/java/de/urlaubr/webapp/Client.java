package de.urlaubr.webapp;

import de.urlaubr.ws.TravelService;
import de.urlaubr.ws.client.TravelClient;
import de.urlaubr.ws.domain.Vacation;

import java.util.Arrays;
import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 27.08.13
 */
public class Client {

    private static TravelService service = new TravelClient();

    private Client() {}

    public static List<Vacation> getTopseller() {
        return Arrays.<Vacation>asList(service.getTopseller());
    }

    public static Integer login(String username, String password) {
        return service.login(username, password);
    }

    public static void logout(Integer sessionKey) {
        service.logout(sessionKey);
    }
}
