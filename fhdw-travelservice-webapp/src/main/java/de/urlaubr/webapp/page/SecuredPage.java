package de.urlaubr.webapp.page;

import de.urlaubr.webapp.page.login.LoginPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;

/**
 * Abstract page which requires a login. Every page which extends this page is only accessible when the user is logged in. If the user is not logged in he gets redirected to the login page.
 *
 * @author Patrick Groß-Holtwick
 *         Date: 21.08.13
 */
public abstract class SecuredPage extends BasePage {

    public SecuredPage() {
        super();
        if (getSession().getAttribute("sessionKey") == null) {
            throw new RestartResponseAtInterceptPageException(LoginPage.class);
        }
    }

    protected Integer getSessionKey() {
        return (Integer) getSession().getAttribute("sessionKey");
    }
}
