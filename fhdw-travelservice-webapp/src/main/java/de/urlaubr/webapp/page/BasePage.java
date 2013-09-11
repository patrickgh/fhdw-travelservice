package de.urlaubr.webapp.page;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import webresources.ImportResourceLocator;

/**
 * Abstract Base page. Every page in this application extends this class.
 * Contains markup & components which are on every page (for example the header bar).
 * Also integrates the resource paths (css, images & javascript)
 *
 * @author Patrick Groß-Holtwick
 *         Date: 21.08.13
 */
public abstract class BasePage extends WebPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new PackageResourceReference(ImportResourceLocator.class, "js/jquery.mobile-1.3.1.css")));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(ImportResourceLocator.class, "js/star-rating/jquery.rating.css")));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(ImportResourceLocator.class, "css/urlaubr.css")));

        response.render(JavaScriptHeaderItem.forReference(new JQueryPluginResourceReference(ImportResourceLocator.class, "js/jquery.mobile-1.3.1.js")));

    }

}
