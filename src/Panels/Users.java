/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panels;

import Classes.DbConnection;
import Classes.User;
import Frames.mini.AddUserFrame;
import Frames.MainFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


import java.sql.*;

import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;



import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;



/**
 *
 * @author padilla
 */
public class Users extends javax.swing.JPanel {

    /**
     * Creates new form Users
     */
    DefaultTableModel model;
    Connection conn = null;
    
    
    File file;
    String path = null;
   
    
    int user_id =-1;
    String userName;
    String password;
    String name;
    String contact ;
    String email;
    String role;
    
     //validation image
    ImageIcon warning = new ImageIcon(this.getClass().getClassLoader().getResource("Icons/warning.png"));
    ImageIcon good = new ImageIcon(this.getClass().getClassLoader().getResource("Icons/check.png"));
    
    ImageIcon promote = new ImageIcon(this.getClass().getClassLoader().getResource("Icons/up_icon.png"));
    ImageIcon demote = new ImageIcon(this.getClass().getClassLoader().getResource("Icons/down_icon.png"));
    
    
    User user;
    public Users() {
        conn = DbConnection.dbConnect();
        initComponents();
       // init();
        loadDefault();
        System.out.println("user id"+user_id);
       
        init();
    }
    
    
    
    public void init(){ 
     
        tableDesign();
        getAllData();
        modifyTable();
        showChangeLabel(false);
        
        lblNote.hide();
        if (user_id <0){
            lblchange.hide();
        }
    }

    
   private void updateField(String entity, String attribute, String value){
       String sql = "UPDATE "+entity+" SET "+attribute+" = '"+value+"' WHERE user_id = "+user_id;
       try{
           PreparedStatement pst = conn.prepareStatement(sql);
           pst.execute();
           
           
       }catch(Exception e){
           System.out.println("Error update"+e);
       }
        getAllData();
   }
   
    private void updateImage(String entity, String attribute, InputStream is){
       String sql = "UPDATE "+entity+" SET "+attribute+" = ? WHERE user_id = "+user_id;
       try{
           PreparedStatement pst = conn.prepareStatement(sql);
           pst.setBlob(1, is);
           pst.execute();
           
           
       }catch(Exception e){
           System.out.println("Error update"+e);
       }
   }

    
   public void getAllData(){
        String sql = "Select * from tbl_userinfo";
        model.setRowCount(0);
        
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.last();
            System.out.println("here:"+rs.getRow());
            Object[] users = new Object[3];
            rs.beforeFirst();
            int i = 0;
            while(rs.next()){
                System.out.println("hello");
                String id = rs.getString("user_id");
                String _name = rs.getString("user_firstname")+" "+rs.getString("user_lastName");
                String _role = rs.getString("user_role");
                
                         
                
                users[0]= id;
                users[1]= _name;
                users[2]= _role;
              
                model.addRow(users);
                
                i++;
            }
                jTable1.setModel(model);
            }catch(Exception e){
                    System.out.println("here "+e);
            }
            
            
        
        
    }
   
  
   void selectDataFromTable(){
       int row = jTable1.getSelectedRow();
       String selected = jTable1.getModel().getValueAt(row, 0).toString();
       user_id = Integer.parseInt(selected);
       System.out.println(selected);
       String sql = "SELECT * FROM tbl_users INNER JOIN tbl_userinfo on tbl_userinfo.user_id = tbl_users.user_id where tbl_users.user_id = "+selected;
       try{
           PreparedStatement pst = conn.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
           
           if(rs.next()){
                
                userName = rs.getString("username");
                role = rs.getString("user_role");
                password = rs.getString("pass_word");
                name = rs.getString("user_firstname")+" "+  rs.getString("user_lastname");
                contact = rs.getString("user_contact");
                email = rs.getString("user_email");
               
               txtName.setText(name);
               txtUserName.setText(userName);
               ftxtContactnum.setText(contact);
               txtEmail.setText(email);
               txtPassword.setText(password);
               
               BufferedImage im = ImageIO.read(rs.getBinaryStream("user_image"));
               ImageIcon ii = new ImageIcon(im);
               Image img = ii.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
               lblImage.setIcon(new ImageIcon(img));

           }

           if (user_id == 1) {
               lblPromote.setVisible(false);
               btnDelete.enable(false);

           } else {
                 lblPromote.setVisible(true);
 btnDelete.enable(true);
               promoteDemoteButton();
           }
           showChangeLabel(true);
            lblchange.show();
       }catch(Exception e){
           System.out.println("Error "+ e);
       }
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
                try{
                     InputStream is = new FileInputStream(file);
                     updateImage("tbl_userinfo", "user_image",  is);
                     JOptionPane.showMessageDialog(null, "Updated");
                }catch(Exception e){
                    
                }
               
            }

        }
    }
     
   private void deleteUser(){
       String sql = "DELETE tbl_users, tbl_userinfo FROM tbl_users INNER JOIN tbl_userinfo ON tbl_users.user_id = tbl_userinfo.user_id WHERE tbl_users.user_id = "+user_id;
       try{
           PreparedStatement pst = conn.prepareStatement(sql);
           pst.execute();
           JOptionPane.showMessageDialog(null, "Item Deleted");
           
       }catch(Exception e){
           System.out.println(e);
       }
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
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblchange = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        lblpassword = new javax.swing.JLabel();
        lblusername = new javax.swing.JLabel();
        lblemail = new javax.swing.JLabel();
        lblcontactnum = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        ftxtContactnum = new javax.swing.JFormattedTextField();
        lblPassword = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblContact = new javax.swing.JLabel();
        lblNote = new javax.swing.JLabel();
        lblname = new javax.swing.JLabel();
        lblPromote = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDelete.setBackground(new java.awt.Color(51, 51, 51));
        btnDelete.setFont(new java.awt.Font("Waree", 1, 10)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeleteMouseExited(evt);
            }
        });
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 150, 80, 30));

        btnAdd.setBackground(new java.awt.Color(51, 51, 51));
        btnAdd.setFont(new java.awt.Font("Waree", 1, 10)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnAddMousePressed(evt);
            }
        });
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 150, 80, 30));

        jLabel1.setFont(new java.awt.Font("Waree", 0, 36)); // NOI18N
        jLabel1.setText("Accounts");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 0, 301, 69));

        jLabel2.setFont(new java.awt.Font("Waree", 0, 14)); // NOI18N
        jLabel2.setText("Welcome Admin");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, 361, 39));

        jLabel4.setBackground(new java.awt.Color(153, 153, 153));
        jLabel4.setFont(new java.awt.Font("Waree", 1, 18)); // NOI18N
        jLabel4.setText("Role");
        jLabel4.setOpaque(true);
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(574, 183, 380, 25));

        jLabel5.setBackground(new java.awt.Color(153, 153, 153));
        jLabel5.setFont(new java.awt.Font("Waree", 1, 18)); // NOI18N
        jLabel5.setText("Name");
        jLabel5.setOpaque(true);
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 183, 573, 25));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblchange.setFont(new java.awt.Font("URW Gothic", 0, 10)); // NOI18N
        lblchange.setForeground(new java.awt.Color(0, 51, 153));
        lblchange.setText("Change");
        lblchange.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblchange.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblchangeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblchangeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblchangeMouseExited(evt);
            }
        });
        jPanel2.add(lblchange, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, -1));

        lblImage.setBackground(new java.awt.Color(153, 153, 153));
        lblImage.setOpaque(true);
        jPanel2.add(lblImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 140, 140));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 150, 150));

        jLabel3.setFont(new java.awt.Font("URW Gothic L", 1, 12)); // NOI18N
        jLabel3.setText("Name:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 15, 120, 30));

        jLabel6.setFont(new java.awt.Font("URW Gothic L", 1, 12)); // NOI18N
        jLabel6.setText("Username:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 45, 130, 30));

        jLabel7.setFont(new java.awt.Font("URW Gothic L", 1, 12)); // NOI18N
        jLabel7.setText("Email:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 75, 120, 30));

        jLabel9.setFont(new java.awt.Font("URW Gothic L", 1, 12)); // NOI18N
        jLabel9.setText("Contact Number:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 105, 130, 30));

        txtName.setEditable(false);
        txtName.setBackground(new java.awt.Color(255, 255, 255));
        txtName.setToolTipText("");
        txtName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        txtName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNameFocusLost(evt);
            }
        });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNameKeyReleased(evt);
            }
        });
        jPanel1.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 15, 210, 25));

        txtUserName.setEditable(false);
        txtUserName.setBackground(new java.awt.Color(255, 255, 255));
        txtUserName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        txtUserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserNameActionPerformed(evt);
            }
        });
        txtUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserNameKeyReleased(evt);
            }
        });
        jPanel1.add(txtUserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 45, 210, 25));

        txtEmail.setEditable(false);
        txtEmail.setBackground(new java.awt.Color(255, 255, 255));
        txtEmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmailKeyReleased(evt);
            }
        });
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 75, 210, 25));

        jLabel10.setFont(new java.awt.Font("URW Gothic L", 1, 12)); // NOI18N
        jLabel10.setText("Password:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 135, 130, 30));

        lblpassword.setFont(new java.awt.Font("URW Gothic", 0, 10)); // NOI18N
        lblpassword.setForeground(new java.awt.Color(0, 51, 153));
        lblpassword.setText("Change");
        lblpassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblpassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblpasswordMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblpasswordMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblpasswordMouseExited(evt);
            }
        });
        jPanel1.add(lblpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 140, -1, -1));

        lblusername.setFont(new java.awt.Font("URW Gothic", 0, 10)); // NOI18N
        lblusername.setForeground(new java.awt.Color(0, 51, 153));
        lblusername.setText("Change");
        lblusername.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblusername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblusernameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblusernameMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblusernameMouseExited(evt);
            }
        });
        jPanel1.add(lblusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, -1, -1));

        lblemail.setFont(new java.awt.Font("URW Gothic", 0, 10)); // NOI18N
        lblemail.setForeground(new java.awt.Color(0, 51, 153));
        lblemail.setText("Change");
        lblemail.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblemail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblemailMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblemailMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblemailMouseExited(evt);
            }
        });
        jPanel1.add(lblemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, -1, -1));

        lblcontactnum.setFont(new java.awt.Font("URW Gothic", 0, 10)); // NOI18N
        lblcontactnum.setForeground(new java.awt.Color(0, 51, 153));
        lblcontactnum.setText("Change");
        lblcontactnum.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblcontactnum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblcontactnumMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblcontactnumMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblcontactnumMouseExited(evt);
            }
        });
        jPanel1.add(lblcontactnum, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 110, -1, -1));

        txtPassword.setEditable(false);
        txtPassword.setBackground(new java.awt.Color(255, 255, 255));
        txtPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });
        jPanel1.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, 210, 25));

        ftxtContactnum.setEditable(false);
        ftxtContactnum.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        ftxtContactnum.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        ftxtContactnum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ftxtContactnumKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ftxtContactnumKeyReleased(evt);
            }
        });
        jPanel1.add(ftxtContactnum, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, 210, 25));
        jPanel1.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 135, 20, 20));
        jPanel1.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 15, 20, 20));
        jPanel1.add(lblUserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 45, 20, 20));
        jPanel1.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 75, 20, 20));
        jPanel1.add(lblContact, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 105, 20, 20));

        lblNote.setFont(new java.awt.Font("Khmer OS System", 0, 11)); // NOI18N
        lblNote.setForeground(new java.awt.Color(255, 0, 0));
        lblNote.setText("Note: FirstName and Lastname must be separated with coma. Ex. Juan, dela Cruz");
        jPanel1.add(lblNote, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 470, 20));

        lblname.setFont(new java.awt.Font("URW Gothic", 0, 10)); // NOI18N
        lblname.setForeground(new java.awt.Color(0, 51, 153));
        lblname.setText("Change");
        lblname.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblname.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblnameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblnameMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblnameMouseExited(evt);
            }
        });
        jPanel1.add(lblname, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, -1, -1));

        lblPromote.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPromote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPromoteMouseClicked(evt);
            }
        });
        jPanel1.add(lblPromote, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 150, 30, 30));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, 210));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);

        jTable1.setBackground(new java.awt.Color(204, 204, 204));
        jTable1.setFont(new java.awt.Font("Waree", 0, 18)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 0, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setOpaque(false);
        jTable1.setRowHeight(30);
        jTable1.setSelectionBackground(new java.awt.Color(255, 255, 153));
        jTable1.setShowVerticalLines(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 209, 950, 400));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMousePressed
        
    }//GEN-LAST:event_btnAddMousePressed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
       AddUserFrame adduser = new AddUserFrame();
       adduser.setInstance(this);
       adduser.show();
       
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseEntered
        changColorEntered(btnAdd);
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseEntered
       changColorEntered(btnDelete);
    }//GEN-LAST:event_btnDeleteMouseEntered

    private void btnAddMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseExited
        changColorExit(btnAdd);
    }//GEN-LAST:event_btnAddMouseExited

    private void btnDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseExited
         changColorExit(btnDelete);
    }//GEN-LAST:event_btnDeleteMouseExited

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
         getAllData();
    }//GEN-LAST:event_formFocusGained

    private void txtUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNameActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void lblnameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblnameMouseEntered
      changeLabelChangeColorEntered(lblname);
    }//GEN-LAST:event_lblnameMouseEntered

    private void lblnameMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblnameMouseExited
        changeLabelChangeColorExit(lblname);
    }//GEN-LAST:event_lblnameMouseExited

    private void lblusernameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblusernameMouseEntered
        changeLabelChangeColorEntered(lblusername);
    }//GEN-LAST:event_lblusernameMouseEntered

    private void lblusernameMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblusernameMouseExited
        changeLabelChangeColorExit(lblusername);
    }//GEN-LAST:event_lblusernameMouseExited

    private void lblemailMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblemailMouseEntered
        changeLabelChangeColorEntered(lblemail);
    }//GEN-LAST:event_lblemailMouseEntered

    private void lblemailMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblemailMouseExited
         changeLabelChangeColorExit(lblemail);
    }//GEN-LAST:event_lblemailMouseExited

    private void lblcontactnumMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblcontactnumMouseEntered
        changeLabelChangeColorEntered(lblcontactnum);
    }//GEN-LAST:event_lblcontactnumMouseEntered

    private void lblcontactnumMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblcontactnumMouseExited
       changeLabelChangeColorExit(lblcontactnum);
    }//GEN-LAST:event_lblcontactnumMouseExited

    private void lblpasswordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblpasswordMouseEntered
        changeLabelChangeColorEntered(lblpassword);
    }//GEN-LAST:event_lblpasswordMouseEntered

    private void lblpasswordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblpasswordMouseExited
         changeLabelChangeColorExit(lblpassword);
    }//GEN-LAST:event_lblpasswordMouseExited

    private void lblnameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblnameMouseClicked
        String str =extract(lblname.getText());
         
        if(str.equals("Change")){
            lblNote.show();
            changeLabelMouseClick(txtName);
        }else{
             String[] _name = txtName.getText().split(",");
             
             updateField("tbl_userinfo", "user_firstname", _name[0]);
             updateField("tbl_userinfo", "user_lastname", _name[1]);
             name =_name[0]+" "+_name[1]; 
             JOptionPane.showMessageDialog(null, "Update Success");
             lblNote.hide();
             backToDefault();
            
        }
       
    }//GEN-LAST:event_lblnameMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
       selectDataFromTable();
    }//GEN-LAST:event_jTable1MouseClicked

    private void lblusernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblusernameMouseClicked
        String str =extract(lblusername.getText());
         
        if(str.equals("Change")){
            changeLabelMouseClick(txtUserName);
        }else{
            try{
                String username = txtUserName.getText();                
                updateField("tbl_users", "username", username);
                JOptionPane.showMessageDialog(null, "Update Succes");
                userName = username;
                backToDefault();
            }catch(Exception e){
                
            }
            
            
        }
       
    }//GEN-LAST:event_lblusernameMouseClicked

    private void lblemailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblemailMouseClicked
        
        
         String str =extract(lblemail.getText());
         
        if(str.equals("Change")){
           changeLabelMouseClick(txtEmail);
        }else{
            try{
                String _email = txtEmail.getText();                
                updateField("tbl_userinfo", "user_email", _email);
                JOptionPane.showMessageDialog(null, "Update Succes");
                email = _email;
                backToDefault();
            }catch(Exception e){
                
            }
            
            
        }
    }//GEN-LAST:event_lblemailMouseClicked

    private void lblcontactnumMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblcontactnumMouseClicked
      
       
        String str =extract(lblcontactnum.getText());
         
        if(str.equals("Change")){
            changeLabelMouseClick(ftxtContactnum);
        }else{
            try{
                String _contact = ftxtContactnum.getText();                
                updateField("tbl_userinfo", "user_contact", _contact);
                JOptionPane.showMessageDialog(null, "Update Succes");
                contact = _contact;
                backToDefault();
            }catch(Exception e){
                
            }
            
            
        }
    }//GEN-LAST:event_lblcontactnumMouseClicked

    private void lblpasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblpasswordMouseClicked
       
         String str =extract(lblpassword.getText());
         
        if(str.equals("Change")){
           changeLabelMouseClick(txtPassword);
        }else{
            try{
                String _password= txtPassword.getText();                
                updateField("tbl_users", "pass_word", _password);
                JOptionPane.showMessageDialog(null, "Update Success");
                password = _password;
                backToDefault();
            }catch(Exception e){
                
            }
            
            
        }
        
    }//GEN-LAST:event_lblpasswordMouseClicked

    private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased
       //^[\\p{L} .'-,]+$
      
//        if (txtName.getText().length()>2 && (Pattern.matches(".*[\\p{L} .'-],[\\p{L} .'-].*", txtName.getText()))) {
//            lblname.setText("Save");
//            lblName.setIcon(good);
//            String[] name = txtName.getText().split(",");
//            String fname = name[0];
//            String lname = name[1];
//       
//        }else{
//             lblname.setText("Change");
//             lblName.setIcon(warning);
//        }
        if (txtName.getText().length() > 2 && (Pattern.matches(".*[\\p{L} .'-],[\\p{L} .'-].*", txtName.getText()))) {
            
           
            String[] _name = txtName.getText().split(",");
            String fname = _name[0];
            String lname = _name[1];
            if(fname.replace(" ", "").length()>=2 && lname.replace(" ", "").length()>=2){
                lblname.setText("Save");
                lblName.setIcon(good);
               
            }else{
                lblname.setText("Change");
                 lblName.setIcon(warning);
            }

        } else {
            lblname.setText("Change");
            lblName.setIcon(warning);
        }
    }//GEN-LAST:event_txtNameKeyReleased

    private void txtUserNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyReleased
          if (txtUserName.getText().length()>6 && (Pattern.matches("^[a-zA-Z0-9]*$", txtUserName.getText()))) {
            lblusername.setText("Save");
           
            lblUserName.setIcon(good);
        }else{
             lblusername.setText("Change");
             lblUserName.setIcon(warning);
        }
    }//GEN-LAST:event_txtUserNameKeyReleased

    private void txtEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyReleased
         String email = txtEmail.getText();
        String EMAIL_PATTERN
                = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (Pattern.matches(EMAIL_PATTERN,email)) {
            lblemail.setText("Save");
            lblEmail.setIcon(good);
        } else {
           lblemail.setText("Change");
            lblEmail.setIcon(warning);
        }
    }//GEN-LAST:event_txtEmailKeyReleased

    private void ftxtContactnumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftxtContactnumKeyReleased
        
        String str =ftxtContactnum.getText();
        str = str.replaceAll("[^\\d]", "");
        
        ftxtContactnum.setText(str);
        
        int len = str.length();
       
        
        if (len < 7) {
            lblcontactnum.setText("Change");
            lblContact.setIcon(warning);
        } else {
            lblcontactnum.setText("Save");
            lblContact.setIcon(good);
        }
    }//GEN-LAST:event_ftxtContactnumKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        if(txtPassword.getText().length()>6){
            lblpassword.setText("Save");
            lblPassword.setIcon(good);
            
        }else{
            lblpassword.setText("Change");
            lblPassword.setIcon(warning);
        }
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void txtNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNameFocusLost
       
    }//GEN-LAST:event_txtNameFocusLost

    private void ftxtContactnumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftxtContactnumKeyPressed
       
    }//GEN-LAST:event_ftxtContactnumKeyPressed

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        
    }//GEN-LAST:event_jTable1MouseEntered

    private void txtNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNameFocusGained
       txtName.setToolTipText("Enter new Name in this format: Firstname, LastName(note must be sepated with coma)");
    }//GEN-LAST:event_txtNameFocusGained

    private void lblchangeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblchangeMouseClicked
       uploadImage();
    }//GEN-LAST:event_lblchangeMouseClicked

    private void lblchangeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblchangeMouseEntered
        changeLabelChangeColorEntered(lblchange);
    }//GEN-LAST:event_lblchangeMouseEntered

    private void lblchangeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblchangeMouseExited
         changeLabelChangeColorExit(lblchange);
    }//GEN-LAST:event_lblchangeMouseExited

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if(user_id <0){
            JOptionPane.showMessageDialog(null, "Please Select an item");
        }else{
            if(user_id ==1){
                JOptionPane.showMessageDialog(null, "Restricted");
            }else{
                 int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + txtName.getText() + "?", "Delete", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                 deleteUser();
                 getAllData();
            }
              
            }
            
        }
        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void lblPromoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPromoteMouseClicked
        String str;
        String title ;
        String value;
        if(role.equals("User")){
            title ="Promote";
            str= "Promote "+name+" to Admin?";
            value = "Admin";
        }else{
             title ="Demote";
            str= "Demote "+name+" as User?";
            value ="User";
        }
        int reply = JOptionPane.showConfirmDialog(null, str, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
             updateField( "tbl_userinfo", "user_role",  value);
              JOptionPane.showMessageDialog(null, name+" was "+title+"d"+" to "+value);
        }
    }//GEN-LAST:event_lblPromoteMouseClicked

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JFormattedTextField ftxtContactnum;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblContact;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNote;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPromote;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblchange;
    private javax.swing.JLabel lblcontactnum;
    private javax.swing.JLabel lblemail;
    private javax.swing.JLabel lblname;
    private javax.swing.JLabel lblpassword;
    private javax.swing.JLabel lblusername;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables

//DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
//
//    Border padding = BorderFactory.createEmptyBorder(0, 10, 0, 10);
//    @Override
//    public Component getTableCellRendererComponent(JTable table,
//            Object value, boolean isSelected, boolean hasFocus,
//            int row, int column) {
//        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
//                row, column);
//        setBorder(BorderFactory.createCompoundBorder(getBorder(), padding));
//        return this;
//    }
//
//};
    private void showChangeLabel(boolean isShow){
        lblname.setVisible(isShow);
        lblusername.setVisible(isShow);
        lblcontactnum.setVisible(isShow);
        lblemail.setVisible(isShow);
        lblpassword.setVisible(isShow);
    }
    private void backToDefault(){
        textFieldReset(txtName, name);
        textFieldReset(txtUserName, userName);
        textFieldReset(txtEmail, email);
        textFieldReset(txtPassword, password);
        textFieldReset(ftxtContactnum, contact);
        
        changeLabelDefault(lblname, lblName);
        changeLabelDefault(lblusername, lblUserName);
        changeLabelDefault(lblemail, lblEmail);
        changeLabelDefault(lblcontactnum, lblContact);
        changeLabelDefault(lblpassword, lblPassword);
        
    }
    
    private void textFieldReset(JTextField tf, String str){
       tf.setText(str);
       tf.setEditable(false);
       tf.setBackground(Color.WHITE);
       tf.setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
    }
    private void changeLabelMouseClick(JTextField tf) {
        
        textFieldReset(txtName, name);
        textFieldReset(txtUserName, userName);
        textFieldReset(txtEmail, email);
        textFieldReset(txtPassword, password);
        textFieldReset(ftxtContactnum, contact);
        
        
        tf.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
        tf.setBackground(new Color(153, 153, 153));
        tf.setEditable(true);
        tf.requestFocus();
    }
    
    private String extract(String str){
        if(str.contains("Save")){
            return "Save";
        }else{
            return "Change";
        }
    }
    
     private void changeLabelDefault(JLabel label, JLabel label2) {
       
       
        label.setText("Change");
       label.setForeground(new Color(0, 51, 153));
        
        label2.setIcon(null);
       
    }

    private void changeLabelChangeColorEntered(JLabel label) {
       
        String str = label.getText();
        label.setText("<html><u>"+str+"</u></html>");
        label.setForeground(Color.red);
      
       
    }

    private void changeLabelChangeColorExit(JLabel label) {
        String str = extract(label.getText());
        
        
        label.setText(str);
        label.setForeground(new Color(0, 51, 153));
    }

    private void changColorEntered(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(51, 51, 51));
    }

    private void changColorExit(JButton button) {
        button.setBackground(new Color(51, 51, 51));
        button.setForeground(Color.WHITE);
    }

    void alternateCellColor() {

        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 1 ? new Color(137, 162, 201) : Color.WHITE);
                return c;
            }
        });
    }

    void modifyTable() {

        //remove header
        jTable1.setTableHeader(null);

        //remove the first column
        jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));

        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(300);
        columnModel.getColumn(1).setPreferredWidth(100);

        jTable1.setOpaque(false);

        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);

        alternateCellColor();

    }

    void tableDesign() {

        Object[] columns = {"Id", "Name", "role"};
        model = new DefaultTableModel(null, columns) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        
        jTable1.setSelectionForeground(Color.red);


    }
    public void promoteDemoteButton(){
   
             lblPromote.setVisible(true);
            if (role.equals("Admin")) {
                lblPromote.setToolTipText("Demote to User");
                lblPromote.setIcon(demote);
            } else {
                lblPromote.setToolTipText("Set as Admin");
                
                lblPromote.setIcon(promote);
            }
        
        
    }

    void loadDefault() {
        ImageIcon ii = new ImageIcon(this.getClass().getClassLoader().getResource("img/defaultUser.gif"));
        Image img = ii.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
        lblImage.setIcon(new ImageIcon(img));
    }

    
}
