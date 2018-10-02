package rs.logik.phonebook.statichelpers;

import java.io.PrintWriter;
import javax.swing.table.DefaultTableModel;

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
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                switch (j) {
                    case 0:
                        firstname = (String) model.getValueAt(i, j);
                        break;
                    case 1:
                        lastname = (String) model.getValueAt(i, j);
                        break;
                    case 2:
                        contacttype = (String) model.getValueAt(i, j);
                        break;
                    case 3:
                        description = (String) model.getValueAt(i, j);
                        break;
                    case 4:
                        phone = (String) model.getValueAt(i, j);
                        break;
                    case 5:
                        phonetype = (String) model.getValueAt(i, j);
                        break;
                    case 6:
                        email = (String) model.getValueAt(i, j);
                        break;
                }
            }
            String output = String.format("%s, %s, %s, %s, %s, %s, %s", firstname, lastname, contacttype, description, phone, phonetype, email);
            pw.println(output);
        }
    }
}
