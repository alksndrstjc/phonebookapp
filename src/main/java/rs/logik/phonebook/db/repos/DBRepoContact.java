package rs.logik.phonebook.db.repos;

// getContactByContactId(int contactId)
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.ContactEmail;
import rs.logik.phonebook.db.beans.ContactPhone;
import rs.logik.phonebook.db.beans.User;
import rs.logik.phonebook.db.config.DBConnection;
import rs.logik.phonebook.statichelpers.HelperDateTimeString;
import rs.logik.phonebook.statichelpers.MyLogger;
import rs.logik.phonebook.statichelpers.HelperResourceCloser;
import rs.logik.phonebook.statichelpers.LoggedInUser;

// deleteContact(Contact c)
public class DBRepoContact {

    private static void getAllEmailForContact(Contact c) {
        if (c != null && c.isActive()) {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "SELECT * FROM contactemail WHERE contactid = ? and isactive = true";

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setInt(1, c.getContactId());
                rs = ps.executeQuery();

                while (rs.next()) {
                    int contactEmailId = rs.getInt("contactemailid");
                    String email = rs.getString("email");
                    DateTime dateChanged = HelperDateTimeString.getDateTimeFromString(rs.getString("datechanged"));
                    boolean isActive = rs.getBoolean("isactive");
                    int contactId = rs.getInt("contactid");

                    c.addContactEmail(new ContactEmail(contactEmailId, email, dateChanged, isActive, contactId));
                }

                HelperResourceCloser.closeResources(rs, ps, conn);

            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContact.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(rs, ps, conn);
            }
        }
    }

    private static void getAllPhonesForContact(Contact c) {
        if (c != null && c.isActive()) {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "SELECT * FROM contactphonenum WHERE contactid = ? AND isactive = true";

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setInt(1, c.getContactId());
                rs = ps.executeQuery();

                while (rs.next()) {
                    int phonenumId = rs.getInt("phonenumid");
                    String phoneNum = rs.getString("phonenum");
                    String pType = rs.getString("phonenumtype");
                    DateTime dateChanged = HelperDateTimeString.getDateTimeFromString(rs.getString("datechanged"));
                    boolean isActive = rs.getBoolean("isactive");
                    int contactId = rs.getInt("contactid");

                    c.addContactPhone(new ContactPhone(phonenumId, phoneNum, pType, dateChanged, isActive, contactId));
                }

                rs.close();
                rs = null;
                ps.close();
                ps = null;
                conn.close();
                conn = null;

            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContact.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(rs, ps, conn);
            }
        }
    }

    public static ArrayList<Contact> getAllContactsForUser(User u) {
        ArrayList<Contact> contacts = new ArrayList();

        if (u != null && u.isActive()) {

            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "SELECT * FROM contact WHERE userid = ? and isactive = true";

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setInt(1, u.getUserid());
                rs = ps.executeQuery();

                while (rs.next()) {
                    int contactId = rs.getInt("contactid");
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String cType = rs.getString("contacttype");
                    String description = rs.getString("description");
                    boolean isActive = rs.getBoolean("isactive");
                    DateTime dateChanged = HelperDateTimeString.getDateTimeFromString(rs.getString("datechanged"));
                    int userId = rs.getInt("userid");

                    Contact c = new Contact(contactId, firstname, lastname, cType, description, isActive, dateChanged, userId);
                    getAllPhonesForContact(c);
                    getAllEmailForContact(c);
                    contacts.add(c);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContact.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(rs, ps, conn);
            }
        } else {
            throw new IllegalArgumentException("Korisnik ne postoji!");
        }

        return contacts;
    }

    //contactid, firstname, lastname, contacttype, description, isactive, datechanged, userid
    public static void saveContactForUser(User u, Contact c) {
        if (u != null && u.isActive() && c != null && c.isActive()) {

            boolean isUpdate = c.getContactId() > 0;

            String sql;
            sql = isUpdate ? "UPDATE contact SET firstname = ?, lastname = ?, contacttype = ?, description = ?, isactive = ?, datechanged = ?, userid = ? where contactid = ?"
                    : "INSERT INTO contact(firstname, lastname, contacttype, description, isactive, datechanged, userid) VALUES(?, ?, ?, ?, ?, ?, ?)";

            Connection conn = null;
            PreparedStatement ps = null;

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, c.getFirstname());
                ps.setString(2, c.getLastname());
                ps.setString(3, String.valueOf(c.getContactType()));
                ps.setString(4, c.getDescription());
                ps.setBoolean(5, c.isActive());
                String dateTime = HelperDateTimeString.getStringFromDateTime(new DateTime(System.currentTimeMillis()));
                ps.setString(6, dateTime);
                ps.setInt(7, c.getUserId());
                if (isUpdate) {
                    ps.setInt(8, c.getContactId());
                }
                ps.executeUpdate();

                if (isUpdate) {
                    MyLogger.getInstance().changeContact(LoggedInUser.getUser(), c);
                } else {
                    MyLogger.getInstance().addNewContact(u, c);
                }

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    c.setContactId((int) rs.getLong(1));
                }
                List<ContactEmail> emails = c.getEmails();
                for (int i = 0; i < emails.size(); i++) {
                    ContactEmail email = emails.get(i);
                    email.setContactId(c.getContactId());
                    saveContactEmailForContact(c, email);
                }
                List<ContactPhone> phones = c.getPhones();
                for (int i = 0; i < phones.size(); i++) {
                    ContactPhone phone = phones.get(i);
                    phone.setContactId(c.getContactId());
                    saveContactPhoneForContact(c, phone);
                }

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

    public static ContactEmail getContactEmailByEmail(Contact c, String email) {
        ContactEmail ce = null;
        if (c != null && c.isActive()) {
            String sql = "SELECT * FROM contactemail WHERE email = ? and contactid = ? LIMIT 1";

            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                ps.setInt(2, c.getContactId());
                rs = ps.executeQuery();
                if (rs.next()) {
                    int contactEmailId = rs.getInt("contactemailid");
                    String emaildb = rs.getString("email");
                    DateTime dateChanged = HelperDateTimeString.getDateTimeFromString(rs.getString("datechanged"));
                    ce = new ContactEmail(contactEmailId, email, dateChanged, true, c.getContactId());
                }

                HelperResourceCloser.closeResources(rs, ps, conn);

            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContact.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(rs, ps, conn);
            }
        }
        return ce;
    }

    // metoda koja vrsi update i insert
    // unese novi -> upise u bazu
    // unese vec postojeci -> baca gresku
    public static void saveContactEmailForContact(Contact c, ContactEmail ce) throws IllegalArgumentException {
        if (c != null && c.isActive()) {
            String sql;
            ContactEmail cee = getContactEmailByEmail(c, ce.getEmail());
            boolean isUpdate = cee != null;

            sql = isUpdate ? "UPDATE contactemail SET email = ?, datechanged = ?, isactive = ?, contactid = ? WHERE contactemailid = ?"
                    : "INSERT INTO contactemail (email, datechanged, isactive, contactid) VALUES(?, ?, ?, ?)";

            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, ce.getEmail());
                ps.setString(2, HelperDateTimeString.getStringFromDateTime(new DateTime(System.currentTimeMillis())));
                ps.setBoolean(3, ce.isActive());
                ps.setInt(4, c.getContactId());
                if (isUpdate) {
                    ps.setInt(5, cee.getContactEmailId());
                    ps.executeUpdate();
                    ce.setEmail(ce.getEmail());
                    try {
                        c.getEmails().set(c.getEmails().indexOf(ce), ce);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        c.getEmails().add(ce);
                    }
                } else {
                    ps.executeUpdate();
                    ResultSet indexes = ps.getGeneratedKeys();
                    int index = -1;
                    if (indexes.next()) {
                        index = (int) indexes.getLong(1);
                    }
                    ce.setContactEmailId(index);
                    if (!c.getEmails().contains(ce)) {
                        c.addContactEmail(ce);
                    }
                }

                MyLogger.getInstance().changeEmail(LoggedInUser.getUser(), c, ce);

                HelperResourceCloser.closeResources(null, ps, conn);

            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContact.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(null, ps, conn);
            }

        } else {
            throw new IllegalArgumentException("No such contact!");
        }

    }

    public static ContactPhone getContactPhoneByPhone(Contact c, String phonenum) {
        ContactPhone cp = null;
        if (c != null && c.isActive()) {
            String sql = "SELECT * FROM contactphonenum WHERE phonenum = ? and contactid = ? LIMIT 1";
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setString(1, phonenum);
                ps.setInt(2, c.getContactId());
                rs = ps.executeQuery();
                if (rs.next()) {
                    int phonenumId = rs.getInt("phonenumid");
                    String pType = rs.getString("phonenumtype");
                    DateTime dateChanged = HelperDateTimeString.getDateTimeFromString(rs.getString("datechanged"));
                    cp = new ContactPhone(phonenumId, phonenum, pType, dateChanged, true, c.getContactId());
                }

                HelperResourceCloser.closeResources(rs, ps, conn);

            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContact.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(rs, ps, conn);
            }
        }
        return cp;
    }

    public static void saveContactPhoneForContact(Contact c, ContactPhone cp) throws IllegalArgumentException {
        if (c != null && c.isActive()) {
            String sql;
            ContactPhone cpe = getContactPhoneByPhone(c, cp.getPhonenum());
            boolean isUpdate = cpe != null;

            sql = isUpdate ? "UPDATE contactphonenum SET phonenum = ?, phonenumtype = ?, datechanged = ?, isactive = ?, contactid = ? WHERE phonenumid = ?"
                    : "INSERT INTO contactphonenum (phonenum, phonenumtype, datechanged, isactive, contactid) VALUES(?, ?, ?, ?, ?)";

            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = DBConnection.getConnection();
                ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, cp.getPhonenum());
                ps.setString(2, cp.getPhonenumType());
                ps.setString(3, HelperDateTimeString.getStringFromDateTime(new DateTime(System.currentTimeMillis())));
                ps.setBoolean(4, cp.isActive());
                ps.setInt(5, c.getContactId());
                if (isUpdate) {
                    ps.setInt(6, cpe.getContactPhoneId());
                    ps.executeUpdate();
                    cp.setPhonenum(cp.getPhonenum());
                    try {
                        c.getPhones().set(c.getPhones().indexOf(cp), cp);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        c.getPhones().add(cp);
                    }
                } else {
                    ps.executeUpdate();
                    int index = -1;
                    ResultSet indexes = ps.getGeneratedKeys();
                    if (indexes.next()) {
                        index = (int) indexes.getLong(1);
                    }
                    cp.setContactPhoneId(index);
                    if (!c.getPhones().contains(cp)) {
                        c.addContactPhone(cp);
                    }
                }

                MyLogger.getInstance().changePhone(LoggedInUser.getUser(), c, cp);

                HelperResourceCloser.closeResources(null, ps, conn);

            } catch (SQLException ex) {
                Logger.getLogger(DBRepoContact.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(null, ps, conn);
            }

        } else {
            throw new IllegalArgumentException("No such contact!");
        }
    }

    public static boolean deleteContactForUser(User u, Contact c) {
        if (u != null && u.isActive() && c != null && c.isActive()) {
            Connection conn = null;
            PreparedStatement ps = null;
            String sqlUpdatePhonesForContact = "UPDATE contactphonenum SET isactive = false WHERE contactid = ?";
            String sqlUpdateEmailsForContact = "UPDATE contactemail SET isactive = false WHERE contactid = ?";
            String sqlUpdateContacts = "UPDATE contact SET isactive = false WHERE contactid = ?";

            try {
                conn = DBConnection.getConnection();
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(sqlUpdatePhonesForContact);
                int contactId = c.getContactId();
                ps.setInt(1, contactId);
                ps.executeUpdate();

                ps = conn.prepareStatement(sqlUpdateEmailsForContact);
                ps.setInt(1, contactId);
                ps.executeUpdate();

                ps = conn.prepareStatement(sqlUpdateContacts);
                ps.setInt(1, contactId);
                ps.executeUpdate();

                conn.commit();
                HelperResourceCloser.closeResources(null, ps, conn);
                return true;
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(DBRepoContact.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(DBRepoUser.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                HelperResourceCloser.closeResources(null, ps, conn);
            }
        } else {
            throw new IllegalArgumentException("Korisnik ne postoji!");
        }
        return false;
    }
}
