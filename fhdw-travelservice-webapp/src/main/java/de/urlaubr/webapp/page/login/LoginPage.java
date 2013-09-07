package de.urlaubr.webapp.page.login;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.page.BasePage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 28.08.13
 */
public class LoginPage extends BasePage {

    public LoginPage() {
        super();

        final TextField<String> userField = new TextField<String>("username", new Model<String>());
        userField.setRequired(false);
        final PasswordTextField passwordField = new PasswordTextField("password", new Model<String>());
        passwordField.setRequired(false);

        final Form loginForm = new Form("form");
        loginForm.add(new Image("logo", new PackageResourceReference(ImportResourceLocator.class, "images/urlaubr.png")));
        loginForm.add(userField);
        loginForm.add(passwordField);
        loginForm.add(new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                String user = userField.getModelObject();
                String password = passwordField.getModelObject();
                Integer sessionKey = Client.login(user, password);
                if (sessionKey != null) {
                    getSession().setAttribute("sessionKey", sessionKey);
                    if (getSession().isTemporary()) { getSession().bind(); }
                    continueToOriginalDestination();
                    setResponsePage(MyVacationPage.class);
                }
                else {
                    target.appendJavaScript("shake();");
                }
            }
        });
        add(loginForm);
    }
}
