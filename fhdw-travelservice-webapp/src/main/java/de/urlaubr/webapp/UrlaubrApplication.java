package de.urlaubr.webapp;

import de.urlaubr.webapp.page.detail.DetailPage;
import de.urlaubr.webapp.page.login.LoginPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.webapp.page.search.SearchPage;
import de.urlaubr.webapp.page.start.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.core.util.file.WebApplicationPath;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;
import org.apache.wicket.resource.NoOpTextCompressor;
import org.apache.wicket.settings.def.JavaScriptLibrarySettings;
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
        getJavaScriptLibrarySettings().setJQueryReference(new PackageResourceReference(ImportResourceLocator.class,"js/demos/js/jquery.js"));
        if (RuntimeConfigurationType.DEVELOPMENT.equals(getConfigurationType())) {
            getDebugSettings().setDevelopmentUtilitiesEnabled(true);
            getDebugSettings().setAjaxDebugModeEnabled(true);
            getDebugSettings().setOutputComponentPath(true);
            String resourceFolder = System.getProperty("application.resource.src");
            String webResourceFolder = System.getProperty("application.webresource.src");
            if (!Strings.isEmpty(resourceFolder)) {
                getResourceSettings().getResourceFinders().add(new WebApplicationPath(getServletContext(), resourceFolder));
            }
            if (!Strings.isEmpty(webResourceFolder)) {
                getResourceSettings().getResourceFinders().add(new WebApplicationPath(getServletContext(), webResourceFolder));
            }
        }
        else {
            getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new LastModifiedResourceVersion()));
            getResourceSettings().setJavaScriptCompressor(new NoOpTextCompressor());
            getMarkupSettings().setStripComments(true);
            getMarkupSettings().setCompressWhitespace(true);
        }
        getMarkupSettings().setDefaultMarkupEncoding("utf-8");

        mountPage("meinereisen.html", MyVacationPage.class);
        mountPage("startseite.html", HomePage.class);
        mountPage("login.html", LoginPage.class);
        mountPage("reisedetails.html", DetailPage.class);
        mountPage("suche.html", SearchPage.class);

        mountResource("apple-touch-icon.png", new PackageResourceReference(ImportResourceLocator.class,"images/apple-touch-icon.png"));
        mountResource("favicon.ico", new PackageResourceReference(ImportResourceLocator.class,"images/favicon.ico"));
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
}
