package de.urlaubr.ws;

import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 30.07.13
 */
public class TravelServiceTest {

    @Test
    public void testLoginAndSession() {
        final TravelServiceImpl service = new TravelServiceImpl();
        final Integer sessionKey = service.login("patrickgh","test");

        Assert.assertNotNull(sessionKey);
        Assert.assertTrue(sessionKey != 0);

        Assert.assertTrue(service.isAuthenticated(sessionKey));

        service.logout(sessionKey);

        Assert.assertFalse(service.isAuthenticated(sessionKey));


    }
}
