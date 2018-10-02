package rs.logik.phonebook.statichelpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.ContactEmail;
import rs.logik.phonebook.db.beans.ContactPhone;
import rs.logik.phonebook.db.beans.User;

public class MyLogger {

    private final String FILE_PATH = "activity_log.txt";
    private PrintWriter writer;
    private static MyLogger logger = null;

    private MyLogger() {
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)), true);
        } catch (IOException e) {
        }
    }

    public static synchronized MyLogger getInstance() {
        if (logger == null) {
            logger = new MyLogger();
        }
        return logger;
    }

    public void addNewContact(User u, Contact c) {
        writer.println("User '" + u.getUsername() + "' added new contact " + c.getFirstname() + " " + c.getLastname() + " at: "
                + HelperDateTimeString.getStringFromDateTime(c.getDateChanged()));
    }

    public void changeContact(User u, Contact c) {
        if (c.isActive()) {
            changeContactData(u, c);
        } else {
            removeContact(u, c);
        }
    }

    private void changeContactData(User u, Contact c) {
        writer.println("User '" + u.getUsername() + "' changed contact's '" + c.getFirstname() + " " + c.getLastname() + "' data at: "
                + HelperDateTimeString.getStringFromDateTime(c.getDateChanged()));
    }

    private void removeContact(User u, Contact c) {
        writer.println("User '" + u.getUsername() + "' disabled contact '" + c.getFirstname() + " " + c.getLastname() + "' at: "
                + HelperDateTimeString.getStringFromDateTime(c.getDateChanged()));
    }

    public void changeEmail(User u, Contact c, ContactEmail ce) {
        if (ce.isActive()) {
            addNewEmail(u, c, ce);
        } else {
            removeEmail(u, c, ce);
        }
    }

    private void addNewEmail(User u, Contact c, ContactEmail ce) {
        writer.println("User '" + u.getUsername() + "' added new email '" + ce.getEmail() + "' for contact '" + c.getFirstname() + "' " + c.getLastname() + " at: "
                + HelperDateTimeString.getStringFromDateTime(ce.getDateChanged()));
    }

    private void removeEmail(User u, Contact c, ContactEmail ce) {
        writer.println("User '" + u.getUsername() + "' disabled email '" + ce.getEmail() + "' for contact '" + c.getFirstname() + "' " + c.getLastname() + " at: "
                + HelperDateTimeString.getStringFromDateTime(ce.getDateChanged()));
    }

    public void changePhone(User u, Contact c, ContactPhone cp) {
        if (cp.isActive()) {
            addNewPhone(u, c, cp);
        } else {
            removePhone(u, c, cp);
        }
    }

    private void addNewPhone(User u, Contact c, ContactPhone cp) {
        writer.println("User '" + u.getUsername() + "' added new phone '" + cp.getPhonenum() + "' for contact '" + c.getFirstname() + " " + c.getLastname() + "' at: "
                + HelperDateTimeString.getStringFromDateTime(cp.getDateChanged()));
    }

    private void removePhone(User u, Contact c, ContactPhone cp) {
        writer.println("User '" + u.getUsername() + "' disabled phone '" + cp.getPhonenum() + "' for contact '" + c.getFirstname() + " " + c.getLastname() + "' at: "
                + HelperDateTimeString.getStringFromDateTime(cp.getDateChanged()));
    }

}
