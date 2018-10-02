package rs.logik.phonebook.statichelpers;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class HelperPasswordHashing {
    
    public static String getMD5HashFromString(String password) {
        return Hashing.md5().hashString(password, StandardCharsets.UTF_8).toString();
    }
    
}
