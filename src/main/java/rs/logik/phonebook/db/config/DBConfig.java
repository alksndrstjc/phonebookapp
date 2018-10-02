
package rs.logik.phonebook.db.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConfig {
    
    private Properties info = new Properties();
    private InputStream input = null;
    
    public DBConfig() {
        try {
            input = DBConfig.class.getClassLoader().getResourceAsStream("dbconfig.properties");
            info.load(input);
        } catch (IOException ex) {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getJDBCDriver() {
        return info.getProperty("jdbc.driver");
    }
    
    public String getJDBCUrl() {
        return info.getProperty("jdbc.url");
    }
    
    public String getJDBCUser() {
        return info.getProperty("jdbc.user");
    }
    
    public String getJDBCPassword() {
        return info.getProperty("jdbc.password");
    }
}
