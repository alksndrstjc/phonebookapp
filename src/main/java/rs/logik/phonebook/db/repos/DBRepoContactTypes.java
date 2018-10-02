package rs.logik.phonebook.db.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.logik.phonebook.db.config.DBConnection;
import rs.logik.phonebook.statichelpers.HelperResourceCloser;

// napuni liste
// dodaj novi tip u listu
// obrisi odredjeni iz liste
public class DBRepoContactTypes {

    public static void fillListsWithTypes(ArrayList<String> contactTypes, ArrayList<String> phoneTypes) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql1 = "SELECT * FROM contacttype";
        String sql2 = "SELECT * FROM phonetype";

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql1);
            rs = ps.executeQuery();

            while (rs.next()) {
                contactTypes.add(rs.getString("contacttype"));
            }

            ps = conn.prepareStatement(sql2);
            rs = ps.executeQuery();

            while (rs.next()) {
                phoneTypes.add(rs.getString("phonetype"));
            }
            HelperResourceCloser.closeResources(rs, ps, conn);

        } catch (SQLException ex) {
            Logger.getLogger(DBRepoContactTypes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HelperResourceCloser.closeResources(rs, ps, conn);
        }
    }

    public static void addContactType(String contactType, ArrayList<String> contactTypes) throws IllegalArgumentException {
        if (!isContactTypeInDB(contactType)) {
            Connection conn = null;
            PreparedStatement ps = null;

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement("INSERT INTO contacttype(contacttype) VALUES(?)");
                ps.setString(1, contactType);
                ps.executeUpdate();
                contactTypes.add(contactType);
            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContactTypes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void addPhoneType(String phoneType, ArrayList<String> phoneTypes) throws IllegalArgumentException {
        if (!isPhoneTypeInDB(phoneType)) {
            Connection conn = null;
            PreparedStatement ps = null;

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement("INSERT INTO phonetype(phonetype) VALUES(?)");
                ps.setString(1, phoneType);
                ps.executeUpdate();
                phoneTypes.add(phoneType);
            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContactTypes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void deleteContactType(String contactType, ArrayList<String> contactTypes) {
        if (isContactTypeInDB(contactType)) {
            Connection conn = null;
            PreparedStatement ps = null;
            String sql = "DELETE FROM contacttype WHERE contacttype = ?";

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setString(1, contactType);
                ps.executeUpdate();
                contactTypes.remove(contactType);

                HelperResourceCloser.closeResources(null, ps, conn);
            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContactTypes.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(null, ps, conn);
            }
        }
    }

    public static void deletePhoneType(String phoneType, ArrayList<String> phoneTypes) {
        if (isPhoneTypeInDB(phoneType)) {
            Connection conn = null;
            PreparedStatement ps = null;
            String sql = "DELETE FROM phonetype WHERE contacttype = ?";

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setString(1, phoneType);
                ps.executeUpdate();
                phoneTypes.remove(phoneType);

                HelperResourceCloser.closeResources(null, ps, conn);
            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContactTypes.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(null, ps, conn);
            }
        }
    }

    private static boolean isContactTypeInDB(String contactType) {
        boolean isPresent = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM contacttype WHERE contacttype = ? LIMIT 1";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, contactType);
            rs = ps.executeQuery();
            if (rs.next()) {
                isPresent = true;
            }
            HelperResourceCloser.closeResources(rs, ps, conn);
        } catch (SQLException ex) {
            Logger.getLogger(DBRepoContactTypes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HelperResourceCloser.closeResources(rs, ps, conn);
        }

        return false;
    }

    private static boolean isPhoneTypeInDB(String phoneType) {
        boolean isPresent = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM phonetype WHERE phonetype = ? LIMIT 1";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, phoneType);
            rs = ps.executeQuery();
            if (rs.next()) {
                isPresent = true;
            }
            HelperResourceCloser.closeResources(rs, ps, conn);
        } catch (SQLException ex) {
            Logger.getLogger(DBRepoContactTypes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HelperResourceCloser.closeResources(rs, ps, conn);
        }

        return isPresent;
    }

}
