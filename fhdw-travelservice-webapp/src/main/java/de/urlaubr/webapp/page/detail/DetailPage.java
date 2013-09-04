package de.urlaubr.webapp.page.detail;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.page.BasePage;
import de.urlaubr.ws.domain.Rating;
import de.urlaubr.ws.domain.Vacation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;

import java.util.Arrays;
import java.util.Date;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 04.09.13
 */
public class DetailPage extends BasePage {

    public DetailPage() {
        super();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (getPageParameters().get("id") != null && getPageParameters().get("id").toInt(-1) != -1) {
            final Integer id = getPageParameters().get("id").toInt();
            final CompoundPropertyModel<Vacation> model = new CompoundPropertyModel<Vacation>(Client.getVactationById(id));
            add(new ByteArrayImage("image", model.<byte[]>bind("image")));
            add(new Label("title", model.<String>bind("title")));
            add(new Label("city", model.<String>bind("city")));
            add(new Label("country", model.<String>bind("country")));
            add(new Label("hotelstars", model.<String>bind("hotelstars")));
            add(new Label("catering", model.<Integer>bind("catering")));
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
                }
            };
            add(rating);

        }
        else {
            setResponsePage(getApplication().getHomePage());
        }
    }
}
