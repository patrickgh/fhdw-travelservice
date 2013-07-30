package de.fhdw.travelservice.ws.utils;

import java.nio.charset.Charset;

/**
 * This class provides static help-methods for the webservice
 *
 * @author Patrick Groß-Holtwick
 *         Date: 30.07.13
 */
public final class UrlaubrWsUtils {

    /**
     * private constructor, because every method is static
     */
    private UrlaubrWsUtils() {
    }

    /**
     * This Method generates a md5 hash of a given String.
     *
     * @param input String that should get hashed
     * @return md5 String
     */
    public static String md5(String input) {
        String md5 = (input == null) ? "" : input;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes(Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        }
        catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException while creating MD5 hash");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is similar to Integer.valueOf(), but instead of throwing an NumberFormatException it returns null
     * if there is an error.
     *
     * @param number the string which contains the number
     * @return the integer value (or null)
     */
    public static Integer parseInt(String number) {
        try {
            return Integer.valueOf(number);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
}

