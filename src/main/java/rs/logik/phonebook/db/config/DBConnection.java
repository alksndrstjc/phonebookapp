
package rs.logik.phonebook.db.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConnection {
    
    private static final DBConfig conn = new DBConfig();
    
    public static final Connection getConnection() {
        try {
            return DriverManager.getConnection(conn.getJDBCUrl(), conn.getJDBCUser(), conn.getJDBCPassword());
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } 
}
