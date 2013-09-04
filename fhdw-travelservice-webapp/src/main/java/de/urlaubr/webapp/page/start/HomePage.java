package de.urlaubr.webapp.page.start;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.page.BasePage;
import de.urlaubr.webapp.page.detail.DetailPage;
import de.urlaubr.ws.TravelServiceImpl;
import de.urlaubr.ws.client.TravelClient;
import de.urlaubr.ws.domain.Vacation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

import java.util.Arrays;
import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 31.07.13
 */
public class HomePage extends BasePage {

    public HomePage() {
        super();
        add(new Image("logo", new PackageResourceReference(ImportResourceLocator.class, "images/urlaubr3.png")));
        IModel<List<Vacation>> model = new AbstractReadOnlyModel<List<Vacation>>() {
            @Override
            public List<Vacation> getObject() {
                return Client.getTopseller();
            }
        };
        add(new ListView<Vacation>("topsellerList", model) {

            @Override
            protected void populateItem(ListItem<Vacation> item) {
                final CompoundPropertyModel<Vacation> model = new CompoundPropertyModel<Vacation>(item.getModel());
                PageParameters parameters = new PageParameters();
                parameters.add("id",model.getObject().getId());
                final BookmarkablePageLink link = new BookmarkablePageLink("topsellerLink", DetailPage.class, parameters);
                link.add(new Label("title", model.<String>bind("title")));
                link.add(new Label("price", model.<String>bind("price")));
                link.add(new ByteArrayImage("image", model.<byte[]>bind("image")));
                item.add(link);
            }
        });
    }
}
