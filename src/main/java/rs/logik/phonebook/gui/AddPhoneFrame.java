package rs.logik.phonebook.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.joda.time.DateTime;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.ContactPhone;
import rs.logik.phonebook.db.beans.ContactTypes;
import rs.logik.phonebook.db.repos.DBRepoContactTypes;

public class AddPhoneFrame extends javax.swing.JDialog {

    private AddPhoneFrame thizz = this;
    private Contact c;
    private DefaultListModel<ContactPhone> modelPhones;

    public AddPhoneFrame(AddChangeContact parent, boolean modal, Contact c) {
        super(parent, modal);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        this.c = c;
        this.modelPhones = parent.getListPhonesModel();

        for (String s : ContactTypes.phoneTypes) {
            cbPhoneType.addItem(s);
        }

        cbPhoneType.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                cbPhoneType.setEnabled(true);
                tfPhoneType.setEnabled(false);
            }
        });

        tfPhoneType.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                cbPhoneType.setEnabled(false);
                tfPhoneType.setEnabled(true);
            }
        });

        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPhone();
            }
        });

        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    //obrisem i ponovo dodam
    // preurediti funkciju
    // klik na 
    public void addPhone() {
        String phoneNum = tfPhoneNum.getText();
        if (phoneNum.length() > 0) {
            String phoneType = cbPhoneType.isEnabled() ? (String) cbPhoneType.getSelectedItem() : tfPhoneType.getText();
            if (phoneType.length() > 0) {
                if (!cbPhoneType.isEnabled()) {
                    DBRepoContactTypes.addPhoneType(phoneType, ContactTypes.phoneTypes);
                }
                if (phoneNum.matches("\\d+")) {
                    int contactId = c != null ? c.getContactId() : 0;
                    ContactPhone cp = new ContactPhone(phoneNum, phoneType, new DateTime(System.currentTimeMillis()), true, contactId);
                    if (!isContactPhoneInModel(cp)) {
                        modelPhones.addElement(cp);
                        if (c != null) {
                            c.addContactPhone(cp);
                        }
                        JOptionPane.showMessageDialog(thizz, "You have succesfully entered a new phonenumber!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(thizz, "Phone already exists!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(thizz, String.format("Entry '%s' is not a valid phone number!", phoneNum), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(thizz, "Please enter your desired phonetype!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(thizz, "Please enter the phone number you wish to add!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean isContactPhoneInModel(ContactPhone phone) {
        boolean isPresent = false;

        for (int i = 0; i < modelPhones.size(); i++) {
            if (modelPhones.getElementAt(i).equals(phone)) {
                isPresent = true;
                break;
            }
        }
        return isPresent;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfPhoneNum = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        bAdd = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        cbPhoneType = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        tfPhoneType = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Phone number:");

        jLabel2.setText("Phone type: ");

        bAdd.setText("Add");

        bCancel.setText("Cancel");

        jLabel3.setText("or");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfPhoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbPhoneType, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfPhoneType))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfPhoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbPhoneType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfPhoneType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bAdd)
                    .addComponent(bCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAdd;
    private javax.swing.JButton bCancel;
    private javax.swing.JComboBox<String> cbPhoneType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField tfPhoneNum;
    private javax.swing.JTextField tfPhoneType;
    // End of variables declaration//GEN-END:variables
}
