package rs.logik.phonebook.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.ContactEmail;
import rs.logik.phonebook.db.beans.ContactPhone;
import rs.logik.phonebook.db.beans.ContactTypes;
import rs.logik.phonebook.db.repos.DBRepoContact;
import rs.logik.phonebook.db.repos.DBRepoContactTypes;
import rs.logik.phonebook.statichelpers.HelperCSVWriter;
import rs.logik.phonebook.statichelpers.LoggedInUser;

public class DashboardFrame extends javax.swing.JFrame {

    private ArrayList<Contact> contacts;
    private DefaultListModel<Contact> contactsListModel;
    private DefaultTableModel contactsTableModel;
    private static final int COLUMNS = 8;
    DashboardFrame thizz = this;

    public ArrayList<Contact> getContacts() {
        return this.contacts;
    }

    public DefaultListModel<Contact> getContactsListModel() {
        return this.contactsListModel;
    }

    public DefaultTableModel getContactsTableModel() {
        return this.contactsTableModel;
    }

    public DashboardFrame() {
        DBRepoContactTypes.fillListsWithTypes(ContactTypes.contactTypes, ContactTypes.phoneTypes);
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // kreiranje modela i setovanje
        contactsListModel = new DefaultListModel<>();
        lContacts.setModel(contactsListModel);
        // kreiranje modela i setovanje na tabelu koja trenutno ne postoji

        //contactsTableModel = new DefaultTableModel(headers, COLUMNS);
        contactsTableModel = (DefaultTableModel) this.tContacts.getModel();
        // mora da se napravi Tabela
        //tContacts.setModel(contactsTableModel);

        // punjenje liste
        contacts = DBRepoContact.getAllContactsForUser(LoggedInUser.getUser());
        if (contacts.size() > 0) {
            for (Contact c : contacts) {
                contactsListModel.addElement(c);
            }
        }
        // punjenje tabele
        populateTable(contactsTableModel, contacts);
        tContacts.removeColumn(tContacts.getColumn("Id"));

        TableRowSorter<DefaultTableModel> trs = new TableRowSorter(contactsTableModel);
        tContacts.setRowSorter(trs);

        lContacts.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    populateTableWithSingleEntry(contactsTableModel, lContacts.getSelectedValue());
                }
            }
        });

        bFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindFrame f = new FindFrame(thizz, false);
            }
        });

        bListAllContacts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateTable(contactsTableModel, contacts);
            }
        });

        bRemoveContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeContact();
            }
        });

        miChangeUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeUsernameDialog ud = new ChangeUsernameDialog(thizz, false);
            }
        });

        miChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePasswordDialog pd = new ChangePasswordDialog(thizz, false);
            }
        });

        miLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggedInUser.setUser(null);
                dispose();
            }
        });

        bAddContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddChangeContact acc = new AddChangeContact(thizz, false, null);
            }
        });

        bChangeContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Contact selectedContact = lContacts.getSelectedValue();
                if (selectedContact != null) {
                    AddChangeContact acc = new AddChangeContact(thizz, false, selectedContact);
                } else {
                    JOptionPane.showMessageDialog(thizz, "No contact selected!", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    private void removeContact() {
        Contact selectedContact = lContacts.getSelectedValue();
        if (selectedContact != null) {
            int confirm = JOptionPane.showConfirmDialog(thizz, String.format("Are you sure you want to remove contact '%s'?", selectedContact.getFirstname()), "Confirm!", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (DBRepoContact.deleteContactForUser(LoggedInUser.getUser(), selectedContact)) {
                    contactsListModel.remove(lContacts.getSelectedIndex());
                    contacts.remove(selectedContact);
                    populateTable(contactsTableModel, contacts);
                    contactsTableModel.setRowCount(0);
                }
            }
        } else {
            JOptionPane.showMessageDialog(thizz, "No contact is selected!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void populateTable(DefaultTableModel tmodel, List<Contact> contacts) {
        tmodel.setRowCount(contacts.size());
        //tmodel.addRow
        for (int i = 0; i < contacts.size(); i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Contact c = contacts.get(i);
                String input = "";
                List<ContactPhone> phones = null;
                List<ContactEmail> emails = null;
                switch (j) {
                    case 0:
                        int id = c.getContactId();
                        input = String.valueOf(id);
                        tmodel.setValueAt(input, i, j);
                        break;
                    case 1:
                        String firstname = c.getFirstname();
                        if (firstname != null) {
                            input = firstname;
                        } else {
                            input = "";
                        }
                        tmodel.setValueAt(input, i, j);
                        break;
                    case 2:
                        String lastname = c.getLastname();
                        if (lastname != null) {
                            input = lastname;
                        } else {
                            input = "";
                        }
                        tmodel.setValueAt(input, i, j);
                        break;
                    case 3:
                        String contacttype = c.getContactType();
                        if (contacttype != null) {
                            input = contacttype;
                        } else {
                            input = "";
                        }
                        tmodel.setValueAt(input, i, j);
                        break;
                    case 4:
                        String description = c.getDescription();
                        if (description != null) {
                            input = description;
                        } else {
                            input = "";
                        }
                        tmodel.setValueAt(input, i, j);
                        break;
                    case 5:
                        input = "";
                        phones = c.getPhones();
                        if (phones.size() > 0) {
                            input = phones.get(0).getPhonenum();
                        }
                        tmodel.setValueAt(input, i, j);
                        break;
                    case 6:
                        input = "";
                        phones = c.getPhones();
                        if (phones.size() > 0) {
                            input = phones.get(0).getPhonenumType();
                        }
                        tmodel.setValueAt(input, i, j);
                        break;
                    case 7:
                        input = "";
                        emails = c.getEmails();
                        if (emails.size() > 0) {
                            input = emails.get(0).getEmail();
                        }
                        tmodel.setValueAt(input, i, j);
                        break;
                }
            }
        }
    }

    public void populateTableWithSingleEntry(DefaultTableModel tmodel, Contact c) {
        String firstname = c.getFirstname() != null ? c.getFirstname() : "";
        String lastname = c.getLastname() != null ? c.getLastname() : "";
        String contacttype = c.getContactType() != null ? c.getContactType() : "";
        String description = c.getDescription() != null ? c.getDescription() : "";
        String phonenum = "";
        String phonetype = "";
        List<ContactPhone> phones = c.getPhones();
        if (phones.size() > 0) {
            ContactPhone cp = phones.get(0);
            phonenum = cp.getPhonenum();
            phonetype = cp.getPhonenumType();
        }
        String email = "";
        List<ContactEmail> emails = c.getEmails();
        if (emails.size() > 0) {
            email = emails.get(0).getEmail();
        }
        Object[] rowData = {firstname, lastname, contacttype, description, phonenum, phonetype, email};
        tmodel.setRowCount(0);
        tmodel.addRow(rowData);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        lContacts = new javax.swing.JList<>();
        bAddContact = new javax.swing.JButton();
        bRemoveContact = new javax.swing.JButton();
        bChangeContact = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tContacts = new javax.swing.JTable();
        bFind = new javax.swing.JButton();
        bListAllContacts = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miExportCsv = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        miChangeUsername = new javax.swing.JMenuItem();
        miChangePassword = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miLogOut = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane2.setViewportView(lContacts);

        bAddContact.setText("Add Contact");

        bRemoveContact.setText("Remove");

        bChangeContact.setText("Change contact");

        tContacts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Firstname", "Lastname", "Contact type", "Description", "Phone #", "Phone type", "E-mail"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tContacts);

        bFind.setText("Find");

        bListAllContacts.setText("List all contacts");

        jMenu1.setText("File");

        miExportCsv.setText("Export current search to .csv");
        miExportCsv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExportCsvActionPerformed(evt);
            }
        });
        jMenu1.add(miExportCsv);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Profile Options");

        miChangeUsername.setText("Change username");
        jMenu2.add(miChangeUsername);

        miChangePassword.setText("Change password");
        jMenu2.add(miChangePassword);
        jMenu2.add(jSeparator1);

        miLogOut.setText("Log out");
        jMenu2.add(miLogOut);
        jMenu2.add(jSeparator2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bChangeContact, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addComponent(bRemoveContact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bAddContact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bFind, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bListAllContacts)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bFind)
                    .addComponent(bListAllContacts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bAddContact)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bChangeContact)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRemoveContact))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(147, 147, 147))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miExportCsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExportCsvActionPerformed
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("file.csv")))) {
            HelperCSVWriter.printCSV(contactsTableModel, pw);
            pw.flush();
            JOptionPane.showMessageDialog(thizz, "Successfully exported .csv file!", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(thizz, "Error writing to file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_miExportCsvActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddContact;
    private javax.swing.JButton bChangeContact;
    private javax.swing.JButton bFind;
    private javax.swing.JButton bListAllContacts;
    private javax.swing.JButton bRemoveContact;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JList<Contact> lContacts;
    private javax.swing.JMenuItem miChangePassword;
    private javax.swing.JMenuItem miChangeUsername;
    private javax.swing.JMenuItem miExportCsv;
    private javax.swing.JMenuItem miLogOut;
    private javax.swing.JTable tContacts;
    // End of variables declaration//GEN-END:variables
}
