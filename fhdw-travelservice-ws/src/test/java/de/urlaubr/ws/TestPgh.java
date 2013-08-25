package de.urlaubr.ws;

import de.urlaubr.ws.domain.Booking;
import de.urlaubr.ws.domain.Vacation;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;

import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * @author Patrick Groß-Holtwick
 *         Date: 07.08.13
 */
public class TestPgh {

    public static void main(String[] args) {
        //testQrCode(args);
        try {
            testWSCall(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*test qr code generation*/
    public static void testQrCode(String[] args) {
        ByteArrayOutputStream out = QRCode.from("passenger:Patrick Groß-Holtwick;origin:BER;destination:PMI;flightdate:"+new Date().toString())
                                          .to(ImageType.PNG).withSize(300,300).stream();

        try {
            FileOutputStream fout = new FileOutputStream(new File(
                "/Users/pgh/Desktop/sample.png"));

            fout.write(out.toByteArray());

            fout.flush();
            fout.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testWSCall(String[] args1) throws AxisFault {

        ServiceClient sender = new ServiceClient();
        Options options = sender.getOptions();
        EndpointReference targetEPR = new EndpointReference(
            "http://localhost:8080/axis2/services/fhdw-travelservice-ws-1.0");
        options.setTo(targetEPR);

        // Die Operation "findHotel" soll aufgerufen werden
        QName opFindHotel = new QName("http://ws.urlaubr.de",
                                      "getTopseller");

        // Die Parameter für die Operation
        // werden definiert...

        Object[] opArgs = new Object[]{};

        // ...und ein AXIOM-OMElement mit der
        //    Request-Nachricht erzeugt
        OMElement request = BeanUtil.getOMElement(opFindHotel,
                                                  opArgs, null, false, null);

        // Der Request wird an den Service abgeschickt.
        // Der Aufruf erfolgt synchron mit dem
        // Kommunikationsmuster IN-OUT
        OMElement response = sender.sendReceive(request);

        // Diese Typen sollte der Web Service zur�ckliefern...
        Class[] returnTypes = new Class[]{Vacation.class};

        // ...und werden mit einer Hilfsroutine in ein
        // Objekt-Array �berf�hrt
        Object[] result = BeanUtil.deserialize(response,
                                               returnTypes, new DefaultObjectSupplier());
        System.out.println(result);
    }
}
