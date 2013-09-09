package de.urlaubr.ws.utils;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationInfo;
import de.urlaubr.ws.domain.BookingState;
import de.urlaubr.ws.domain.CateringType;
import org.joda.time.DateTime;

import java.nio.charset.Charset;
import java.util.Date;

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

    /*
     * This methods updates the Database schema if necessary. It uses the Flyway framework ( http://www.flywaydb.org ) and the SQL-Scripts located in the resource folder.
     *
     * @param url the database url
     * @param user the database username
     * @param password the database password
     */
    public static void migrateDatabase(String url, String user, String password) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(url, user, password);
        flyway.migrate();
        MigrationInfo[] migrationInfos = flyway.info().all();
        final StringBuilder builder = new StringBuilder("migration status:\n");
        builder.append("--------------------------------------------------------------------------------------------- \n");
        builder.append(String.format("| %12s | %8s | %20s | %-40s | \n", "version", "state", "installed", "description"));
        builder.append("--------------------------------------------------------------------------------------------- \n");
        for (MigrationInfo patch : migrationInfos) {
            builder.append(
                String.format("| %12s | %8s | %20s | %-40s | \n",
                              patch.getVersion(),
                              patch.getState().getDisplayName(),
                              patch.getInstalledOn(),
                              patch.getDescription()));
        }
        builder.append("--------------------------------------------------------------------------------------------- ");
        System.out.println(builder.toString());
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

    /**
     * This Method returns the enum value from a given integer value.
     * If there is no suitable enum value it returns null.
     *
     * @param value the integer value
     * @return the enum value
     */
    public static CateringType getCateringTypeFromInteger(Integer value) {
        if (value != null) {
            CateringType[] states = CateringType.values();
            if (value < states.length) {
                return states[value];
            }
        }
        return null;
    }

    /**
     * This Method returns the enum value from a given integer value.
     * If there is no suitable enum value it returns null.
     *
     * @param value the integer value
     * @return the enum value
     */
    public static BookingState getBookingStateFromInteger(Integer value) {
        if (value != null) {
            BookingState[] states = BookingState.values();
            if (value < states.length) {
                return states[value];
            }
        }
        return null;
    }

    /**
     * checks if the given date is in the range, but does only compare months and days
     * @param date the reference date
     * @param start the start date
     * @param end the end date
     * @return true if it is in the range, false if not
     */
    public static boolean checkDateRange(Date date, Date start, Date end) {
        DateTime referenceDate = new DateTime(date);
        DateTime startDate = new DateTime(start).withYear(referenceDate.getYear());
        DateTime endDate = new DateTime(end).withYear(referenceDate.getYear());
        if(startDate.isAfter(endDate)) {
            return referenceDate.isAfter(endDate) && referenceDate.isBefore(startDate.plusYears(1));
        } else {
            return referenceDate.isAfter(startDate) && referenceDate.isBefore(endDate);
        }
    }
}

