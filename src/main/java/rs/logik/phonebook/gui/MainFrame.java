package rs.logik.phonebook.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import rs.logik.phonebook.db.beans.Contact;
import rs.logik.phonebook.db.beans.User;
import rs.logik.phonebook.db.repos.DBRepoUser;
import rs.logik.phonebook.statichelpers.LoggedInUser;

public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        initComponents();
        lPasswordStatus.setText("");
        lUsernameStatus.setText("");
        lStatusAll.setText("");
        setLocationRelativeTo(null);
        MainFrame thizz = this;
        setTitle("Phonebook 1.0.0");

        tfUsername.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (tfUsername.getText().equals("Enter your e-mail or username here...")) {
                    tfUsername.setText("");
                }
            }
        });

        pfPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (String.valueOf(pfPassword.getPassword()).equals("************")) {
                    pfPassword.setText("");
                }
            }
        });

        tfUsername.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lUsernameStatus.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lUsernameStatus.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lUsernameStatus.setText("");
            }
        });

        pfPassword.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lPasswordStatus.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lPasswordStatus.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lPasswordStatus.setText("");
            }
        });

        bLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        bSignin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SigninDialog sid = new SigninDialog(thizz, false);
            }
        });
    }

    public void login() {
        String username = tfUsername.getText();
        if (username.length() > 0) {
            String password = String.valueOf(pfPassword.getPassword());
            if (password.length() > 0) {
                User user = DBRepoUser.getUserByUsername(username);
                if (user != null) {
                    if (DBRepoUser.isPasswordGood(username, password)) {
                        LoggedInUser.setUser(user);
                        DashboardFrame df = new DashboardFrame();
                        tfUsername.setText("");
                        pfPassword.setText("");
                    } else {
                        lPasswordStatus.setText("Wrong password!");
                    }
                } else {
                    lUsernameStatus.setText("No such user!");
                }
            } else {
                lPasswordStatus.setText("Please enter your password!");
            }
        } else {
            lUsernameStatus.setText("Please enter your email or username!");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pLeft = new javax.swing.JPanel();
        bImage = new javax.swing.JButton();
        pRight = new javax.swing.JPanel();
        tfUsername = new javax.swing.JTextField();
        lUsernameStatus = new javax.swing.JLabel();
        lPasswordStatus = new javax.swing.JLabel();
        pfPassword = new javax.swing.JPasswordField();
        bLogin = new javax.swing.JButton();
        bSignin = new javax.swing.JButton();
        lStatusAll = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pLeft.setBackground(new java.awt.Color(0, 153, 255));
        pLeft.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        bImage.setBackground(new java.awt.Color(0, 153, 255));
        bImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonebook.png"))); // NOI18N

        javax.swing.GroupLayout pLeftLayout = new javax.swing.GroupLayout(pLeft);
        pLeft.setLayout(pLeftLayout);
        pLeftLayout.setHorizontalGroup(
            pLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLeftLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(bImage)
                .addContainerGap(48, Short.MAX_VALUE))
        );
        pLeftLayout.setVerticalGroup(
            pLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLeftLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(bImage)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tfUsername.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tfUsername.setText("Enter your e-mail or username here...");
        tfUsername.setBorder(null);

        lUsernameStatus.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lUsernameStatus.setForeground(new java.awt.Color(255, 0, 0));
        lUsernameStatus.setText("jLabel1");

        lPasswordStatus.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lPasswordStatus.setForeground(new java.awt.Color(255, 0, 0));
        lPasswordStatus.setText("jLabel1");

        pfPassword.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        pfPassword.setText("************");
        pfPassword.setBorder(null);

        bLogin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bLogin.setText("Log in");
        bLogin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        bSignin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSignin.setText("Sign in");
        bSignin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lStatusAll.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lStatusAll.setForeground(new java.awt.Color(255, 0, 0));
        lStatusAll.setText("jLabel1");

        javax.swing.GroupLayout pRightLayout = new javax.swing.GroupLayout(pRight);
        pRight.setLayout(pRightLayout);
        pRightLayout.setHorizontalGroup(
            pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRightLayout.createSequentialGroup()
                .addGroup(pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pRightLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pRightLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(bSignin, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lStatusAll, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                    .addComponent(lUsernameStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lPasswordStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(141, Short.MAX_VALUE))
        );
        pRightLayout.setVerticalGroup(
            pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRightLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lUsernameStatus)
                .addGap(16, 16, 16)
                .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lPasswordStatus)
                .addGap(27, 27, 27)
                .addComponent(lStatusAll)
                .addGap(34, 34, 34)
                .addComponent(bLogin)
                .addGap(18, 18, 18)
                .addComponent(bSignin)
                .addContainerGap(122, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pLeft, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bImage;
    private javax.swing.JButton bLogin;
    private javax.swing.JButton bSignin;
    private javax.swing.JLabel lPasswordStatus;
    private javax.swing.JLabel lStatusAll;
    private javax.swing.JLabel lUsernameStatus;
    private javax.swing.JPanel pLeft;
    private javax.swing.JPanel pRight;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
}
