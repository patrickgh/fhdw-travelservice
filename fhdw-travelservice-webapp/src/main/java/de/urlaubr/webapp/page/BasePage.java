package de.urlaubr.webapp.page;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import webresources.ImportResourceLocator;

/**
 * Abstract Base page. Every page in this application extend this class. Contains markup & components which are on every page (for example the logout link, if a user is logged in). Also integrates the resource paths (css, images & javascript)
 *
 * @author Patrick Groß-Holtwick
 *         Date: 21.08.13
 */
public abstract class BasePage extends WebPage {

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
//        response.render(CssHeaderItem.forReference(new PackageResourceReference(ImportResourceLocator.class, "css/bootstrap.css")));
//        response.render(CssHeaderItem.forReference(new PackageResourceReference(ImportResourceLocator.class, "css/bootstrap-datepicker.css")));
//        response.render(CssHeaderItem.forReference(new PackageResourceReference(ImportResourceLocator.class, "css/puzzles.css")));
//
//        response.render(JavaScriptHeaderItem.forReference(new JQueryPluginResourceReference(ImportResourceLocator.class, "js/bootstrap.js")));
//        response.render(JavaScriptHeaderItem.forReference(new JQueryPluginResourceReference(ImportResourceLocator.class, "js/jquery-ui-1.10.1.custom.js")));
//        response.render(JavaScriptHeaderItem.forReference(new JQueryPluginResourceReference(ImportResourceLocator.class, "js/jquery-ui-1.10.1.custom.js")));
//        response.render(JavaScriptHeaderItem.forReference(new JQueryPluginResourceReference(ImportResourceLocator.class, "js/puzzles.js")));
//        response.render(JavaScriptHeaderItem.forReference(new JQueryPluginResourceReference(ImportResourceLocator.class, "js/bootstrap-datepicker.js")));
    }

}
