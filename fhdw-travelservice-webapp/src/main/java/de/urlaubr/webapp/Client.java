package de.urlaubr.webapp;

import de.urlaubr.ws.TravelService;
import de.urlaubr.ws.TravelServiceImpl;
import de.urlaubr.ws.client.TravelClient;
import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.Customer;
import de.urlaubr.ws.domain.SearchParams;
import de.urlaubr.ws.domain.Traveler;
import de.urlaubr.ws.domain.Vacation;
import org.apache.axis2.AxisFault;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Wrapper class for client methods.
 * Useful for converting arrays to lists which are required for wicket listviews.
 * It also enables to change the service class between webservice and direct database access, so that no axis2 is required (for development)
 *
 * @author Patrick Groß-Holtwick
 *         Date: 27.08.13
 */
public class Client {

    //private static TravelService service = new TravelClient(); //use webservice
    private static TravelService service = new TravelServiceImpl(); //use direct database calls

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

    public static List<Booking> getMyVacation(Integer sessionKey) {
        try {
            return Arrays.asList(service.getMyVacations(sessionKey));
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static List<Vacation> findVacations(SearchParams params) {
        return Arrays.asList(service.findVacations(params));
    }

    public static Integer createBooking(Integer sessionKey, Integer vacationId, Date startdate, List<Traveler> travelers) {
        return service.createBooking(sessionKey, vacationId, startdate, travelers.toArray(new Traveler[travelers.size()]));
    }

    public static void cancelBooking(Integer sessionKey, Integer bookingId) {
        service.cancelBooking(sessionKey, bookingId);
    }

    public static void rateVacation(Integer sessionKey, Integer bookingId, Integer rating, String comment) {
        service.rateVacation(sessionKey, bookingId, rating, comment);
    }

    public static Vacation getVactationById(Integer id) {
        return service.getVacationById(id);
    }

    public static Customer getUserInfo(Integer sessionKey) {
        return service.getUserInfo(sessionKey);
    }

    public static Booking getBookingById(Integer sessionKey, Integer bookingId) {
        return service.getBookingById(sessionKey,bookingId);
    }

}
