/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Frames.mini;

import Classes.DbConnection;
import Panels.Users;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.*;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author padilla
 */
public class AddUserFrame extends javax.swing.JFrame {

    /**
     * Creates new form AddUserFrame
     */
    Users users;
    Connection conn = null;
    File file;
    String path = null;
   
    
     
    
    //validation
    boolean isValidFirstName;
    boolean isValidLastName;
    boolean isValidUserName;
    boolean isValidContactNo;
    boolean isValidemail;
    boolean isValidPassword;
    
    //validation image
    ImageIcon warning = new ImageIcon(this.getClass().getClassLoader().getResource("Icons/warning.png"));
    ImageIcon good = new ImageIcon(this.getClass().getClassLoader().getResource("Icons/check.png"));

    public AddUserFrame() {

        conn = DbConnection.dbConnect();
        initComponents();
        loadDefault();
    }
    
    public void setInstance(Users users){
        this.users =users;
    }

    void loadDefault() {

        try {
            file = new File(this.getClass().getClassLoader().getResource("img/defaultUser.gif").toURI());
            System.out.println(this.getClass().getClassLoader().getResource("img/defaultUser.gif").toURI());

            double size = (double) file.length() / (1024 * 1024);

            System.out.println("here" + size);

        } catch (Exception e) {

        }

        ImageIcon ii = new ImageIcon(this.getClass().getClassLoader().getResource("img/defaultUser.gif"));
        Image img = ii.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);

        lblImage.setIcon(new ImageIcon(img));
        btnSave.setForeground(Color.GRAY);
        btnSave.setEnabled(false);
        
        clearTextField();
        
        lblUserWarning.setVisible(false);
    }

    void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileExtension = new FileNameExtensionFilter("PNG JPG AND JPEG", "png", "jpeg", "jpg");
        fileChooser.addChoosableFileFilter(fileExtension);

        int load = fileChooser.showOpenDialog(null);

        if (load == fileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();

            path = file.getAbsolutePath();
            double size = (double) file.length() / (1024 * 1024);
           
            if (size > 1) {
                JOptionPane.showMessageDialog(null, "File is too big");
                loadDefault();
            } else {
                ImageIcon ii = new ImageIcon(path);
                Image img = ii.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            }

        }
    }
    
    boolean checkUserName(){
        try{
            PreparedStatement pst = conn.prepareStatement("Select * from tbl_users where username = '"+txtUserName.getText()+"'");
            ResultSet rs = pst.executeQuery();
            rs.last();
            if(rs.getRow()==0){
                return false;
            }
        }catch(Exception e){
            
        }
        return true;
    }

    void saveButton() {
        String firstName = txtfName.getText();
        String lastName = txtlName.getText();
        String username = txtUserName.getText();
        String contactno = ftxtContactno.getText();
        String email = txtemail.getText();
        String Password = txtPassword.getText();

        String sql1 = "INSERT INTO tbl_users(username,pass_word) VALUES(?,?)";
        String sql2 = "INSERT INTO tbl_userinfo(user_role, user_firstname, user_lastname, user_contact, user_email, user_image) VALUES(?,?,?,?,?,?);";

        try {
            InputStream is = new FileInputStream(file);

            PreparedStatement pst = conn.prepareStatement(sql2);
            pst.setString(1, "User");
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            pst.setString(4, contactno);
            pst.setString(5, email);
            pst.setBlob(6, is);
            pst.executeUpdate();

            pst = conn.prepareStatement(sql1);
            pst.setString(1, username);
            pst.setString(2, Password);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Inserted");

        } catch (Exception e) {
            System.out.println("Error inserting " + e);
        }
    }
    
    private void checkAllFields(){
        if(isValidFirstName && isValidLastName && isValidUserName && isValidContactNo && isValidemail && isValidPassword){
            btnSave.setEnabled(true);
              btnSave.setForeground(new Color(51,51,51));
        }else{
             btnSave.setEnabled(false);
             btnSave.setForeground(Color.GRAY);
        }
                
     
   
    
    }
    
    private void clearTextField() {
        txtPassword.setText("");
        txtUserName.setText("");
        txtemail.setText("");
        txtfName.setText("");
        txtlName.setText("");
        ftxtContactno.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblUpload = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        txtfName = new javax.swing.JTextField();
        txtlName = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtemail = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        ftxtContactno = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        lblfName = new javax.swing.JLabel();
        lbllName = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblContact = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblUserWarning = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblUpload.setFont(new java.awt.Font("Garuda", 0, 12)); // NOI18N
        lblUpload.setForeground(new java.awt.Color(0, 51, 204));
        lblUpload.setText("Upload");
        lblUpload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUploadMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblUploadMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblUploadMouseExited(evt);
            }
        });
        jPanel3.add(lblUpload, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 126, 51, -1));
        jPanel3.add(lblImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 140, 140));

        txtfName.setBackground(new java.awt.Color(51, 51, 51));
        txtfName.setFont(new java.awt.Font("Waree", 0, 14)); // NOI18N
        txtfName.setForeground(new java.awt.Color(255, 255, 255));
        txtfName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtfName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtfNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtfNameKeyReleased(evt);
            }
        });

        txtlName.setBackground(new java.awt.Color(51, 51, 51));
        txtlName.setFont(new java.awt.Font("Waree", 0, 14)); // NOI18N
        txtlName.setForeground(new java.awt.Color(255, 255, 255));
        txtlName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtlName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtlNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtlNameKeyReleased(evt);
            }
        });

        txtUserName.setBackground(new java.awt.Color(51, 51, 51));
        txtUserName.setFont(new java.awt.Font("Waree", 0, 14)); // NOI18N
        txtUserName.setForeground(new java.awt.Color(255, 255, 255));
        txtUserName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtUserName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUserNameFocusLost(evt);
            }
        });
        txtUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserNameKeyReleased(evt);
            }
        });

        txtemail.setBackground(new java.awt.Color(51, 51, 51));
        txtemail.setFont(new java.awt.Font("Waree", 0, 14)); // NOI18N
        txtemail.setForeground(new java.awt.Color(255, 255, 255));
        txtemail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtemail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtemailKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtemailKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LastName");

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("First Name");

        jLabel3.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("UserName");

        jLabel4.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Email");

        jLabel5.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Contact No.");

        jLabel6.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Password");

        txtPassword.setBackground(new java.awt.Color(51, 51, 51));
        txtPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/close.png"))); // NOI18N
        jLabel7.setText("jLabel7");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setFont(new java.awt.Font("Waree", 1, 12)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        ftxtContactno.setBackground(new java.awt.Color(51, 51, 51));
        ftxtContactno.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        ftxtContactno.setForeground(new java.awt.Color(255, 255, 255));
        ftxtContactno.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        ftxtContactno.setFont(new java.awt.Font("Waree", 0, 14)); // NOI18N
        ftxtContactno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ftxtContactnoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ftxtContactnoKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Add New User");

        lblfName.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblfName.setForeground(new java.awt.Color(255, 255, 255));

        lbllName.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lbllName.setForeground(new java.awt.Color(255, 255, 255));

        lblUserName.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblUserName.setForeground(new java.awt.Color(255, 255, 255));

        lblContact.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblContact.setForeground(new java.awt.Color(255, 255, 255));

        lblEmail.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblEmail.setForeground(new java.awt.Color(255, 255, 255));

        lblPassword.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(255, 255, 255));

        lblUserWarning.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        lblUserWarning.setForeground(new java.awt.Color(255, 0, 0));
        lblUserWarning.setText("*UserName already Exist");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtemail)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtfName, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(4, 4, 4)
                                    .addComponent(lblfName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtlName, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbllName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(txtUserName)
                        .addComponent(txtPassword)
                        .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ftxtContactno)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(78, 78, 78)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblContact, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(lblUserWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblfName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lbllName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtlName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblUserWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblContact, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ftxtContactno, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblUploadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUploadMouseEntered
        lblUpload.setText("<html><u>Upload</u></html>");
        lblUpload.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblUpload.setForeground(Color.red);
    }//GEN-LAST:event_lblUploadMouseEntered

    private void lblUploadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUploadMouseExited
        lblUpload.setText("Upload");
        lblUpload.setForeground(new Color(0, 51, 204));

    }//GEN-LAST:event_lblUploadMouseExited

    private void lblUploadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUploadMouseClicked
        uploadImage();
    }//GEN-LAST:event_lblUploadMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        saveButton();
        loadDefault();
        users.getAllData();
        dispose();
        

    }//GEN-LAST:event_btnSaveActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
       
       dispose();
    }//GEN-LAST:event_jLabel7MouseClicked

    private void txtfNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtfNameKeyPressed
        

     
    
       
    }//GEN-LAST:event_txtfNameKeyPressed

    private void txtlNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtlNameKeyPressed
         
    }//GEN-LAST:event_txtlNameKeyPressed

    private void txtUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyPressed
       
       
        
                
    }//GEN-LAST:event_txtUserNameKeyPressed

    private void ftxtContactnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftxtContactnoKeyPressed
       
    }//GEN-LAST:event_ftxtContactnoKeyPressed

    private void txtemailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtemailKeyPressed
       
    }//GEN-LAST:event_txtemailKeyPressed

    private void txtlNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtlNameKeyReleased
       if (txtlName.getText().length()>2 && (Pattern.matches("^[\\p{L} .'-]+$", txtlName.getText()))) {
            isValidLastName =true;
            lbllName.setIcon(good);
        }else{
             isValidLastName =false;
             lbllName.setIcon(warning);
        }
       
       checkAllFields();
    }//GEN-LAST:event_txtlNameKeyReleased

    private void txtfNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtfNameKeyReleased
       if (txtfName.getText().length()>2 && (Pattern.matches("^[\\p{L} .'-]+$", txtfName.getText()))) {
            isValidFirstName =true;
            lblfName.setIcon(good);

        }else{
             isValidFirstName =false;
             lblfName.setIcon(warning);
        }
       checkAllFields();
    }//GEN-LAST:event_txtfNameKeyReleased

    private void txtUserNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyReleased
        if (txtUserName.getText().length()>6 && (Pattern.matches("^[a-zA-Z0-9]*$", txtUserName.getText()))) {
            isValidUserName =true;
            lblUserName.setIcon(good);
        }else{
             isValidUserName =false;
             lblUserName.setIcon(warning);
        }
        checkAllFields();
    }//GEN-LAST:event_txtUserNameKeyReleased

    private void txtemailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtemailKeyReleased
        String email = txtemail.getText();
        String EMAIL_PATTERN
                = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (Pattern.matches(EMAIL_PATTERN,email)) {
            isValidemail = true;
            lblEmail.setIcon(good);
        } else {
            isValidemail = false;
            lblEmail.setIcon(warning);
        }
        checkAllFields();
    }//GEN-LAST:event_txtemailKeyReleased

    private void ftxtContactnoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftxtContactnoKeyReleased
      
         String str =ftxtContactno.getText();
        str = str.replaceAll("[^\\d]", "");
        
        ftxtContactno.setText(str);
        
        int len = str.length();
        
        
        if (len < 7) {
            isValidContactNo = false;
            lblContact.setIcon(warning);
        } else {
            isValidContactNo = true;
            lblContact.setIcon(good);
        }
        checkAllFields();
        
    }//GEN-LAST:event_ftxtContactnoKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        if(txtPassword.getText().length()>6){
            lblPassword.setIcon(good);
            isValidPassword = true;
        }else{
            isValidPassword = false;
            lblPassword.setIcon(warning);
        }
        checkAllFields();
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void txtUserNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUserNameFocusLost
        System.out.println(checkUserName());
        if(! checkUserName()){
            
            isValidUserName = true;
            lblUserName.setIcon(good);
             lblUserWarning.setVisible(false);
        }else{
            isValidUserName = false;
            lblUserName.setIcon(warning);
             lblUserWarning.setVisible(true);
        }
    }//GEN-LAST:event_txtUserNameFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddUserFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddUserFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddUserFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddUserFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddUserFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JFormattedTextField ftxtContactno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblContact;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUpload;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblUserWarning;
    private javax.swing.JLabel lblfName;
    private javax.swing.JLabel lbllName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtfName;
    private javax.swing.JTextField txtlName;
    // End of variables declaration//GEN-END:variables
}
