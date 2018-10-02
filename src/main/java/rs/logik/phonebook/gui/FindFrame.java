package rs.logik.phonebook.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.ContactEmail;
import rs.logik.phonebook.db.beans.ContactPhone;
import rs.logik.phonebook.db.beans.ContactTypes;

public class FindFrame extends javax.swing.JDialog {

    private DefaultComboBoxModel cbModelContactTypes;
    private DefaultComboBoxModel cbModelPhoneTypes;
    private FindFrame thizz = this;

    public FindFrame(DashboardFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cbModelContactTypes = new DefaultComboBoxModel();
        cbModelPhoneTypes = new DefaultComboBoxModel();
        cbContactTypes.setModel(cbModelContactTypes);
        cbPhoneTypes.setModel(cbModelPhoneTypes);
        fillInComboBoxModelWithTextData(ContactTypes.contactTypes, cbModelContactTypes);
        fillInComboBoxModelWithTextData(ContactTypes.phoneTypes, cbModelPhoneTypes);

        rbContactType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cbContactTypes.setEnabled(true);
                } else {
                    cbContactTypes.setEnabled(false);
                }
            }
        });

        rbPhoneType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cbPhoneTypes.setEnabled(true);
                } else {
                    cbPhoneTypes.setEnabled(false);
                }
            }
        });

        bSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generalFind(parent);
            }
        });

        setVisible(true);
    }

    private void generalFind(DashboardFrame parent) {
        ArrayList<Contact> contacts = parent.getContacts();
        DefaultTableModel tModel = parent.getContactsTableModel();
        if (rbFirstname.isSelected()) {
            ArrayList<Contact> firstNameSelection = findAllByFirstnameOrLastName(contacts, tfFind.getText(), true);
            if (firstNameSelection != null && firstNameSelection.size() > 0) {
                parent.populateTable(tModel, firstNameSelection);
            } else {
                JOptionPane.showMessageDialog(thizz, "No entries found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (rbLastname.isSelected()) {
            ArrayList<Contact> lastNameSelection = findAllByFirstnameOrLastName(contacts, tfFind.getText(), false);
            if (lastNameSelection != null & lastNameSelection.size() > 0) {
                parent.populateTable(tModel, lastNameSelection);
            } else {
                JOptionPane.showMessageDialog(thizz, "No entries found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (rbPhoneNum.isSelected()) {
            Contact c = findContactByPhonenumOrEmail(contacts, tfFind.getText(), true);
            if (c != null) {
                parent.populateTableWithSingleEntry(tModel, c);
            } else {
                JOptionPane.showMessageDialog(thizz, "No entries found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (rbEmail.isSelected()) {
            Contact c = findContactByPhonenumOrEmail(contacts, tfFind.getText(), false);
            if (c != null) {
                parent.populateTableWithSingleEntry(tModel, c);
            } else {
                JOptionPane.showMessageDialog(thizz, "No entries found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (rbContactType.isSelected()) {
            ArrayList<Contact> selection = findAllContactsWithContactType(contacts, (String) cbContactTypes.getSelectedItem());
            if (selection != null && selection.size() > 0) {
                parent.populateTable(tModel, selection);
            }
        } else if (rbPhoneType.isSelected()) {
            ArrayList<Contact> selection = findAllContactsWithPhoneType(contacts, (String) cbPhoneTypes.getSelectedItem());
            if (selection != null && selection.size() > 0) {
                parent.populateTable(tModel, selection);
            }
        }
    }

    public void fillInComboBoxModelWithTextData(ArrayList<String> data, DefaultComboBoxModel cbModel) {
        if (data != null && data.size() > 0 && cbModel != null) {
            for (String d : data) {
                cbModel.addElement(d);
            }
        }
    }

    private ArrayList<Contact> findAllByFirstnameOrLastName(ArrayList<Contact> contacts, String firstnameOrLastname, boolean isFirstname) {
        ArrayList<Contact> subList = null;
        if (contacts != null && contacts.size() > 0) {
            subList = new ArrayList<>();
            if (isFirstname) {
                for (Contact c : contacts) {

                    if (c.getFirstname().startsWith(firstnameOrLastname)) {
                        subList.add(c);
                    }
                }
            } else {
                for (Contact c : contacts) {
                    if (c.getLastname().startsWith(firstnameOrLastname)) {
                        subList.add(c);
                    }
                }
            }

        }
        return subList;
    }

    private Contact findContactByPhonenumOrEmail(ArrayList<Contact> contacts, String phonenumOrEmail, boolean isPhonenum) {
        Contact contact = null;

        if (contacts != null && contacts.size() > 0) {
            for (Contact c : contacts) {
                if (isPhonenum) {
                    List<ContactPhone> phones = c.getPhones();
                    if (phones != null && phones.size() > 0) {
                        for (ContactPhone cp : phones) {
                            if (cp.getPhonenum().startsWith(phonenumOrEmail)) {
                                contact = c;
                                break;
                            }
                        }
                    }
                } else {
                    List<ContactEmail> emails = c.getEmails();
                    if (emails != null && emails.size() > 0) {
                        for (ContactEmail ce : emails) {
                            if (ce.getEmail().startsWith(phonenumOrEmail)) {
                                contact = c;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return contact;
    }

    private ArrayList<Contact> findAllContactsWithContactType(ArrayList<Contact> contacts, String contactType) {
        ArrayList<Contact> subList = null;

        if (contacts != null && contacts.size() > 0 && ContactTypes.contactTypes.contains(contactType)) {
            subList = new ArrayList<>();
            for (Contact c : contacts) {
                if (c.getContactType().equals(contactType)) {
                    subList.add(c);
                }
            }
        }
        return subList;
    }

    private ArrayList<Contact> findAllContactsWithPhoneType(ArrayList<Contact> contacts, String phonenumType) {
        ArrayList<Contact> subList = null;

        if (contacts != null && contacts.size() > 0 && ContactTypes.phoneTypes.contains(phonenumType)) {
            subList = new ArrayList();
            for (Contact c : contacts) {
                List<ContactPhone> phones = c.getPhones();
                if (phones != null && phones.size() > 0) {
                    for (ContactPhone cp : phones) {
                        if (cp.getPhonenumType().equals(phonenumType)) {
                            subList.add(c);
                            break;
                        }
                    }
                }
            }
        }
        return subList;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgFind = new javax.swing.ButtonGroup();
        lFind = new javax.swing.JLabel();
        tfFind = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        rbFirstname = new javax.swing.JRadioButton();
        rbLastname = new javax.swing.JRadioButton();
        rbContactType = new javax.swing.JRadioButton();
        cbContactTypes = new javax.swing.JComboBox<>();
        rbPhoneNum = new javax.swing.JRadioButton();
        rbPhoneType = new javax.swing.JRadioButton();
        cbPhoneTypes = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        rbEmail = new javax.swing.JRadioButton();
        bSearch = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lFind.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lFind.setText("Find");

        tfFind.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("as:");

        bgFind.add(rbFirstname);
        rbFirstname.setText("firstname");

        bgFind.add(rbLastname);
        rbLastname.setText("lastname");

        bgFind.add(rbContactType);
        rbContactType.setText("contact type");

        cbContactTypes.setEnabled(false);
        cbContactTypes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbContactTypesActionPerformed(evt);
            }
        });

        bgFind.add(rbPhoneNum);
        rbPhoneNum.setText("phone#");

        bgFind.add(rbPhoneType);
        rbPhoneType.setText("phone type");

        cbPhoneTypes.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Find all by:");

        bgFind.add(rbEmail);
        rbEmail.setText("e-mail");

        bSearch.setText("Search");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lFind)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfFind)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(rbFirstname)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbLastname)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbPhoneNum)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbEmail))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(rbContactType)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cbContactTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(rbPhoneType)
                                            .addGap(18, 18, 18)
                                            .addComponent(cbPhoneTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(26, 26, 26))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bSearch)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lFind)
                    .addComponent(tfFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbFirstname)
                    .addComponent(rbLastname)
                    .addComponent(rbPhoneNum)
                    .addComponent(rbEmail))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbContactType)
                    .addComponent(cbContactTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPhoneType)
                    .addComponent(cbPhoneTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(bSearch)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbContactTypesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbContactTypesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbContactTypesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bSearch;
    private javax.swing.ButtonGroup bgFind;
    private javax.swing.JComboBox<String> cbContactTypes;
    private javax.swing.JComboBox<String> cbPhoneTypes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lFind;
    private javax.swing.JRadioButton rbContactType;
    private javax.swing.JRadioButton rbEmail;
    private javax.swing.JRadioButton rbFirstname;
    private javax.swing.JRadioButton rbLastname;
    private javax.swing.JRadioButton rbPhoneNum;
    private javax.swing.JRadioButton rbPhoneType;
    private javax.swing.JTextField tfFind;
    // End of variables declaration//GEN-END:variables
}
