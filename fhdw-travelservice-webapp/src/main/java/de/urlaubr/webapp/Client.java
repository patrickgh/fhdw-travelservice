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
 * It provides static methods for calling the webservice (makes sure that there is always only one client instance)
 * Useful for converting arrays to lists which are required for wicket listviews.
 * It also enables to change the service class between webservice and direct database access, so that development without a running axis2 service is possible.
 *
 * @author Patrick Gross-Holtwick
 *         Date: 27.08.13
 */
public class Client {

    private static TravelService service = new TravelClient(); //use webservice
    //private static TravelService service = new TravelServiceImpl(); //use direct database calls

    private Client() {}

    public static List<Vacation> getTopseller() {
        try {
            return Arrays.<Vacation>asList(service.getTopseller());
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static Integer login(String username, String password) {
        try {
            return service.login(username, password);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void logout(Integer sessionKey) {
        try {
            service.logout(sessionKey);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
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
        try {
            return Arrays.asList(service.findVacations(params));
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return Collections.emptyList();

    }

    public static Integer createBooking(Integer sessionKey, Integer vacationId, Date startdate, List<Traveler> travelers) {
        try {
            return service.createBooking(sessionKey, vacationId, startdate, travelers.toArray(new Traveler[travelers.size()]));
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void cancelBooking(Integer sessionKey, Integer bookingId) {
        try {
            service.cancelBooking(sessionKey, bookingId);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
    }

    public static void rateVacation(Integer sessionKey, Integer bookingId, Integer rating, String comment) {
        try {
            service.rateVacation(sessionKey, bookingId, rating, comment);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
    }

    public static Vacation getVactationById(Integer id) {
        try {
            return service.getVacationById(id);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Customer getUserInfo(Integer sessionKey) {
        try {
            return service.getUserInfo(sessionKey);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Booking getBookingById(Integer sessionKey, Integer bookingId) {
        try {
            return service.getBookingById(sessionKey, bookingId);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getOnlineTicket(Integer sessionKey, Integer bookingId, Integer travelerId) {
        try {
            return service.createTicket(sessionKey, bookingId, travelerId);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void registerUser(String firstname, String lastname, String username, String email, String password) {
        try {
            service.registerCustomer(firstname, lastname, username, email, password);
        }
        catch (AxisFault e) {
            e.printStackTrace();
        }
    }
}
