package de.urlaubr.ws;

import de.urlaubr.ws.client.TravelClient;
import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 * This class contains unit tests for the webservice client.
 * It requires a deployed axis2 webservice (and is therefore not included in the maven build process).
 *
 * @author Patrick Groß-Holtwick
 *         Date: 21.08.13
 */

public class TravelServiceClientTest {

    private TravelClient client = new TravelClient();

    @Test
    public void testLogin() throws Exception {
        Integer sessionKey = client.login("patrickgh", "test");

        Assert.assertNotNull(sessionKey);

        client.logout(sessionKey);
    }
}
