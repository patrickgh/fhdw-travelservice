package de.urlaubr.ws;

import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.Customer;
import de.urlaubr.ws.domain.SearchParams;
import de.urlaubr.ws.domain.Traveler;
import de.urlaubr.ws.domain.Vacation;
import org.apache.axis2.AxisFault;

import java.util.Date;

/**
 * TravelService Interface.
 * It defines the methods which the webservice should offer.
 * It is implemented in the service and the client, so that each new webservice method always gets an corresponding client method.
 * @author Patrick Gross-Holtwick
 *         Date: 31.07.13
 */
public interface TravelService {

    Integer login(String username, String password) throws AxisFault;

    void logout(Integer sessionKey) throws AxisFault;

    Customer getUserInfo(Integer sessionKey) throws AxisFault;

    Vacation[] getTopseller() throws AxisFault;

    Booking[] getMyVacations(Integer sessionKey) throws AxisFault;

    Vacation getVacationById(Integer id) throws AxisFault;

    void rateVacation(Integer sessionKey, Integer bookingId, Integer rating, String comment) throws AxisFault;

    void cancelBooking(Integer sessionKey, Integer bookingId) throws AxisFault;

    Vacation[] findVacations(SearchParams params) throws AxisFault;

    Integer createBooking(Integer sessionKey, Integer vacationId, Date startdate, Traveler[] travelers) throws AxisFault;

    byte[] createTicket(Integer sessionKey, Integer bookingId, Integer travelerId) throws AxisFault;

    Booking getBookingById(Integer sessionKey, Integer bookingId) throws AxisFault;

    void registerCustomer(String firstname, String lastname, String username, String email, String password) throws AxisFault;
}
