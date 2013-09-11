package de.urlaubr.webapp.page.myvacation;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.page.SecuredPage;
import de.urlaubr.webapp.page.myvacation.detail.BookingDetailPage;
import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.utils.UrlaubrWsUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

import java.util.List;

/**
 * the user area which shows all booked vacations for the user which is logged in.
 * @author Patrick Groß-Holtwick
 *         Date: 28.08.13
 */
public class MyVacationPage extends SecuredPage {

    public MyVacationPage() {
        super();
        add(new Image("logo", new PackageResourceReference(ImportResourceLocator.class, "images/urlaubr.png")));
        add(new Link("logout") {
            @Override
            public void onClick() {
                Client.logout(getSessionKey());
                getSession().removeAttribute("sessionKey");
                setResponsePage(getApplication().getHomePage());
            }
        });
        IModel<List<Booking>> model = new
            AbstractReadOnlyModel<List<Booking>>() {
                @Override
                public List<Booking> getObject() {
                    return Client.getMyVacation(getSessionKey());
                }
            };
        add(new ListView<Booking>("myVacationList", model) {

            @Override
            protected void populateItem(ListItem<Booking> item) {
                final CompoundPropertyModel<Booking> model = new CompoundPropertyModel<Booking>(item.getModel());
                final String resourceKey = "booking.state." + UrlaubrWsUtils.getBookingStateFromInteger(model.getObject().getState()).name().toLowerCase();
                PageParameters parameters = new PageParameters();
                parameters.add("id", model.getObject().getId());
                final BookmarkablePageLink link = new BookmarkablePageLink("vacationLink", BookingDetailPage.class, parameters);
                link.add(new Label("title", model.<String>bind("vacation.title")));
                link.add(new Label("persons", new AbstractReadOnlyModel<Integer>() {
                    @Override
                    public Integer getObject() {
                        if (model.getObject().getTraveler() != null) {
                            return model.getObject().getTraveler().length;
                        }
                        return 0;
                    }
                }));
                link.add(new Label("state", new ResourceModel(resourceKey, resourceKey)));
                link.add(new ByteArrayImage("image", model.<byte[]>bind("vacation.image")));
                item.add(link);
            }
        });
    }
}
