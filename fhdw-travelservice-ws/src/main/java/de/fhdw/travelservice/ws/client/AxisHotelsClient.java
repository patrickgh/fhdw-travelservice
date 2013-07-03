package de.fhdw.travelservice.ws.client;

import de.fhdw.travelservice.ws.Hotel;
import de.fhdw.travelservice.ws.RoomType;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;

import javax.xml.namespace.QName;

public class AxisHotelsClient {

   public static void main(String[] args1) throws AxisFault {

      ServiceClient sender = new ServiceClient();
      Options options = sender.getOptions();
      EndpointReference targetEPR = new EndpointReference(
       "http://localhost:8080/axis2/services/SimpleHotelService");
      options.setTo(targetEPR);

      // Die Operation "findHotel" soll aufgerufen werden
      QName opFindHotel = new QName("http://axishotels.de/xsd",
                                    "findHotel");

      // Die Parameter f�r die Operation "findHotel"
      // werden definiert...
      String hotelCode = "AX050";
      Object[] opArgs = new Object[] { hotelCode };

      // ...und ein AXIOM-OMElement mit der 
      //    Request-Nachricht erzeugt
      OMElement request = BeanUtil.getOMElement(opFindHotel,
                                    opArgs, null, false, null);

      // Der Request wird an den Service abgeschickt. 
      // Der Aufruf erfolgt synchron mit dem 
      // Kommunikationsmuster IN-OUT
      OMElement response = sender.sendReceive(request);

      // Diese Typen sollte der Web Service zur�ckliefern...
      Class[] returnTypes = new Class[] { Hotel.class };

      // ...und werden mit einer Hilfsroutine in ein 
      // Objekt-Array �berf�hrt
      Object[] result = BeanUtil.deserialize(response,
                    returnTypes,	new DefaultObjectSupplier());
     
      Hotel hotel = (Hotel) result[0];

      if (hotel == null) {
         System.out.println("No entry for code: " + hotelCode);
         return;
      }
      
      System.out.println("Hotel Name: " + hotel.getHotelName());
      System.out.println("Hotel Code: " + hotel.getHotelCode());
      System.out.println("City: " + hotel.getCity());
      System.out.println("Stars: " + hotel.getNumberOfStars());

      for (RoomType roomType : hotel.getRoomTypes()) {

         System.out.println("\n RoomCode : " +
                            roomType.getRoomCode());
         System.out.println(" Price EUR: " +  
                            roomType.getPriceInEuros());
         System.out.println(" with TV  : " +
                            roomType.isRoomWithTV());
      }
   }
}
