/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Frames.mini;

/**
 *
 * @author padilla
 */

import Classes.DbConnection;
import Classes.LogsClass;
import Classes.SavePhotoToFolder;
import Classes.User;
import Classes.Directories;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Color;
import java.awt.Image;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AddNewItem extends javax.swing.JFrame {

    /**
     * Creates new form AddNewItem
     */
    Connection conn=null;
    ImageIcon warning = new ImageIcon(this.getClass().getClassLoader().getResource("Icons/warning.png"));
    ImageIcon good = new ImageIcon(this.getClass().getClassLoader().getResource("Icons/check.png"));
    
    boolean name;
    boolean category;
    boolean price; 
    boolean stocks;
    
    File file;
    //linux
    String path = Directories.OS.LINUX.getPath()+"Maxilife/img/items/Default.jpg";
    
    //windows
    //String path = Directories.OS.WINDOWS.getPath()+"Maxilife/img/items/Default.jpg";
    
    User user;
    
  //  LogsClass log = new LogsClass();
    
    public AddNewItem() {
        conn = DbConnection.dbConnect();
        initComponents();
        loadDefault();
        
       
    }
    public void setUser(User user){
        this.user = user;
    }
    
    private void generateItemCode(){
        
        if (category && name) {
            int hc = txtName.getText().hashCode();
            System.out.println(hc);
            String hashStr = String.valueOf(hc).substring(String.valueOf(hc).length() - 5, String.valueOf(hc).length());

            String prodIdpart1 = "";
            if (cmbCategory.getSelectedItem().equals("MEDICINE SUPPLY")) {
                prodIdpart1 = "MS1";
            } else {
                prodIdpart1 = "MS2";
            }
            txtId.setText( prodIdpart1 + "-" + hashStr);;
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

            } else {

                ImageIcon ii = new ImageIcon(path);
                Image img = ii.getImage().getScaledInstance(190, 190, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));

                // updateField("tbl_items", "item_image", value);
            }

        }
    }
        
    private void insert(){
        String sql1 = "insert into tbl_items(item_code, item_name, item_category, item_image) values(?,?,?,?)";
        String sql2 = "insert into tbl_iteminfo(item_code, item_price, item_stock) values(?,?,?)";
        
        String id = txtId.getText();
        String _name = txtName.getText();
        String _category = cmbCategory.getSelectedItem().toString();
        String _price = txtPrice.getText();
        String _stocks = txtStocks.getText();
        try{
            PreparedStatement pst = conn.prepareStatement(sql1);
            
            String str = insertImagePath();
            
            
            pst.setString(1, id);
            pst.setString(2, _name);
            pst.setString(3, _category);
            pst.setString(4, str);
            pst.executeUpdate();
            
            pst = conn.prepareStatement(sql2);
            pst.setString(1, id);
            pst.setString(2, _price.replace("P", ""));
            pst.setString(3, _stocks);
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "New Item Added");
            loadDefault();
        }catch(Exception e){
            System.out.println("error "+e);
        }
    }
        
    private String insertImagePath(){
        SavePhotoToFolder spf = new SavePhotoToFolder();
        //get extension 

        String[] str = path.split("\\.");
        String ext = str[str.length - 1];
        System.out.println(ext);
        String[] id = txtId.getText().split("-");

        String fileName = "med-image" + id[1];
        return spf.saveToFolder(file, fileName, ext);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtStocks = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        cmbCategory = new javax.swing.JComboBox<>();
        panelSave = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanelExport = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblStocks = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblCategory = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(597, 400));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblImage.setBackground(new java.awt.Color(255, 255, 255));
        lblImage.setOpaque(true);
        jPanel2.add(lblImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 190, 190));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 200, 200));

        jLabel1.setFont(new java.awt.Font("URW Gothic L", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Stocks:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 100, 25));

        jLabel2.setFont(new java.awt.Font("URW Gothic L", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Name: ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 100, 25));

        jLabel3.setFont(new java.awt.Font("URW Gothic L", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Category:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 100, 25));

        jLabel4.setFont(new java.awt.Font("URW Gothic L", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Price:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 100, 25));

        txtStocks.setText("0");
        txtStocks.setBorder(new javax.swing.border.MatteBorder(null));
        txtStocks.setMargin(new java.awt.Insets(0, 30, 0, 0));
        txtStocks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStocksKeyReleased(evt);
            }
        });
        jPanel1.add(txtStocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 180, 25));

        txtName.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNameFocusLost(evt);
            }
        });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNameKeyReleased(evt);
            }
        });
        jPanel1.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 180, 25));

        txtPrice.setBorder(new javax.swing.border.MatteBorder(null));
        txtPrice.setMargin(new java.awt.Insets(0, 30, 0, 0));
        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPriceKeyReleased(evt);
            }
        });
        jPanel1.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 180, 25));

        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-", "MEDICAL SUPPLY", "MEDICINE SUPPLY" }));
        cmbCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCategoryItemStateChanged(evt);
            }
        });
        cmbCategory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbCategoryFocusLost(evt);
            }
        });
        cmbCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbCategoryMouseClicked(evt);
            }
        });
        jPanel1.add(cmbCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 180, 25));

        panelSave.setBackground(new java.awt.Color(5, 192, 12));
        panelSave.setForeground(new java.awt.Color(51, 153, 0));
        panelSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelSaveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelSaveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelSaveMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelSaveMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelSaveMouseReleased(evt);
            }
        });
        panelSave.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setBackground(new java.awt.Color(51, 51, 51));
        jLabel6.setFont(new java.awt.Font("AnjaliOldLipi", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Save");
        panelSave.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 5, 50, 18));

        jPanel1.add(panelSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 80, 25));

        txtId.setEditable(false);
        txtId.setBackground(new java.awt.Color(255, 255, 255));
        txtId.setBorder(new javax.swing.border.MatteBorder(null));
        txtId.setMargin(new java.awt.Insets(0, 30, 0, 0));
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 180, 25));

        jLabel5.setFont(new java.awt.Font("URW Gothic L", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Item Code:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 100, 25));

        jPanelExport.setBackground(new java.awt.Color(0, 51, 102));
        jPanelExport.setForeground(new java.awt.Color(51, 153, 0));
        jPanelExport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelExportMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelExportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelExportMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelExportMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanelExportMouseReleased(evt);
            }
        });
        jPanelExport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Umpush", 1, 30)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/upload-3-24.png"))); // NOI18N
        jLabel8.setText("+");
        jPanelExport.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 2, 20, 20));

        jLabel9.setFont(new java.awt.Font("AnjaliOldLipi", 0, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Upload");
        jPanelExport.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 5, 50, 18));

        jPanel1.add(jPanelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 250, 80, 25));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/close.png"))); // NOI18N
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 0, -1, -1));

        lblStocks.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblStocks.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lblStocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 160, 24, 24));

        lblName.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblName.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 70, 24, 24));

        lblCategory.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblCategory.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lblCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 100, 24, 24));

        lblPrice.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        lblPrice.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lblPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 130, 24, 24));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 300));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void panelSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMouseClicked
         if(name && category && stocks && price){
            insert();
  //          log.insertToLog(user.getUserName(), "Add new item("+txtName.getText()+") to the Inventory");
         }else{
             JOptionPane.showMessageDialog(null, "Please Fill All neccessary data");
         }
    }//GEN-LAST:event_panelSaveMouseClicked

    private void panelSaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMouseEntered
        if(name && category && stocks && price){
            panelSave.setBackground(new Color(5, 255, 0));
        }
        
    }//GEN-LAST:event_panelSaveMouseEntered

    private void panelSaveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMouseExited
       
        if(name && category && stocks && price){
             panelSave.setBackground(new Color(5, 192, 12));
        }
      
    }//GEN-LAST:event_panelSaveMouseExited

    private void panelSaveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMousePressed
        //jPanelAdd.setBackground(new Color(5,101,3));
         if(name && category && stocks && price){
            panelSave.setBackground(new Color(5, 192, 12)); 
        }
        
    }//GEN-LAST:event_panelSaveMousePressed

    private void panelSaveMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMouseReleased
         if(name && category && stocks && price){
              panelSave.setBackground(new Color(5, 255, 0));
        }
       
    }//GEN-LAST:event_panelSaveMouseReleased

    private void jPanelExportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMouseEntered
        jPanelExport.setBackground(new Color(0, 141, 168));
    }//GEN-LAST:event_jPanelExportMouseEntered

    private void jPanelExportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMouseExited
        jPanelExport.setBackground(new Color(0, 51, 102));
    }//GEN-LAST:event_jPanelExportMouseExited

    private void jPanelExportMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMousePressed
        jPanelExport.setBackground(new Color(0, 51, 102));
    }//GEN-LAST:event_jPanelExportMousePressed

    private void jPanelExportMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMouseReleased
        jPanelExport.setBackground(new Color(0, 141, 168));
    }//GEN-LAST:event_jPanelExportMouseReleased

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked

        dispose();
    }//GEN-LAST:event_jLabel10MouseClicked

    private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased
         if (txtName.getText().length()>3 ) {
            
            lblName.setIcon(good);
            name = true;
            generateItemCode();
        }else{
           
             lblName.setIcon(warning);
             price = false;
             txtId.setText("");
        } 
        saveButton_Active();
        
    }//GEN-LAST:event_txtNameKeyReleased

    private void txtPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyReleased
        txtPrice.setHorizontalAlignment(txtPrice.RIGHT);
        String str = txtPrice.getText();
        
        //str = str.replaceAll("[^\\d]", "");
        str = str.replaceAll( "[^0-9]", "");
        String res ="";
       
        if(str.length()>=2 ){
            //public String addChar(String str, char ch, int position) {
             res=str.substring(0, str.length()-2) + "." + str.substring(str.length()-2);
                txtPrice.setText("P"+res);
        }else{
            txtPrice.setText("P"+str);
        }
        
        if(txtPrice.getText().length()<5){
            lblPrice.setIcon(warning);
            price = false;
        }else{
            lblPrice.setIcon(good);
            price = true;
        }
        System.out.println(txtPrice.getText().replace("P", ""));
         saveButton_Active();
    }//GEN-LAST:event_txtPriceKeyReleased

    private void txtStocksKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStocksKeyReleased
        txtStocks.setHorizontalAlignment(txtStocks.RIGHT);
        String str = txtStocks.getText();

        str = str.replaceAll("[^0-9]", "");

        if (txtStocks.getText().length() == 0) {

            lblStocks.setIcon(warning);
            txtStocks.setText("0");
            stocks = false; 
        } else {
            if (str.charAt(0) == '0' && str.length() > 1) {
                System.out.println("hello");
                str = str.replace("0", "");
            }
            lblStocks.setIcon(good);
            stocks = true; 
            txtStocks.setText(str);

        }
         saveButton_Active();


        
    }//GEN-LAST:event_txtStocksKeyReleased

    private void txtNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNameFocusLost
       String sql = "Select item_name from tbl_items where item_name ='"+txtName.getText()+"'";
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "Name already Exist");
                
                lblName.setIcon(warning);
                name =false;
                
            }else{
                 
                lblName.setIcon(good);
                name =true;
            }
            
            
        }catch(Exception e ){
            System.out.println(e);
        }
    }//GEN-LAST:event_txtNameFocusLost

    private void cmbCategoryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbCategoryFocusLost
        
    }//GEN-LAST:event_cmbCategoryFocusLost

    private void cmbCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCategoryMouseClicked

    }//GEN-LAST:event_cmbCategoryMouseClicked

    private void cmbCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCategoryItemStateChanged
        if (cmbCategory.getSelectedIndex() == 0) {
            lblCategory.setIcon(warning);
            category = false;
            txtId.setText("");
        } else {
            lblCategory.setIcon(good);
            category = true;
        }
        generateItemCode();
         saveButton_Active();
          
    }//GEN-LAST:event_cmbCategoryItemStateChanged

    private void jPanelExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMouseClicked
        if(txtId.getText().length()==0){
            JOptionPane.showMessageDialog(null, "Must generate item code first");
        }else{
            uploadImage();
        }
        
    }//GEN-LAST:event_jPanelExportMouseClicked

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
            java.util.logging.Logger.getLogger(AddNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddNewItem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelExport;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblStocks;
    private javax.swing.JPanel panelSave;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtStocks;
    // End of variables declaration//GEN-END:variables
    private void loadDefault(){
        txtId.setText("");
        txtName.setText("");
        cmbCategory.setSelectedIndex(0);
        txtPrice.setText("");
        txtStocks.setText("0");
        
        //lblImage.setVisible(false);
        lblName.setVisible(false);
        lblPrice.setVisible(false);
        lblStocks.setVisible(false);
        
         name = false;
         category = false;
         price =false ;
         stocks =false;

        
         txtName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
         txtId.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        
        txtPrice.setHorizontalAlignment(txtPrice.RIGHT);
        txtStocks.setHorizontalAlignment(txtStocks.RIGHT);
        
        
        ImageIcon ii = new ImageIcon(this.getClass().getClassLoader().getResource("img/med.jpg"));
        Image img = ii.getImage().getScaledInstance(190, 190, Image.SCALE_SMOOTH);
        
        lblImage.setIcon(new ImageIcon(img));
        
        
        panelSave.setEnabled(false);
        panelSave.setBackground(Color.GRAY);
    }
    
    private void saveButton_Active(){
        if(name && category && stocks && price){
            panelSave.setBackground(new Color(5, 255, 0));
        }else{
            panelSave.setBackground(Color.GRAY);
        }
    }
   
}
