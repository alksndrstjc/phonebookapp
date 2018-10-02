package rs.logik.phonebook.db.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import rs.logik.phonebook.db.beans.User;
import rs.logik.phonebook.db.config.DBConnection;
import rs.logik.phonebook.statichelpers.HelperDateTimeString;
import rs.logik.phonebook.statichelpers.HelperPasswordHashing;
import rs.logik.phonebook.statichelpers.HelperResourceCloser;

public class DBRepoUser {

    // if user is "deleted" return null
    // if user with username does not exist return null
    // if user is not "deleted" and user with username exists return that user
    public static User getUserByUsername(String username) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE username = ? and isactive = true LIMIT 1";

        User u = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                int userid = rs.getInt("userid");
                String usernamedb = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                boolean isActive = rs.getBoolean("isactive");
                DateTime dt = new DateTime(rs.getDate("datechanged"), DateTimeZone.UTC);
                u = new User(userid, usernamedb, password, email, isActive, dt);
            }

            HelperResourceCloser.closeResources(rs, ps, conn);

        } catch (SQLException ex) {
            Logger.getLogger(DBRepoUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HelperResourceCloser.closeResources(rs, ps, conn);
        }
        return u;
    }

    // korisnik iz baze po id-u
    // ako korisnik sa id-em ne postoji vrati null
    // ako korisnik sa id-em postoji ali je "obrisan" vrati null
    // ako korisnik sa id-em postoji vrati korisnika
    // userid, username, password, email, isactive, datechanged
    public static User getUserById(int userid) {
        User u = null;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE userid = ? and isactive = true LIMIT 1";

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userid);
            rs = ps.executeQuery();

            if (rs.next()) {
                int useriddb = rs.getInt("userid");
                String username = rs.getString("username");
                String password = HelperPasswordHashing.getMD5HashFromString(rs.getString("password"));
                String email = rs.getString("email");
                boolean isActive = rs.getBoolean("isactive");
                DateTime dt = HelperDateTimeString.getDateTimeFromString(rs.getString("datechanged"));

                u = new User(userid, username, password, email, isActive, dt);

                HelperResourceCloser.closeResources(rs, ps, conn);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBRepoUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HelperResourceCloser.closeResources(rs, ps, conn);
        }

        return u;
    }

    // assumption - user is found
    public static boolean isPasswordGood(String username, String password) {
        User user = getUserByUsername(username);
        return user != null ? HelperPasswordHashing.getMD5HashFromString(password).equals(user.getPassword()) : false;
    }

    // korisnik ne postoji
    // newUsername = oldUsername
    // korisnik postoji i newUsername != oldUsername
    // assumption - korisnik je korisnik iz baze
    // datum promene
    public static void changeUsernameForUser(User u, String newUsername) throws IllegalArgumentException {
        if (u != null && u.isActive()) {
            if (!newUsername.equals(u.getUsername())) {

                Connection conn = null;
                PreparedStatement ps = null;
                String sql = "UPDATE user SET username = ? WHERE userid = ?";

                try {
                    conn = DBConnection.getConnection();
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, newUsername);
                    ps.setInt(2, u.getUserid());
                    ps.executeUpdate();
                    u.setUsername(newUsername);

                    HelperResourceCloser.closeResources(null, ps, conn);

                } catch (SQLException ex) {
                    Logger.getLogger(DBRepoUser.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    HelperResourceCloser.closeResources(null, ps, conn);
                }
            } else {
                throw new IllegalArgumentException("Ime za promenu je isto!");
            }
        } else {
            throw new IllegalArgumentException("Korisnik ne postoji!");
        }
    }

    // assumption - user is from a database
    // datum promene
    public static void changePasswordForUser(User u, String newPassword) {
        if (u != null && u.isActive()) {
            Connection conn = null;
            PreparedStatement ps = null;
            String sql = "UPDATE user SET password = ? WHERE userid = ?";

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                String newPasswordHash = HelperPasswordHashing.getMD5HashFromString(newPassword);
                ps.setString(1, newPasswordHash);
                ps.setInt(2, u.getUserid());
                ps.executeUpdate();
                u.setPassword(newPasswordHash);

                HelperResourceCloser.closeResources(null, ps, conn);
                //writeChange(User u, ChangeType);
            } catch (SQLException ex) {
                Logger.getLogger(DBRepoUser.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(null, ps, conn);
            }
        } else {
            throw new IllegalArgumentException("Korisnik ne postoji!");
        }
    }

    //assumption user is from database
    public static void deleteUser(User u) {
        if (u != null && u.isActive()) {
            Connection conn = null;
            PreparedStatement ps = null;
            String sql = "UPDATE user SET isactive = false WHERE userid = ?";

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setInt(1, u.getUserid());
                ps.executeUpdate();
                u.setIsActive(false);

                HelperResourceCloser.closeResources(null, ps, conn);
                //writeChange(User u, ChangeType);
            } catch (SQLException ex) {
                Logger.getLogger(DBRepoUser.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(null, ps, conn);
            }
        } else {
            throw new IllegalArgumentException("Korisnik ne postoji!");
        }
    }

    public static void saveUser(User u) throws IllegalArgumentException {
        if (u != null && u.isActive()) {
            boolean isUpdate = u.getUserid() > 0;

            Connection conn = null;
            PreparedStatement ps = null;
            String sql;

            sql = isUpdate ? "UPDATE user SET username = ?, password = ?, email = ?, datechanged = ? where userid = ?"
                    : "INSERT INTO user(username, password, email, datechanged) VALUES(?, ?, ?, ?)";

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, u.getUsername());
                if (isUpdate) {
                    ps.setString(2, u.getPassword());
                } else {
                    ps.setString(2, HelperPasswordHashing.getMD5HashFromString(u.getPassword()));
                }
                ps.setString(3, u.getEmail());
                ps.setString(4, HelperDateTimeString.getStringFromDateTime(new DateTime(System.currentTimeMillis())));
                if (isUpdate) {
                    ps.setInt(5, u.getUserid());
                }
                ps.executeUpdate();

                HelperResourceCloser.closeResources(null, ps, conn);
            } catch (SQLException ex) {
                Logger.getLogger(DBRepoUser.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(null, ps, conn);
            }
        } else {
            throw new IllegalArgumentException("Korisnik ne postoji!");
        }
    }
}
