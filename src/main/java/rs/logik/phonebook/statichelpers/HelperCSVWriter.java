package rs.logik.phonebook.statichelpers;

import java.io.PrintWriter;
import javax.swing.table.DefaultTableModel;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.ContactEmail;
import rs.logik.phonebook.db.beans.ContactPhone;
import rs.logik.phonebook.db.repos.DBRepoContact;

public class HelperCSVWriter {

    public static void printCSV(DefaultTableModel model, PrintWriter pw) {
        String firstname = null;
        String lastname = null;
        String contacttype = null;
        String description = null;
        String phone = null;
        String phonetype = null;
        String email = null;

        pw.println("firstname, lastname, contacttype, description, phone, phonetype, email");
        for (int i = 0, j = 0; i < model.getRowCount(); i++) {
            int id = Integer.parseInt((String) model.getValueAt(i, j));
            Contact c = DBRepoContact.getContactByContactId(id);
            firstname = c.getFirstname();
            lastname = c.getLastname();
            contacttype = c.getContactType();
            description = c.getDescription();
            pw.print(String.format("%s, %s, %s, %s, ", firstname, lastname, contacttype, description));
            for (ContactPhone cp : c.getPhones()) {
                pw.print(String.format("%s, %s, ", cp.getPhonenum(), cp.getPhonenumType()));
            }

            for (ContactEmail ce : c.getEmails()) {
                pw.print(String.format("%s, ", ce.getEmail()));
            }
            pw.println();
        }
    }
}
