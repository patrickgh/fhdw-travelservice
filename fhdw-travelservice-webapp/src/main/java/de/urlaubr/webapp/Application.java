package de.urlaubr.webapp;

import de.urlaubr.webapp.page.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 31.07.13
 */
public class Application extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
}
