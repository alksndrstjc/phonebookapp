package rs.logik.phonebook.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.ContactDetail;
import rs.logik.phonebook.db.beans.ContactEmail;
import rs.logik.phonebook.statichelpers.HelperEmailValidator;

public class AddEmailFrame extends javax.swing.JDialog {

    private AddEmailFrame thizz = this;
    private Contact c;
    private DefaultListModel<ContactEmail> modelEmails;

    public AddEmailFrame(AddChangeContact parent, boolean modal, Contact c) {
        super(parent, modal);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.c = c;
        this.modelEmails = parent.getListEmailsModel();

        bOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmail();
            }
        });

        setVisible(true);
    }

    public void addEmail() {
        String email = tfEmail.getText();
        if (email.length() > 0) {
            if (HelperEmailValidator.validate(email)) {
                int contactId = c != null ? c.getContactId() : 0;
                ContactEmail ce = new ContactEmail(email, null, true, contactId);
                if (!isContactDetailInList(ce)) {
                    modelEmails.addElement(ce);
                    if (c != null) {
                        c.addContactEmail(ce);
                    }
                    JOptionPane.showMessageDialog(thizz, "You have succesfully entered a new email!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(thizz, String.format("Email '%s' already exists!", email), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(thizz, String.format("Entry '%s' is not a valid email address!", email), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(thizz, "Please enter the new email!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean isContactDetailInList(ContactDetail cd) {
        boolean isPresent = false;
        for (int i = 0; i < modelEmails.size(); i++) {
            if (modelEmails.get(i).equals(cd)) {
                isPresent = true;
                break;
            }
        }
        return isPresent;
    }

    //could reduce a lot of duplication by abstracting away features from email and phone as ContactDetail
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfEmail = new javax.swing.JTextField();
        bOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("new e-mail: ");

        bOk.setText("OK");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 132, Short.MAX_VALUE)
                        .addComponent(bOk, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfEmail)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bOk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField tfEmail;
    // End of variables declaration//GEN-END:variables
}
