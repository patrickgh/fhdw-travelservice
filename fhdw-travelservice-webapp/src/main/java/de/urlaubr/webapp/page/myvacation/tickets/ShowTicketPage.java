package de.urlaubr.webapp.page.myvacation.tickets;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.page.SecuredPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.BookingState;
import de.urlaubr.ws.domain.Traveler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * this page shows the e-ticket. It expects an id (booking-id) and a tid (traveler-id) parameter.
 * @author Patrick Gross-Holtwick
 *         Date: 09.09.13
 */
public class ShowTicketPage extends SecuredPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final Integer bookingId;
        final Integer travelerId;
        if (getPageParameters().get("id") != null && (bookingId = getPageParameters().get("id").toInt(-1)) != -1 &&
            getPageParameters().get("tid") != null && (travelerId = getPageParameters().get("tid").toInt(-1)) != -1) {
            Booking booking = Client.getBookingById(getSessionKey(), bookingId);
            Traveler traveler = null;
            for (Traveler t : booking.getTraveler()) {
                if (travelerId.equals(t.getId())) {
                    traveler = t;
                }
            }
            if (booking.getCustomer() != null && booking.getCustomer().getId().equals(Client.getUserInfo(getSessionKey()).getId()) && booking.getState() != BookingState.FINISHED.ordinal() &&
                traveler != null) {
                add(new ByteArrayImage("image", new Model<byte[]>(Client.getOnlineTicket(getSessionKey(), bookingId, travelerId))));
                add(new Label("name", new Model<String>(traveler.getFirstname() + " " + traveler.getLastname())));
            }
            else {
                setResponsePage(MyVacationPage.class);
            }
        }
        else {
            setResponsePage(MyVacationPage.class);
        }
    }
}
