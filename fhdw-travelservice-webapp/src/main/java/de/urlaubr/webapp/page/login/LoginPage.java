package de.urlaubr.webapp.page.login;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.page.BasePage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

/**
 * Login Page.
 * It displays the login form and a button to the register page.
 * @author Patrick Gross-Holtwick
 *         Date: 28.08.13
 */
public class LoginPage extends BasePage {

    public LoginPage() {
        super();

        final TextField<String> userField = new TextField<String>("username", new Model<String>());
        userField.setRequired(true);
        final PasswordTextField passwordField = new PasswordTextField("password", new Model<String>());
        passwordField.setRequired(true);

        final Form loginForm = new Form("form");
        loginForm.add(new Image("logo", new PackageResourceReference(ImportResourceLocator.class, "images/urlaubr.png")));
        loginForm.add(userField);
        loginForm.add(passwordField);
        loginForm.add(new SubmitLink("submit") {
            @Override
            public void onSubmit() {
                super.onSubmit();
                String user = userField.getModelObject();
                String password = passwordField.getModelObject();
                Integer sessionKey = Client.login(user, password);
                if (sessionKey != null) {
                    getSession().setAttribute("sessionKey", sessionKey);
                    if (getSession().isTemporary()) { getSession().bind(); }
                    continueToOriginalDestination();
                    //continueToOriginalDestionation: the user gets redirected to his original destination
                    setResponsePage(MyVacationPage.class);
                    //if no original destionation available (for example when the login page gets called directly) he gets redirected to the my vacation page.
                }
            }
        });
        add(loginForm);
    }
}
