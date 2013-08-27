package de.urlaubr.webapp.page;

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
            setResponsePage(getApplication().getHomePage());
        }
    }

    protected Integer getSessionKey() {
        return (Integer) getSession().getAttribute("sessionKey");
    }
}
