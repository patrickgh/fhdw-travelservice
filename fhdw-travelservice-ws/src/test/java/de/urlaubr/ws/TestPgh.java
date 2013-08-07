package de.urlaubr.ws;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

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

    /*test qr code generation*/
    public static void main(String[] args) {
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
}
