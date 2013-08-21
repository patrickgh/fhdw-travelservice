package de.urlaubr.ws;

import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.Customer;
import de.urlaubr.ws.domain.SearchParams;
import de.urlaubr.ws.domain.Traveler;
import de.urlaubr.ws.domain.Vacation;
import org.apache.axis2.AxisFault;

import java.util.Date;
import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 31.07.13
 */
public interface TravelService {

    Integer login(String username, String password);

    void logout(Integer sessionKey);

    Customer getUserInfo(Integer sessionKey);

    List<Vacation> getTopseller();

    List<Booking> getMyVacations(Integer sessionKey) throws AxisFault;

    Vacation getVacationById(Integer id);

    void rateVacation(Integer sessionKey, Integer bookingId, Integer rating, String comment);

    void cancelBooking(Integer sessionKey, Integer bookingId);

    List<Vacation> findVacations(SearchParams params);

    Integer createBooking(Integer sessionKey, Integer vacationId, Date startdate, List<Traveler> travelers);
}
