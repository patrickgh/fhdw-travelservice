package de.urlaubr.webapp.components.panel;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 04.09.13
 */
public class StarRatingPanel extends GenericPanel<Integer> {

    Integer maxValue = 5;

    public StarRatingPanel(String id, IModel<Integer> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new ListView<Boolean>("imageList", getValueList()) {
            @Override
            protected void populateItem(ListItem<Boolean> item) {
                if (item.getModelObject()) {
                    item.add(new Image("image", new PackageResourceReference(ImportResourceLocator.class, "images/Star.png")));
                }
                else {
                    item.add(new Image("image", new PackageResourceReference(ImportResourceLocator.class, "images/Star_white.png")));
                }
            }
        });
    }

    private List<Boolean> getValueList() {
        List<Boolean> result = new ArrayList<Boolean>();
        for (int i = 1; i <= maxValue; i++) {
            if (i <= getModelObject()) {
                result.add(true);
            }
            else {
                result.add(false);
            }
        }
        return result;
    }

    public void setMaxValue(Integer max) {
        this.maxValue = max;
    }
}
