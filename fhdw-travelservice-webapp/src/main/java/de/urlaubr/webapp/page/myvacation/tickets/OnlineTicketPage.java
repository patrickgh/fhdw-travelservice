package de.urlaubr.webapp.page.myvacation.tickets;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.page.SecuredPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.BookingState;
import de.urlaubr.ws.domain.Traveler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import java.util.Arrays;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 07.09.13
 */
public class OnlineTicketPage extends SecuredPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final Integer bookingId;
        if (getPageParameters().get("id") != null && (bookingId = getPageParameters().get("id").toInt(-1)) != -1) {
            Booking booking = Client.getBookingById(getSessionKey(), bookingId);
            if (booking != null && booking.getCustomer() != null && booking.getCustomer().getId().equals(Client.getUserInfo(getSessionKey()).getId()) && booking.getState() != BookingState.FINISHED.ordinal()) {
                final WebMarkupContainer ticket = new WebMarkupContainer("ticket");
                ticket.setVisible(false);
                ticket.add(new ByteArrayImage("image"));
                ticket.add(new Label("name"));

                final WebMarkupContainer selection = new WebMarkupContainer("selectTraveler");
                selection.add(new ListView<Traveler>("traveler", new ListModel<Traveler>(Arrays.<Traveler>asList(booking.getTraveler()))) {
                    @Override
                    protected void populateItem(final ListItem<Traveler> item) {
                        final IModel<String> nameModel = new AbstractReadOnlyModel<String>() {
                            @Override
                            public String getObject() {
                                return item.getModelObject().getFirstname() + " " + item.getModelObject().getLastname();
                            }
                        };
                        Link link = new Link("travelerLink") {
                            @Override
                            public void onClick() {
                                selection.setVisible(false);
                                ticket.setVisible(true);
                                ticket.addOrReplace(new ByteArrayImage("image", new Model<byte[]>(Client.getOnlineTicket(getSessionKey(), bookingId, item.getModelObject().getId()))));
                                ticket.addOrReplace(new Label("name", nameModel));
                            }
                        };
                        link.add(new Label("travelerName", nameModel));
                        item.add(link);
                    }
                });
                add(selection);
                add(ticket);
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
