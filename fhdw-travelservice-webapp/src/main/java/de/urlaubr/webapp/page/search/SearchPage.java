package de.urlaubr.webapp.page.search;

import de.urlaubr.webapp.Client;
import de.urlaubr.webapp.components.ByteArrayImage;
import de.urlaubr.webapp.components.panel.StarRatingPanel;
import de.urlaubr.webapp.page.BasePage;
import de.urlaubr.webapp.page.detail.DetailPage;
import de.urlaubr.ws.domain.CateringType;
import de.urlaubr.ws.domain.SearchParams;
import de.urlaubr.ws.domain.Vacation;
import de.urlaubr.ws.utils.UrlaubrWsUtils;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import webresources.ImportResourceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * the search page.
 * it contains the form which provides the ability to search for specific vacations.
 * @author Patrick Gross-Holtwick
 *         Date: 31.08.13
 */
public class SearchPage extends BasePage {

    private WebMarkupContainer resultContainer;
    private WebMarkupContainer formContainer;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final IModel<List<Vacation>> model = new ListModel<Vacation>(new ArrayList<Vacation>());

        resultContainer = new WebMarkupContainer("result");

        final ListView<Vacation> resultList = new ListView<Vacation>("topsellerList", model) {
            @Override
            protected void populateItem(ListItem<Vacation> item) {
                final CompoundPropertyModel<Vacation> model = new CompoundPropertyModel<Vacation>(item.getModel());
                PageParameters parameters = new PageParameters();
                parameters.add("id", model.getObject().getId());
                final BookmarkablePageLink link = new BookmarkablePageLink("topsellerLink", DetailPage.class, parameters);
                link.add(new Label("title", model.<String>bind("title")));
                link.add(new Label("price", model.<String>bind("price")));
                link.add(new ByteArrayImage("image", model.<byte[]>bind("image")));
                link.add(new StarRatingPanel("starrating", new AbstractReadOnlyModel<Integer>() {
                    @Override
                    public Integer getObject() {
                        return Long.valueOf(Math.round(model.getObject().getAvgRating())).intValue();
                    }
                }));
                item.add(link);
            }
        };
        resultContainer.setVisible(false);
        resultContainer.add(resultList);
        add(resultContainer);

        Form form = new Form<SearchParams>("form");
        form.add(new Image("logo", new PackageResourceReference(ImportResourceLocator.class, "images/urlaubr.png")));

        formContainer = new WebMarkupContainer("formContainer");
        formContainer.setOutputMarkupId(true);

        final TextField<String> titleField = new TextField<String>("title", new Model<String>(""));
        formContainer.add(titleField);

        List<String> selected = new ArrayList<String>();
        selected.add("none");
        final Select<List<String>> countrySelect = new Select<List<String>>("country", new ListModel<String>(new ArrayList<String>(selected)));
        countrySelect.add(new SelectOption<String>("countryNone", new Model<String>("none")));
        countrySelect.add(new SelectOption<String>("GER", new Model<String>("GER")));
        countrySelect.add(new SelectOption<String>("NED", new Model<String>("NED")));
        countrySelect.add(new SelectOption<String>("ENG", new Model<String>("ENG")));
        countrySelect.add(new SelectOption<String>("ESP", new Model<String>("ESP")));
        countrySelect.add(new SelectOption<String>("FRANCE", new Model<String>("FRA")));
        countrySelect.add(new SelectOption<String>("ITA", new Model<String>("ITA")));
        countrySelect.add(new SelectOption<String>("POR", new Model<String>("POR")));
        countrySelect.add(new SelectOption<String>("EGY", new Model<String>("EGY")));
        countrySelect.add(new SelectOption<String>("USA", new Model<String>("USA")));
        countrySelect.add(new SelectOption<String>("MEX", new Model<String>("MEX")));
        formContainer.add(countrySelect);

        final Select<List<String>> airportSelect = new Select<List<String>>("airport", new ListModel<String>(new ArrayList<String>(selected)));
        airportSelect.add(new SelectOption<String>("airportNone", new Model<String>("none")));
        airportSelect.add(new SelectOption<String>("PAD", new Model<String>("PAD")));
        airportSelect.add(new SelectOption<String>("DUS", new Model<String>("DUS")));
        airportSelect.add(new SelectOption<String>("DTM", new Model<String>("DTM")));
        airportSelect.add(new SelectOption<String>("CGN", new Model<String>("CGN")));
        airportSelect.add(new SelectOption<String>("MUC", new Model<String>("MUC")));
        airportSelect.add(new SelectOption<String>("STR", new Model<String>("STR")));
        airportSelect.add(new SelectOption<String>("FRA", new Model<String>("FRA")));
        airportSelect.add(new SelectOption<String>("TXL", new Model<String>("TXL")));
        airportSelect.add(new SelectOption<String>("SXF", new Model<String>("SXF")));
        airportSelect.add(new SelectOption<String>("BRE", new Model<String>("BRE")));
        airportSelect.add(new SelectOption<String>("HAM", new Model<String>("HAM")));
        formContainer.add(airportSelect);

        final Select<Integer> starSelect = new Select<Integer>("stars", new Model<Integer>(-1));
        starSelect.add(new SelectOption<Integer>("starsNone", new Model<Integer>(-1)));
        starSelect.add(new SelectOption<Integer>("one", new Model<Integer>(1)));
        starSelect.add(new SelectOption<Integer>("two", new Model<Integer>(2)));
        starSelect.add(new SelectOption<Integer>("three", new Model<Integer>(3)));
        starSelect.add(new SelectOption<Integer>("four", new Model<Integer>(4)));
        starSelect.add(new SelectOption<Integer>("five", new Model<Integer>(5)));
        formContainer.add(starSelect);

        final Select<Integer> cateringSelect = new Select<Integer>("catering", new Model<Integer>(-1));
        cateringSelect.add(new SelectOption<Integer>("cateringNone", new Model<Integer>(-1)));
        cateringSelect.add(new SelectOption<Integer>("overnight", new Model<Integer>(CateringType.NONE.ordinal())));
        cateringSelect.add(new SelectOption<Integer>("breakfast", new Model<Integer>(CateringType.BREAKFAST.ordinal())));
        cateringSelect.add(new SelectOption<Integer>("halfboard", new Model<Integer>(CateringType.HALF_BOARD.ordinal())));
        cateringSelect.add(new SelectOption<Integer>("fullboard", new Model<Integer>(CateringType.FULL_BOARD.ordinal())));
        cateringSelect.add(new SelectOption<Integer>("allinclusive", new Model<Integer>(CateringType.ALL_INCLUSIVE.ordinal())));
        formContainer.add(cateringSelect);

        final Select<Integer> durationSelect = new Select<Integer>("duration", new Model<Integer>(-1));
        durationSelect.add(new SelectOption<Integer>("durationNone", new Model<Integer>(-1)));
        durationSelect.add(new SelectOption<Integer>("2days", new Model<Integer>(2)));
        durationSelect.add(new SelectOption<Integer>("3days", new Model<Integer>(3)));
        durationSelect.add(new SelectOption<Integer>("5days", new Model<Integer>(5)));
        durationSelect.add(new SelectOption<Integer>("1week", new Model<Integer>(7)));
        durationSelect.add(new SelectOption<Integer>("2week", new Model<Integer>(14)));
        formContainer.add(durationSelect);
        form.add(formContainer);
        form.add(new SubmitLink("submit") {
            @Override
            public void onSubmit() {
                super.onSubmit();
                if (formContainer.isVisible()) {
                    final SearchParams params = new SearchParams();
                    if (countrySelect.getModelObject() != null && countrySelect.getModelObject().size() != 0 && !countrySelect.getModelObject().contains("none")) {
                        params.setCountry(countrySelect.getModelObject().toArray(new String[countrySelect.getModelObject().size()]));
                    }

                    if (airportSelect.getModelObject() != null && airportSelect.getModelObject().size() != 0 && !airportSelect.getModelObject().contains("none")) {
                        params.setHomeairport(airportSelect.getModelObject().toArray(new String[airportSelect.getModelObject().size()]));
                    }

                    if (starSelect.getModelObject() != null && starSelect.getModelObject() != -1) {
                        params.setHotelstars(starSelect.getModelObject());
                    }

                    if (cateringSelect.getModelObject() != null && cateringSelect.getModelObject() != -1) {
                        params.setCatering(UrlaubrWsUtils.getCateringTypeFromInteger(cateringSelect.getModelObject()));
                    }

                    if (durationSelect.getModelObject() != null && durationSelect.getModelObject() != -1) {
                        params.setDuration(durationSelect.getModelObject());
                    }

                    if (titleField.getModelObject() != null && titleField.getModelObject().length() > 0) {
                        params.setTitle(titleField.getModelObject());
                    }

                    model.setObject(Client.findVacations(params));
                    resultList.setModel(model);
                }
                formContainer.setVisible(!formContainer.isVisible());
                resultContainer.setVisible(!resultContainer.isVisible());
            }
        });
        add(form);
    }
}
