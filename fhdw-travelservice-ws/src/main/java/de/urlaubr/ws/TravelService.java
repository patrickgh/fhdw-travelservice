package de.urlaubr.ws;

import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.Vacation;

import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 31.07.13
 */
public interface TravelService {

    Integer login(String username, String password);

    void logout(Integer sessionKey);

    List<Vacation> getTopseller();

    List<Booking> getMyVacations(Integer sessionKey);

    Vacation getVacationById(Integer id);
}
