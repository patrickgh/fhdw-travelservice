package de.urlaubr.ws;

import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.Customer;
import de.urlaubr.ws.domain.SearchParams;
import de.urlaubr.ws.domain.Traveler;
import de.urlaubr.ws.domain.Vacation;
import de.urlaubr.ws.utils.UrlaubrWsUtils;
import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class contains unit tests for the webservice class.
 * It calls the methods directly, therefore no axis2 server is required.
 * It checks the general database handling.
 *
 * @author Patrick Gross-Holtwick
 *         Date: 30.07.13
 */
public class TravelServiceTest {

    protected TravelService service;

    @BeforeTest
    public void setUp() {
        //migrate or create database schema
        UrlaubrWsUtils.migrateDatabase(TravelServiceImpl.DEFAULT_URL, TravelServiceImpl.DEFAULT_USER, TravelServiceImpl.DEFAULT_PASSWORD);

        service = new TravelServiceImpl();
    }

    @Test
    public void testLoginAndSession() {
        try {
            final Integer sessionKey = service.login("patrickgh", "test");

            Assert.assertNotNull(sessionKey);
            Assert.assertTrue(sessionKey != 0);

            Customer me = service.getUserInfo(sessionKey);

            Assert.assertNotNull(me);
            Assert.assertEquals(me.getFirstname(), "Patrick");
            Assert.assertEquals(me.getEmail(), "patrickgh@web.de");

            service.logout(sessionKey);

            Assert.assertNull(service.getUserInfo(sessionKey));
        }
        catch (AxisFault e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetVacationById() {
        try {
            Vacation vac = service.getVacationById(1);
            Assert.assertNotNull(vac);
            Assert.assertEquals(vac.getTitle(), "3 Tage Mallorca");
            Assert.assertEquals(vac.getPrice(), 199.99);
        }
        catch (AxisFault e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetTopseller() {
        try {
            Vacation[] result = service.getTopseller();
            Assert.assertNotNull(result);
            Assert.assertEquals(result.length, 5);
            Assert.assertEquals(result[0].getTitle(), "Wandern in Norwegen");
        }
        catch (AxisFault e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetMyVacations() {
        try {
            final Integer sessionKey = service.login("patrickgh", "test");

            Booking[] bookings = service.getMyVacations(sessionKey);

            Assert.assertNotNull(bookings);
            Assert.assertEquals(bookings.length > 0, true);

        }
        catch (AxisFault e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFindVacations() {
        try {
            Vacation[] vacations = service.findVacations(new SearchParams());
            Assert.assertNotNull(vacations);
            Assert.assertEquals(vacations.length, 11);

            SearchParams params = new SearchParams();
            params.setCountry(new String[]{"ESP"});
            vacations = service.findVacations(params);
            Assert.assertNotNull(vacations);
            Assert.assertEquals(vacations.length, 4);

            params.setTitle("Mallorca");
            vacations = service.findVacations(params);
            Assert.assertNotNull(vacations);
            Assert.assertEquals(vacations.length, 1);
            Assert.assertEquals(vacations[0].getTitle(), "3 Tage Mallorca");
        }
        catch (AxisFault e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateBooking() throws AxisFault {
        try {
            final Integer sessionKey = service.login("patrickgh", "test");

            List<Traveler> travelers = new ArrayList<Traveler>();
            travelers.add(new Traveler());
            travelers.get(0).setFirstname("test");
            travelers.get(0).setLastname("2test");
            travelers.get(0).setPassport("pp");
            travelers.get(0).setBirthday(new Date());
            Integer bookingid = service.createBooking(sessionKey, 1, new DateTime(2017,06,02,0,0).toDate(), travelers.toArray(new Traveler[travelers.size()]));

            Booking[] bookings = service.getMyVacations(sessionKey);

            Assert.assertNotNull(bookings);
            Assert.assertNotNull(bookingid);
            Assert.assertEquals(bookings.length > 0, true);
            Assert.assertEquals(bookings[0].getVacation().getId().intValue(), 1);
            Assert.assertNotNull(bookings[0].getTraveler());
            Assert.assertTrue(bookings[0].getTraveler().length > 0);
        }
        catch (AxisFault e) {
            Assert.fail(e.getMessage());
        }
    }
}
