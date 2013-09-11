package de.urlaubr.webapp.page;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.page.login.LoginPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;

/**
 * Abstract page which requires a login.
 * Every page which extends this page is only accessible when the user is logged in.
 * If the user is not logged in he gets redirected to the login page.
 *
 * @author Patrick Groß-Holtwick
 *         Date: 21.08.13
 */
public abstract class SecuredPage extends BasePage {

    public SecuredPage() {
        super();
        if (getSession().getAttribute("sessionKey") == null || Client.getUserInfo(getSessionKey()) == null) {
            getSession().removeAttribute("sessionKey");
            throw new RestartResponseAtInterceptPageException(LoginPage.class);
            //RestartResponseAtInterceptPageException: redirects to another page (login page) but remembers original request target.
        }
    }

    /**
     * convenience method for retrieving the session key.
     * @return the sessionKey which authenticates the user on the webservice
     */
    protected Integer getSessionKey() {
        return (Integer) getSession().getAttribute("sessionKey");
    }
}
