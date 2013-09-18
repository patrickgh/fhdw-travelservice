package de.urlaubr.webapp;

import de.urlaubr.webapp.page.SecuredPage;
import de.urlaubr.webapp.page.booking.BookingPage;
import de.urlaubr.webapp.page.detail.DetailPage;
import de.urlaubr.webapp.page.login.LoginPage;
import de.urlaubr.webapp.page.myvacation.MyVacationPage;
import de.urlaubr.webapp.page.myvacation.detail.BookingDetailPage;
import de.urlaubr.webapp.page.myvacation.rating.RatingPage;
import de.urlaubr.webapp.page.myvacation.tickets.OnlineTicketPage;
import de.urlaubr.webapp.page.search.SearchPage;
import de.urlaubr.webapp.page.start.HomePage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.tester.WicketTester;
import org.testng.annotations.Test;

/**
 * Unit-test class for the webapp.
 * @author Patrick Gross-Holtwick
 *         Date: 31.07.13
 */
public class WicketApplicationTest {
    private WicketTester tester;

    /**
     * constructor which creates a Wicket-Tester instance with the Application loaded
     */
    public WicketApplicationTest() {
        tester = new WicketTester(new UrlaubrApplication());
    }

    /**
     * simple test which loads every page, checks if there were no errors and validates the rendered page.
     */
    @Test
    public void testApplication() {
        testPage(HomePage.class);
        testPage(SearchPage.class);
        testPage(MyVacationPage.class);
        testPage(BookingDetailPage.class);
        testPage(RatingPage.class);
        testPage(OnlineTicketPage.class);
        testPage(BookingPage.class);
    }

    /**
     * Convenience method which simplifies the webpage test
     * @param page which should be tested
     */
    private void testPage(Class<? extends WebPage> page) {
        tester.startPage(page);
        tester.assertNoErrorMessage();
        if(SecuredPage.class.isAssignableFrom(page)) {
            tester.assertRenderedPage(LoginPage.class); //if the page is secured, we expect to get to the login page
        } else {
            tester.assertRenderedPage(page);
        }
    }
}
