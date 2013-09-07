package de.urlaubr.webapp;

import de.urlaubr.webapp.page.booking.BookingPage;
import de.urlaubr.webapp.page.detail.DetailPage;
import de.urlaubr.webapp.page.login.LoginPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.webapp.page.myvacation.detail.BookingDetailPage;
import de.urlaubr.webapp.page.myvacation.tickets.OnlineTicketPage;
import de.urlaubr.webapp.page.search.SearchPage;
import de.urlaubr.webapp.page.start.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;
import org.apache.wicket.resource.NoOpTextCompressor;
import org.apache.wicket.util.file.Path;
import org.apache.wicket.util.string.Strings;
import webresources.ImportResourceLocator;

/**
 * Application class for the webapp. It manages some configuration for the wicket framework. It also defines the home page of the application.
 *
 * @author Patrick Groß-Holtwick
 *         Date: 31.07.13
 */
public class UrlaubrApplication extends WebApplication {

    @Override
    protected void init() {
        super.init();
        getJavaScriptLibrarySettings().setJQueryReference(new PackageResourceReference(ImportResourceLocator.class, "js/demos/js/jquery.js"));
        if (RuntimeConfigurationType.DEVELOPMENT.equals(getConfigurationType())) {
            getDebugSettings().setDevelopmentUtilitiesEnabled(true);
            getDebugSettings().setAjaxDebugModeEnabled(true);
            getDebugSettings().setOutputComponentPath(true);
            String resourceFolder = System.getProperty("application.resource.src");
            String webResourceFolder = System.getProperty("application.webresource.src");
            if (!Strings.isEmpty(resourceFolder)) {
                getResourceSettings().getResourceFinders().add(0, new Path(resourceFolder));
            }
            if (!Strings.isEmpty(webResourceFolder)) {
                getResourceSettings().getResourceFinders().add(0, new Path(webResourceFolder));
            }
        }
        else {
            getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new LastModifiedResourceVersion()));
            getResourceSettings().setJavaScriptCompressor(new NoOpTextCompressor());
            getMarkupSettings().setStripComments(true);
            getMarkupSettings().setCompressWhitespace(true);
            getMarkupSettings().setStripWicketTags(true);
        }
        getMarkupSettings().setDefaultMarkupEncoding("utf-8");

        mountResources();
    }

    /**
     * returns the Homepage of the Application which will be displayed at the start (http://localhost:8080/)
     *
     * @return the class of the start page
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    /**
     * mounts pages and resources to specific paths, e.g. the homepage can be reached with http://localhost:8080/startseite.html
     */
    private void mountResources() {
        mountPage("meinereisen.html", MyVacationPage.class);
        mountPage("startseite.html", HomePage.class);
        mountPage("login.html", LoginPage.class);
        mountPage("reisedetails.html", DetailPage.class);
        mountPage("suche.html", SearchPage.class);
        mountPage("buchungsdetails.html", BookingDetailPage.class);
        mountPage("buchen.html", BookingPage.class);
        mountPage("e-ticket.html", OnlineTicketPage.class);

        mountResource("apple-touch-icon57.png", new PackageResourceReference(ImportResourceLocator.class, "images/apple-touch-icon.png"));
        mountResource("apple-startup-retina.png", new PackageResourceReference(ImportResourceLocator.class, "images/startup-image-retina.png"));
        mountResource("favicon.ico", new PackageResourceReference(ImportResourceLocator.class, "images/favicon.ico"));
    }
}
