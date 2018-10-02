package rs.logik.phonebook.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.joda.time.DateTime;
import rs.logik.phonebook.db.beans.User;
import rs.logik.phonebook.db.repos.DBRepoUser;
import rs.logik.phonebook.statichelpers.HelperEmailValidator;

public class SigninDialog extends javax.swing.JDialog {

    public SigninDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        lStatusEmail.setText("");
        lStatusUsername.setText("");
        lStatusPasswordField.setText("");
        lStatusPasswordConfirm.setText("");
        pfPassword.setEchoChar((char) 0);
        pfConfirmPassword.setEchoChar((char) 0);
        setTitle("Sign in");

        tfEmail.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lStatusEmail.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lStatusEmail.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lStatusEmail.setText("");
            }
        });
        
        tfUsername.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lStatusUsername.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lStatusUsername.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lStatusUsername.setText("");
            }
        });

        pfPassword.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pfPassword.setEchoChar('*');
                pfPassword.setText("");
                lStatusPasswordField.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }

        });

        pfConfirmPassword.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pfConfirmPassword.setEchoChar('*');
                pfConfirmPassword.setText("");
                lStatusPasswordConfirm.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {
              
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        // prvo da sva polja nisu popunjena,
        // onda da user postoji
        // da ne valja mejl
        // da password ne odgovara 
        bOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createProfile();
            }
        });

        setVisible(true);
    }

    public boolean createProfile() {
        String email = tfEmail.getText();
        if (email.length() > 0) {
            String username = tfUsername.getText();
            if (username.length() > 0) {
                String password = String.valueOf(pfPassword.getPassword());
                if (password.length() > 0) {
                    String passwordConfirm = String.valueOf(pfConfirmPassword.getPassword());
                    if (passwordConfirm.length() > 0) {
                        if (DBRepoUser.getUserByUsername(username) == null) {
                            if (HelperEmailValidator.validate(email)) {
                                if (password.equals(passwordConfirm)) {
                                    User u = new User(username, password, email, true, new DateTime(System.currentTimeMillis()));
                                    DBRepoUser.saveUser(u);
                                    JOptionPane.showMessageDialog(this, "User succesfully created!", "Information", JOptionPane.INFORMATION_MESSAGE);
                                    dispose();
                                    return true;
                                } else {
                                    lStatusPasswordConfirm.setText("Passwords do not match!");
                                }
                            } else {
                                lStatusEmail.setText(String.format("Entry '%s' is not a valid e-mail!", email));
                            }
                        } else {
                            lStatusUsername.setText(String.format("User with username '%s' already exists!", username));
                        }
                    } else {
                        lStatusPasswordConfirm.setText("Please confirm your password!");
                    }
                } else {
                    lStatusPasswordField.setText("Please enter your password!");
                }
            } else {
                lStatusUsername.setText("Please enter your username!");
            }
        } else {
            lStatusEmail.setText("Please enter your email!");
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfEmail = new javax.swing.JTextField();
        lStatusEmail = new javax.swing.JLabel();
        tfUsername = new javax.swing.JTextField();
        lStatusUsername = new javax.swing.JLabel();
        pfPassword = new javax.swing.JPasswordField();
        lStatusPasswordField = new javax.swing.JLabel();
        pfConfirmPassword = new javax.swing.JPasswordField();
        lStatusPasswordConfirm = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        tfEmail.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tfEmail.setText("Enter your e-mail here...");
        tfEmail.setBorder(null);

        lStatusEmail.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lStatusEmail.setForeground(new java.awt.Color(255, 0, 0));
        lStatusEmail.setText("jLabel1");

        tfUsername.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tfUsername.setText("Enter your username here...");
        tfUsername.setBorder(null);

        lStatusUsername.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lStatusUsername.setForeground(new java.awt.Color(255, 0, 0));
        lStatusUsername.setText("jLabel2");

        pfPassword.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        pfPassword.setText("Enter password here...");
        pfPassword.setBorder(null);

        lStatusPasswordField.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lStatusPasswordField.setForeground(new java.awt.Color(255, 0, 0));
        lStatusPasswordField.setText("jLabel3");

        pfConfirmPassword.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        pfConfirmPassword.setText("Confirm your password...");
        pfConfirmPassword.setBorder(null);

        lStatusPasswordConfirm.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lStatusPasswordConfirm.setForeground(new java.awt.Color(255, 0, 0));
        lStatusPasswordConfirm.setText("jLabel4");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 204, 255));
        jLabel5.setText("Enter your profile credentials");

        bOk.setText("OK");
        bOk.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfEmail)
                            .addComponent(lStatusEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfUsername)
                            .addComponent(lStatusUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pfPassword)
                            .addComponent(lStatusPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pfConfirmPassword)
                            .addComponent(lStatusPasswordConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel5)
                        .addGap(0, 122, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bOk, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(145, 145, 145)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel5)
                .addGap(23, 23, 23)
                .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lStatusEmail)
                .addGap(18, 18, 18)
                .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lStatusUsername)
                .addGap(18, 18, 18)
                .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lStatusPasswordField)
                .addGap(18, 18, 18)
                .addComponent(pfConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lStatusPasswordConfirm)
                .addGap(18, 18, 18)
                .addComponent(bOk)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bOk;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lStatusEmail;
    private javax.swing.JLabel lStatusPasswordConfirm;
    private javax.swing.JLabel lStatusPasswordField;
    private javax.swing.JLabel lStatusUsername;
    private javax.swing.JPasswordField pfConfirmPassword;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
}
