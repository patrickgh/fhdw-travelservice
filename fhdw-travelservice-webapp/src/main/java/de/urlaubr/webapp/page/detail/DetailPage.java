package de.urlaubr.webapp.page.detail;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.components.panel.StarRatingPanel;
import de.urlaubr.webapp.page.BasePage;
import de.urlaubr.webapp.page.booking.BookingPage;
import de.urlaubr.ws.domain.Rating;
import de.urlaubr.ws.domain.Vacation;
import de.urlaubr.ws.utils.UrlaubrWsUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Arrays;
import java.util.Date;

/**
 * Detail page for a given vacation.
 * It displays some attributes (description, title, image, ...) and a link to the booking page.
 * This page expects an id paramater which must be a valid vacation id.
 * @author Patrick Gross-Holtwick
 *         Date: 04.09.13
 */
public class DetailPage extends BasePage {

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (getPageParameters().get("id") != null && getPageParameters().get("id").toInt(-1) != -1) {
            final Integer id = getPageParameters().get("id").toInt();
            final CompoundPropertyModel<Vacation> model = new CompoundPropertyModel<Vacation>(Client.getVactationById(id));
            final String resourceKey = "catering." + UrlaubrWsUtils.getCateringTypeFromInteger(model.getObject().getCatering()).name().toLowerCase();
            add(new ByteArrayImage("image", model.<byte[]>bind("image")));
            add(new Label("title", model.<String>bind("title")));
            add(new Label("city", model.<String>bind("city")));
            add(new Label("country", model.<String>bind("country")));
            add(new Label("hotelstars", model.<String>bind("hotelstars")));
            add(new Label("catering", new ResourceModel(resourceKey, resourceKey)));
            add(new StarRatingPanel("starrating", new AbstractReadOnlyModel<Integer>() {
                @Override
                public Integer getObject() {
                    return Long.valueOf(Math.round(model.getObject().getAvgRating())).intValue();
                }
            }));
            add(new Label("price", model.<Double>bind("price")));
            final Label description = new Label("description", model.<String>bind("description"));
            description.setEscapeModelStrings(false);
            add(description);

            ListView<Rating> rating = new ListView<Rating>("rating", Arrays.asList(model.getObject().getRatings())) {
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
            PageParameters parameters = new PageParameters();
            parameters.add("id", model.getObject().getId());
            add(new BookmarkablePageLink("bookingLink", BookingPage.class, parameters));
        }
        else {
            setResponsePage(getApplication().getHomePage());
        }
    }
}
