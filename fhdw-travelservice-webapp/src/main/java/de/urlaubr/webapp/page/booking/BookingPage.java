package de.urlaubr.webapp.page.booking;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.components.listeditor.ListEditor;
import de.urlaubr.webapp.components.listeditor.ListItem;
import de.urlaubr.webapp.components.panel.StarRatingPanel;
import de.urlaubr.webapp.page.SecuredPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.ws.domain.Customer;
import de.urlaubr.ws.domain.Traveler;
import de.urlaubr.ws.domain.Vacation;
import de.urlaubr.ws.utils.UrlaubrWsUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 05.09.13
 */
public class BookingPage extends SecuredPage {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (getPageParameters().get("id") != null && getPageParameters().get("id").toInt(-1) != -1) {
            final Integer id = getPageParameters().get("id").toInt();
            final CompoundPropertyModel<Vacation> model = new CompoundPropertyModel<Vacation>(Client.getVactationById(id));
            final String resourceKey = "catering." + UrlaubrWsUtils.getCateringTypeFromInteger(model.getObject().getCatering()).name().toLowerCase();
            add(new ByteArrayImage("image", model.<byte[]>bind("image")));
            add(new Label("title", model.<String>bind("title")));
            add(new Label("city", model.<String>bind("city")));
            add(new Label("country", model.<String>bind("country")));
            add(new Label("hotelstars", model.<String>bind("hotelstars")));
            add(new Label("catering", new ResourceModel(resourceKey, resourceKey)));
            add(new StarRatingPanel("starrating", new AbstractReadOnlyModel<Integer>() {
                @Override
                public Integer getObject() {
                    return Long.valueOf(Math.round(model.getObject().getAvgRating())).intValue();
                }
            }));
            add(new Label("price", model.<Double>bind("price")));

            final Form bookingForm = new Form("bookingForm");
            final DateTextField startDateField = new DateTextField("startdate", new Model<Date>(), DATE_FORMAT) {
                @Override
                protected String getInputType() {
                    return "date";
                }
            };
            startDateField.add(new AttributeModifier("min", new SimpleDateFormat(DATE_FORMAT).format(new Date())));
            startDateField.setRequired(true);
            bookingForm.add(startDateField);

            List<Traveler> travelerList = new ArrayList<Traveler>();
            Traveler defaultTraveler = new Traveler();
            Customer me = Client.getUserInfo(getSessionKey());
            defaultTraveler.setFirstname(me.getFirstname());
            defaultTraveler.setLastname(me.getLastname());
            travelerList.add(defaultTraveler);
            final ListEditor<Traveler> travelerListEditor = new ListEditor<Traveler>("traveler", new ListModel<Traveler>(travelerList)) {
                @Override
                protected void onPopulateItem(ListItem<Traveler> item) {
                    CompoundPropertyModel<Traveler> model = new CompoundPropertyModel<Traveler>(item.getModel());
                    TextField<String> firstname = new TextField<String>("firstname", model.<String>bind("firstname"));
                    firstname.setRequired(true);
                    item.add(firstname);
                    TextField<String> lastname = new TextField<String>("lastname", model.<String>bind("lastname"));
                    lastname.setRequired(true);
                    item.add(lastname);
                    TextField<Date> birthdate = new DateTextField("birthdate", model.<Date>bind("birthday"), DATE_FORMAT) {
                        @Override
                        protected String getInputType() {
                            return "date";
                        }
                    };
                    birthdate.add(new AttributeModifier("max", new SimpleDateFormat(DATE_FORMAT).format(new Date())));
                    birthdate.setRequired(true);
                    item.add(birthdate);
                    item.add(new TextField<String>("passport", model.<String>bind("passport")));
                }
            };
            bookingForm.add(new SubmitLink("travelerAdd") {
                @Override
                public void onSubmit() {
                    super.onSubmit();
                    travelerListEditor.addItem(new Traveler());
                    bookingForm.addOrReplace(new Label("fullprice", new AbstractReadOnlyModel<String>() {
                        @Override
                        public String getObject() {
                            return (travelerListEditor.getModelObject().size()+1) * model.getObject().getPrice() + "";
                        }
                    }));
                }
            });
            bookingForm.add(travelerListEditor);
            bookingForm.add(new Label("fullprice", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return travelerListEditor.getModelObject().size() * model.getObject().getPrice() + "";
                }
            }));
            bookingForm.add(new SubmitLink("submit") {
                @Override
                public void onSubmit() {
                    super.onSubmit();
                    List<Traveler> traveler = travelerListEditor.getModelObject();
                    Date startDate = startDateField.getModelObject();
                    Client.createBooking(getSessionKey(), id, startDate, traveler);
                    setResponsePage(MyVacationPage.class);
                }
            });
            add(bookingForm);
        }
        else {
            setResponsePage(getApplication().getHomePage());
        }
    }
}
