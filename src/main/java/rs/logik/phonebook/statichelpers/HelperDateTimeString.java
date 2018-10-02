
package rs.logik.phonebook.statichelpers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class HelperDateTimeString {
    
    public static String getStringFromDateTime(DateTime dt) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.print(dt);
    }
    
    public static DateTime getDateTimeFromString(String s) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.parseDateTime(s);
    }
}
