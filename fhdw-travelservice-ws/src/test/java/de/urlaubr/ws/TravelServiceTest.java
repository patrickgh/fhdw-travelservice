package de.urlaubr.ws;

import de.urlaubr.ws.domain.Vacation;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 30.07.13
 */
public class TravelServiceTest {

    @Test
    public void testLoginAndSession() {
        final TravelServiceImpl service = new TravelServiceImpl();
        final Integer sessionKey = service.login("patrickgh", "test");

        Assert.assertNotNull(sessionKey);
        Assert.assertTrue(sessionKey != 0);

        Assert.assertTrue(service.isAuthenticated(sessionKey));

        service.logout(sessionKey);

        Assert.assertFalse(service.isAuthenticated(sessionKey));

    }

    @Test
    public void testGetVacationById() {
        final TravelServiceImpl service = new TravelServiceImpl();

        Vacation vac = service.getVacationById(1);
        Assert.assertNotNull(vac);
        Assert.assertEquals(vac.getTitle(), "mallorca");
        Assert.assertEquals(vac.getPrice(), 299.0);
    }

    @Test
    public void testGetTopseller() {
        final TravelServiceImpl service = new TravelServiceImpl();

        List<Vacation> result = service.getTopseller();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getTitle(), "mallorca");
    }
}
