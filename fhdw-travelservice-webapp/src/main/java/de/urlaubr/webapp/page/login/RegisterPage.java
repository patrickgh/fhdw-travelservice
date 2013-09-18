package de.urlaubr.webapp.page.login;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.page.BasePage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

/**
 * Register page.
 * it displays a form which asks for the user information. if submitted the user gets automatically logged in and redirected to his my-vacation page.
 * @author Patrick Gross-Holtwick
 *         Date: 08.09.13
 */
public class RegisterPage extends BasePage {

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Form form = new Form("form");
        form.add(new Image("logo", new PackageResourceReference(ImportResourceLocator.class, "images/urlaubr.png")));
        final TextField<String> firstname = new TextField<String>("firstname", new Model<String>());
        form.add(firstname);
        final TextField<String> lastname = new TextField<String>("lastname", new Model<String>());
        form.add(lastname);
        final TextField<String> username = new TextField<String>("username", new Model<String>());
        form.add(username);
        final PasswordTextField password = new PasswordTextField("password", new Model<String>());
        form.add(password);
        final EmailTextField email = new EmailTextField("email", new Model<String>());
        form.add(email);

        form.add(new SubmitLink("submit") {
            @Override
            public void onSubmit() {
                super.onSubmit();
                Client.registerUser(firstname.getModelObject(), lastname.getModelObject(), username.getModelObject(), email.getModelObject(), password.getModelObject());
                Integer sessionKey = Client.login(username.getModelObject(), password.getModelObject());
                if (sessionKey != null) {
                    getSession().setAttribute("sessionKey", sessionKey);
                    if (getSession().isTemporary()) { getSession().bind(); }
                    setResponsePage(MyVacationPage.class);
                }
            }
        });
        add(form);
    }
}
