
package rs.logik.phonebook.statichelpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HelperResourceCloser {
    
    
    public static void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(HelperResourceCloser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(ps != null ){
            try {
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(HelperResourceCloser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(HelperResourceCloser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
