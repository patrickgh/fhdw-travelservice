package de.urlaubr.webapp.page.myvacation.detail;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.components.panel.StarRatingPanel;
import de.urlaubr.webapp.page.SecuredPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.Rating;
import de.urlaubr.ws.utils.UrlaubrWsUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

import java.util.Arrays;
import java.util.Date;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 04.09.13
 */
public class BookingDetailPage extends SecuredPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (getPageParameters().get("id") != null && getPageParameters().get("id").toInt(-1) != -1) {
            final Integer id = getPageParameters().get("id").toInt();
            final CompoundPropertyModel<Booking> model = new CompoundPropertyModel<Booking>(Client.getBookingById(getSessionKey(), id));
            final String resourceKey = "booking.state." + UrlaubrWsUtils.getCateringTypeFromInteger(model.getObject().getState()).name().toLowerCase();
            final String cateringResourceKey = "catering." + UrlaubrWsUtils.getCateringTypeFromInteger(model.getObject().getVacation().getCatering()).name().toLowerCase();
            add(new ByteArrayImage("image", model.<byte[]>bind("vacation.image")));
            add(new Label("title", model.<String>bind("vacation.title")));
            add(new Label("country", model.<String>bind("vacation.country")));
            add(new Label("city", model.<String>bind("vacation.city")));
            add(new Label("hotelstars", model.<Integer>bind("vacation.hotelstars")));
            add(new Label("catering", new ResourceModel(cateringResourceKey, cateringResourceKey)));
            add(new StarRatingPanel("starrating", new AbstractReadOnlyModel<Integer>() {
                @Override
                public Integer getObject() {
                    return Long.valueOf(Math.round(model.getObject().getVacation().getAvgRating())).intValue();
                }
            }));
            add(new Label("state", new ResourceModel(resourceKey, resourceKey)));
            final Label description = new Label("description", model.<String>bind("vacation.description"));
            description.setEscapeModelStrings(false);
            add(description);

            ListView<Rating> rating = new ListView<Rating>("rating", Arrays.asList(model.getObject().getVacation().getRatings())) {
                @Override
                protected void populateItem(ListItem<Rating> item) {
                    final CompoundPropertyModel<Rating> itemModel = new CompoundPropertyModel<Rating>(item.getModel());
                    item.add(new Label("text", itemModel.<String>bind("comment")));
                    item.add(new Label("user", itemModel.<String>bind("author.username")));
                    item.add(new Label("date", itemModel.<Date>bind("creationdate")));
                    item.add(new StarRatingPanel("rating", itemModel.<Integer>bind("rating")));
                }
            };
            add(rating);
        }
        else {
            setResponsePage(MyVacationPage.class);
        }
    }
}
