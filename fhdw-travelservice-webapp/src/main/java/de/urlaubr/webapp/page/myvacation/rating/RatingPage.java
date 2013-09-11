package de.urlaubr.webapp.page.myvacation.rating;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.page.SecuredPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.BookingState;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;

/**
 * this page shows the form which can be used to rate a vacation.
 * It expects an id parameter (booking-id).
 * @author Patrick Groß-Holtwick
 *         Date: 07.09.13
 */
public class RatingPage extends SecuredPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final Integer bookingId;
        if (getPageParameters().get("id") != null && (bookingId = getPageParameters().get("id").toInt(-1)) != -1) {
            Booking booking = Client.getBookingById(getSessionKey(), bookingId);
            if (booking != null && booking.getCustomer() != null && booking.getCustomer().getId().equals(Client.getUserInfo(getSessionKey()).getId()) && booking.getState() != BookingState.FINISHED.ordinal()) {
                final Form form = new Form("form");
                final Select<Integer> starSelect = new Select<Integer>("stars", new Model<Integer>(-1));
                starSelect.add(new SelectOption<Integer>("one", new Model<Integer>(1)));
                starSelect.add(new SelectOption<Integer>("two", new Model<Integer>(2)));
                starSelect.add(new SelectOption<Integer>("three", new Model<Integer>(3)));
                starSelect.add(new SelectOption<Integer>("four", new Model<Integer>(4)));
                starSelect.add(new SelectOption<Integer>("five", new Model<Integer>(5)));
                form.add(starSelect);

                final TextArea<String> comment = new TextArea<String>("comment", new Model<String>());
                form.add(comment);

                form.add(new SubmitLink("submit") {
                    @Override
                    public void onSubmit() {
                        super.onSubmit();
                        Integer rating = starSelect.getModelObject();
                        String text = comment.getModelObject();
                        Client.rateVacation(getSessionKey(), bookingId, rating, text);
                        setResponsePage(MyVacationPage.class);
                    }
                });
                add(form);
            }
            else {
                setResponsePage(MyVacationPage.class);
            }
        }
        else {
            setResponsePage(MyVacationPage.class);
        }
    }
}
