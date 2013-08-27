package de.urlaubr.ws;

import de.urlaubr.ws.client.TravelClient;
import org.testng.annotations.BeforeTest;

/**
 * This class contains unit tests for the webservice client.
 * It uses the same tests as the other class but calls the methods via webservice.
 * It requires a deployed axis2 webservice (and is therefore not included in the maven build process).
 *
 * @author Patrick Groß-Holtwick
 *         Date: 21.08.13
 */

public class TravelServiceClientTest extends TravelServiceTest {

    @BeforeTest
    @Override
    public void setUp() {
        service = new TravelClient();
    }
}
