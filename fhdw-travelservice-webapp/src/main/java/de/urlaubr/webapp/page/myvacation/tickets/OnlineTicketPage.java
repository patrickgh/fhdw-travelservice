package de.urlaubr.webapp.page.myvacation.tickets;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.page.SecuredPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.BookingState;
import de.urlaubr.ws.domain.Traveler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Arrays;

/**
 * This page shows all traveler for a booking. The user can select one and gets directed to the e-ticket (qr-code).
 * @author Patrick Gross-Holtwick
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
                        final PageParameters params = new PageParameters();
                        params.add("id", bookingId);
                        params.add("tid", item.getModelObject().getId());
                        BookmarkablePageLink link = new BookmarkablePageLink("travelerLink", ShowTicketPage.class, params);
                        link.add(new Label("travelerName", nameModel));
                        item.add(link);
                    }
                });
                add(selection);
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
