package de.urlaubr.ws.client;

import de.urlaubr.ws.domain.Vacation;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;

import javax.xml.namespace.QName;

public class TravelClient {

   public static void main(String[] args1) throws AxisFault {

      ServiceClient sender = new ServiceClient();
      Options options = sender.getOptions();
      EndpointReference targetEPR = new EndpointReference(
       "http://localhost:8080/axis2/services/fhdw-travelservice-ws-1.0");
      options.setTo(targetEPR);

      // Die Operation "findHotel" soll aufgerufen werden
      QName opFindHotel = new QName("http://ws.urlaubr.de/xsd",
                                    "getTopseller");

      // Die Parameter f�r die Operation "findHotel"
      // werden definiert...
      String hotelCode = "AX050";
      Object[] opArgs = new Object[] { hotelCode };

      // ...und ein AXIOM-OMElement mit der 
      //    Request-Nachricht erzeugt
      OMElement request = BeanUtil.getOMElement(opFindHotel,
                                    null, null, false, null);

      // Der Request wird an den Service abgeschickt. 
      // Der Aufruf erfolgt synchron mit dem 
      // Kommunikationsmuster IN-OUT
      OMElement response = sender.sendReceive(request);

      // Diese Typen sollte der Web Service zur�ckliefern...
      Class[] returnTypes = new Class[] { Vacation.class };

      // ...und werden mit einer Hilfsroutine in ein 
      // Objekt-Array �berf�hrt
      Object[] result = BeanUtil.deserialize(response,
                    returnTypes,	new DefaultObjectSupplier());
       System.out.println(result);
   }
}
