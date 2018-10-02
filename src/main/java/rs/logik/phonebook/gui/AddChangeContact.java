package rs.logik.phonebook.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.ContactEmail;
import rs.logik.phonebook.db.beans.ContactPhone;
import rs.logik.phonebook.db.beans.ContactTypes;
import rs.logik.phonebook.db.beans.User;
import rs.logik.phonebook.db.repos.DBRepoContact;
import rs.logik.phonebook.db.repos.DBRepoContactTypes;
import rs.logik.phonebook.statichelpers.LoggedInUser;

public class AddChangeContact extends javax.swing.JDialog {

    private DefaultListModel<ContactPhone> listPhonesModel;
    private DefaultListModel<ContactEmail> listEmailsModel;
    private AddChangeContact thizz = this;
    private Contact contact;
    private DashboardFrame parent;

    public DefaultListModel<ContactPhone> getListPhonesModel() {
        return this.listPhonesModel;
    }

    public DefaultListModel<ContactEmail> getListEmailsModel() {
        return this.listEmailsModel;
    }

    public AddChangeContact(DashboardFrame parent, boolean modal, Contact c) {
        super(parent, modal);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.contact = c;
        this.parent = parent;

        listPhonesModel = new DefaultListModel<>();
        lPhones.setModel(listPhonesModel);
        listEmailsModel = new DefaultListModel<>();
        lEmails.setModel(listEmailsModel);

        cbContactType.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                cbContactType.setEnabled(true);
                tfContactType.setEnabled(false);
            }
        });

        tfContactType.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                cbContactType.setEnabled(false);
                tfContactType.setEnabled(true);
            }
        });

        for (String s : ContactTypes.contactTypes) {
            cbContactType.addItem(s);
        }

        if (contact != null) {
            tfFirstname.setText(contact.getFirstname());
            tfLastname.setText(contact.getLastname());
            cbContactType.getModel().setSelectedItem(contact.getContactType());
            tfContactType.setText(contact.getContactType());
            taDescription.setText(contact.getDescription());

            for (ContactEmail ce : contact.getEmails()) {
                listEmailsModel.addElement(ce);
            }

            for (ContactPhone pe : contact.getPhones()) {
                listPhonesModel.addElement(pe);
            }
        }

        bAddNewPhone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddPhoneFrame apf = new AddPhoneFrame(thizz, false, c);
            }
        });

        bRemoveSelectedPhone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedPhone();
            }
        });

        bAddNewEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEmailFrame aef = new AddEmailFrame(thizz, false, c);
            }
        });

        bRemoveSelectedEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedEmail();
            }
        });

        bSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrChangeContact();
            }
        });
        setVisible(true);
    }

    public void removeSelectedPhone() {
        ContactPhone selectedPhone = lPhones.getSelectedValue();
        if (selectedPhone != null) {
            int option = JOptionPane.showConfirmDialog(thizz, String.format("Are you sure you want to delete '%s'?", selectedPhone.getPhonenum()), "Confirm", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                selectedPhone.setIsActive(false);
                contact.getPhones().get(lPhones.getSelectedIndex()).setIsActive(false);
                listPhonesModel.removeElement(selectedPhone);
            }
        } else {
            JOptionPane.showMessageDialog(thizz, "No phone selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeSelectedEmail() {
        ContactEmail selectedEmail = lEmails.getSelectedValue();
        if (selectedEmail != null) {
            int option = JOptionPane.showConfirmDialog(thizz, String.format("Are you sure you want to delete '%s'?", selectedEmail.getEmail()), "Confirm", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                selectedEmail.setIsActive(false);
                contact.getEmails().get(lEmails.getSelectedIndex()).setIsActive(false);
                listEmailsModel.removeElement(selectedEmail);
            }
        } else {
            JOptionPane.showMessageDialog(thizz, "No email selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String addContact(String firstname, String lastname, String contactType, String description) {
        User u = LoggedInUser.getUser();
        this.contact = new Contact(firstname, lastname, contactType, description, true, null, u.getUserid());
        ContactEmail email;
        for (int i = 0; i < listEmailsModel.size(); i++) {
            contact.addContactEmail(listEmailsModel.getElementAt(i));
        }
        ContactPhone phone;
        for (int i = 0; i < listPhonesModel.size(); i++) {
            contact.addContactPhone(listPhonesModel.getElementAt(i));
        }
        DBRepoContact.saveContactForUser(u, contact);
        return "User successfully created!";
    }

    private String changeContact(String firstname, String lastname, String contactType, String description) {
        contact.setFirstname(firstname);
        contact.setLastname(lastname);
        contact.setContactType(contactType);
        contact.setDescription(description);
        DBRepoContact.saveContactForUser(LoggedInUser.getUser(), contact);
        ArrayList<ContactEmail> disposableEmail = new ArrayList<>();
        ArrayList<ContactPhone> disposablePhones = new ArrayList<>();

        for (ContactEmail ce : contact.getEmails()) {
            if (!ce.isActive()) {
                disposableEmail.add(ce);
            }
        }
        for (ContactPhone cp : contact.getPhones()) {
            if (!cp.isActive()) {
                disposablePhones.add(cp);
            }
        }
        contact.getEmails().removeAll(disposableEmail);
        contact.getPhones().removeAll(disposablePhones);

        return "User successfully updated!";
    }

    public void addOrChangeContact() {
        String firstname = tfFirstname.getText();
        if (firstname.length() > 0) {
            boolean isCreation = contact == null;
            String lastname = tfLastname.getText();
            String contactType = cbContactType.isEnabled() ? (String) cbContactType.getModel().getSelectedItem() : tfContactType.getText();
            if (!cbContactType.isEnabled()) {
                DBRepoContactTypes.addContactType(contactType, ContactTypes.contactTypes);
            }
            String description = taDescription.getText();
            String message;
            if (isCreation) {
                message = addContact(firstname, lastname, contactType, description);
                parent.getContactsListModel().addElement(contact);
                parent.getContacts().add(contact);
            } else {
                message = changeContact(firstname, lastname, contactType, description);
            }
            JOptionPane.showMessageDialog(thizz, message, "information", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfFirstname = new javax.swing.JTextField();
        tfLastname = new javax.swing.JTextField();
        tfContactType = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lPhones = new javax.swing.JList<>();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lEmails = new javax.swing.JList<>();
        bAddNewPhone = new javax.swing.JButton();
        bAddNewEmail = new javax.swing.JButton();
        bSubmit = new javax.swing.JButton();
        cbContactType = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        bRemoveSelectedPhone = new javax.swing.JButton();
        bRemoveSelectedEmail = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Firstname:");

        jLabel2.setText("Lastname:");

        jLabel3.setText("Contact type:");

        jLabel4.setText("Description:");

        tfFirstname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tfFirstname.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, null));

        taDescription.setColumns(20);
        taDescription.setRows(5);
        jScrollPane1.setViewportView(taDescription);

        jLabel6.setText("Phones: ");

        jScrollPane2.setViewportView(lPhones);

        jLabel7.setText("E-mails:");

        jScrollPane4.setViewportView(lEmails);

        bAddNewPhone.setText("Add");

        bAddNewEmail.setText("Add");

        bSubmit.setText("OK");

        jLabel5.setText("or");

        bRemoveSelectedPhone.setText("Remove");

        bRemoveSelectedEmail.setText("Remove");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bRemoveSelectedPhone)
                            .addComponent(bAddNewPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bRemoveSelectedEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bAddNewEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfLastname, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cbContactType, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tfContactType, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addGroup(layout.createSequentialGroup()
                .addGap(213, 213, 213)
                .addComponent(bSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(tfFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(tfLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbContactType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(tfContactType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAddNewPhone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRemoveSelectedPhone))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAddNewEmail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRemoveSelectedEmail))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(bSubmit)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddNewEmail;
    private javax.swing.JButton bAddNewPhone;
    private javax.swing.JButton bRemoveSelectedEmail;
    private javax.swing.JButton bRemoveSelectedPhone;
    private javax.swing.JButton bSubmit;
    private javax.swing.JComboBox<String> cbContactType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JList<ContactEmail> lEmails;
    private javax.swing.JList<ContactPhone> lPhones;
    private javax.swing.JTextArea taDescription;
    private javax.swing.JTextField tfContactType;
    private javax.swing.JTextField tfFirstname;
    private javax.swing.JTextField tfLastname;
    // End of variables declaration//GEN-END:variables
}
