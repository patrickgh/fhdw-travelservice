package de.fhdw.travelservice.ws;

import org.testng.annotations.Test;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 30.07.13
 */
public class TravelServiceTest {

    @Test
    public void testLoginAndSession() {
        final TravelService service = new TravelService();
        final String sessionKey = service.login("patrickgh","test");



    }
}
