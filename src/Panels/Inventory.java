/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panels;

import Classes.DbConnection;
import Classes.ExportToExcel;
import Classes.LogsClass;
import Classes.SavePhotoToFolder;
import Classes.User;
import Frames.mini.AddNewItem;
import Frames.mini.Logs;
import Frames.MainFrame;
import Frames.mini.RestrockFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;

import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author padilla
 */
public class Inventory extends javax.swing.JPanel {

    /**
     * Creates new form Inventory
     */

    DefaultTableModel model;
    Connection conn = null;
    
    User user;
    
    LogsClass log = new LogsClass();

    File file;
    String path = null;

    String fetchAll = "Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code";

    boolean sortAscending = true;

    HashSet<String> hs = new HashSet<>();

    public Inventory() {
        conn = DbConnection.dbConnect();

        initComponents();

        init();

    }
    public void setUser(User user){
     this.user=user;
        System.out.println(user.getUserName());
    }

    private void init() {
       
        tableDesign();
       displayAllItem(fetchAll);

        modifyTable();

        loadDefault();
        

    }

    private void displayCritical(int max) {
        String sql = "Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code where tbl_iteminfo.item_stock <" + max + " order by tbl_iteminfo.item_stock";
        displayAllItem(sql);
    }

    public void displayAllItem(String sql) {
        // String sql = "Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code";
        model.setRowCount(0);
        int last = 0;
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.last();
            last = rs.getRow();
            if (rs.getRow() == 0) {

                lblTableLabel.setVisible(true);
                lblTableLabel.setText("No Item found");
                System.out.println("No data");
            } else {
                lblTableLabel.setVisible(false);
            }
            
            rs.beforeFirst();
            Object[] items = new Object[4];
            int i = 0;
            while (rs.next()) {
                String _id = rs.getString("item_code");
                String _name = rs.getString("item_name");
                String _category = rs.getString("item_category");
                float _price = rs.getFloat("item_price");
                String _stock = rs.getString("item_stock");

                hs.add(_category);
                String p = "P" + String.format("%.02f", _price);

                items[0] = _id;
                items[1] = _name;
                items[2] = p;
                items[3] = _stock;

                model.addRow(items);

                i++;
            }
            jTable1.setModel(model);
        } catch (Exception e) {
            System.out.println(e);
        }
       
    }

    private void displayImage(String path) {
        File dir = new File(path);
        ImageIcon ii = new ImageIcon(path);
        try {
            if (!dir.exists()) {
                ii = new ImageIcon(this.getClass().getClassLoader().getResource("img/med.jpg"));

            }
            Image img = ii.getImage().getScaledInstance(190, 190, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));

        } catch (Exception e) {

        }

    }

    private void selectDataFromTable() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        int row = jTable1.getSelectedRow();
        String selected = jTable1.getModel().getValueAt(row, 0).toString();
        System.out.println(selected);

        String sql = "Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code= tbl_iteminfo.item_code where tbl_items.item_code='" + selected + "'";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                float _price = rs.getFloat("item_price");
                String p = "P" + String.format("%.02f", _price);

                lblId.setText(rs.getString("item_code"));
                lblName.setText(rs.getString("item_name"));
                lblCategory.setText(rs.getString("item_category"));
                lblPrice.setText(p);
                lblStock.setText(rs.getString("item_stock"));

                displayImage(rs.getString("item_image"));
            }
           
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void uploadImage() {

        SavePhotoToFolder spf = new SavePhotoToFolder();
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileExtension = new FileNameExtensionFilter("PNG JPG AND JPEG", "png", "jpeg", "jpg");
        fileChooser.addChoosableFileFilter(fileExtension);

        int load = fileChooser.showOpenDialog(null);

        if (load == fileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();

            path = file.getAbsolutePath();

            //get extension
            String[] str = path.split("\\.");
            String ext = str[str.length - 1];
           
            
            String[] id = lblId.getText().split("-");

            String fileName = "med-image" + id[1];

            double size = (double) file.length() / (1024 * 1024);

            if (size > 1) {
                JOptionPane.showMessageDialog(null, "File is too big");

            } else {

                ImageIcon ii = new ImageIcon(path);
                Image img = ii.getImage().getScaledInstance(190, 190, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));

                String value = spf.saveToFolder(file, fileName, ext);
                updateField("tbl_items", "item_image", value);

                JOptionPane.showMessageDialog(null, "Update Success");
            }

        }
    }

    private void updateField(String entity, String attribute, String value) {
        String sql = "UPDATE " + entity + " SET " + attribute + " = '" + value + "' WHERE item_code = '" + lblId.getText() + "'";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();

        } catch (Exception e) {
            System.out.println("Error update" + e);
        }
        
        // displayAllItem("Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code where tbl_items.item_code ='"+ lblId.getText()+"'");
    }

    private void searchItemByName() {
        model.setRowCount(0);
        String name = txtSearch.getText();
        String sql = "Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code where item_name like '%" + name + "%'; ";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            rs.last();
            if (rs.getRow() == 0) {
                lblTableLabel.setVisible(true);
                lblTableLabel.setText("No Item found");
                System.out.println("No data");
            } else {
                lblTableLabel.setVisible(false);
            }

            rs.beforeFirst();

            Object[] items = new Object[4];
            while (rs.next()) {

                String _id = rs.getString("item_code");
                String _name = rs.getString("item_name");
                String _category = rs.getString("item_category");
                float _price = rs.getFloat("item_price");
                String _stock = rs.getString("item_stock");

                hs.add(_category);
                String p = "P" + String.format("%.02f", _price);

                items[0] = _id;
                items[1] = _name;
                items[2] = p;
                items[3] = _stock;

                model.addRow(items);
            }
            jTable1.setModel(model);
        } catch (Exception e) {

        }
    }

    private void deleteItem() {
        SavePhotoToFolder stf = new SavePhotoToFolder();
        String sql = "DELETE tbl_items, tbl_iteminfo FROM tbl_items INNER JOIN tbl_iteminfo ON tbl_items.item_code = tbl_iteminfo.item_code WHERE tbl_items.item_code = '" + lblId.getText() + "'";
        String getPathquery = "Select item_image from tbl_items WHERE item_code = '" + lblId.getText() + "'";
        try {
            PreparedStatement pst = conn.prepareStatement(getPathquery);
            ResultSet rs = pst.executeQuery();
            rs.next();
           
           
            stf.deletePhoto(rs.getString("item_image"));
            
            pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Item Deleted");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getAllTableData() {
        //  Object[] columnData = new Object[jTable1.getRowCount()];  // One entry for each row
        Object[][] tableData = new Object[jTable1.getRowCount()][4];

        tableData[0][0] = "Id";
        tableData[0][1] = "Name";
        tableData[0][2] = "Price";
        tableData[0][3] = "Stocks";
        for (int i = 1; i < jTable1.getRowCount(); i++) {  // Loop through the rows

            for (int j = 0; j < 4; j++) {
                tableData[i][j] = jTable1.getValueAt(i, j);
            }

        }

        createExcelFile(tableData);

//        print(tableData);
        //System.out.println(Arrays.toString(columnData));
    }

    void createExcelFile(Object[][] data) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LocalDateTime now = LocalDateTime.now();
        String fileName = dtf.format(now) + "_inventory";

        ExportToExcel export = new ExportToExcel();
        String path = "/home/padilla/Maxilife/spreadsheets/" + fileName + ".csv";
        try {
            export.exportDataToCSV(path, data);
            //export.exportDataToExcel(path, data);
            JOptionPane.showMessageDialog(null, "File Saved on " + path);
        } catch (IOException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void print(Object[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(arr[i][j] + " ");
            }
            System.out.println("");
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

        lblTableLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        lblChange = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        txtStock = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        lblCategory = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblEditStock = new javax.swing.JLabel();
        lblEditPrice = new javax.swing.JLabel();
        lblEditName = new javax.swing.JLabel();
        lblEditCategory = new javax.swing.JLabel();
        jPanelExport = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbSort = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jPanelAdd = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanelDelete = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTableLabel.setFont(new java.awt.Font("Manjari Thin", 1, 18)); // NOI18N
        lblTableLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(lblTableLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 250, 40));

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

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 680, 450));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/magnifying-glass-3-32.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 30, 30));

        txtSearch.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });
        jPanel2.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 2, 235, 25));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 30));

        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel3MouseExited(evt);
            }
        });
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblChange.setFont(new java.awt.Font("URW Gothic L", 0, 10)); // NOI18N
        lblChange.setForeground(new java.awt.Color(0, 0, 153));
        lblChange.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblChange.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblChangeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblChangeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblChangeMouseExited(evt);
            }
        });
        jPanel3.add(lblChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 45, 15));

        lblImage.setBackground(new java.awt.Color(204, 204, 204));
        lblImage.setOpaque(true);
        jPanel3.add(lblImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 190, 190));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 70, 200, 200));

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setFont(new java.awt.Font("AnjaliOldLipi", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Restock");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 480, 90, 25));

        txtStock.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtStock.setBorder(new javax.swing.border.MatteBorder(null));
        txtStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockActionPerformed(evt);
            }
        });
        txtStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStockKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStockKeyReleased(evt);
            }
        });
        jPanel1.add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 160, 20));

        txtPrice.setBorder(new javax.swing.border.MatteBorder(null));
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPriceKeyReleased(evt);
            }
        });
        jPanel1.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, 160, 20));

        txtName.setBorder(new javax.swing.border.MatteBorder(null));
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
        });
        jPanel1.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 160, 20));

        lblName.setBackground(new java.awt.Color(153, 153, 153));
        lblName.setFont(new java.awt.Font("URW Gothic", 0, 11)); // NOI18N
        lblName.setOpaque(true);
        jPanel1.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 160, 20));

        jLabel10.setFont(new java.awt.Font("URW Gothic L", 1, 11)); // NOI18N
        jLabel10.setText("Name:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 70, 20));

        jLabel11.setFont(new java.awt.Font("URW Gothic L", 1, 11)); // NOI18N
        jLabel11.setText("Category");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 70, 20));

        cmbCategory.setBorder(new javax.swing.border.MatteBorder(null));
        cmbCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCategoryItemStateChanged(evt);
            }
        });
        jPanel1.add(cmbCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 160, 20));

        lblCategory.setBackground(new java.awt.Color(153, 153, 153));
        lblCategory.setFont(new java.awt.Font("URW Gothic", 0, 11)); // NOI18N
        lblCategory.setOpaque(true);
        jPanel1.add(lblCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 160, 20));

        jLabel13.setFont(new java.awt.Font("URW Gothic L", 1, 11)); // NOI18N
        jLabel13.setText("Price");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 70, 20));

        lblPrice.setBackground(new java.awt.Color(153, 153, 153));
        lblPrice.setFont(new java.awt.Font("URW Gothic", 0, 11)); // NOI18N
        lblPrice.setOpaque(true);
        jPanel1.add(lblPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, 160, 20));

        jLabel15.setFont(new java.awt.Font("URW Gothic L", 1, 11)); // NOI18N
        jLabel15.setText("Stock");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 70, 20));

        lblStock.setBackground(new java.awt.Color(255, 255, 255));
        lblStock.setFont(new java.awt.Font("URW Gothic", 0, 11)); // NOI18N
        lblStock.setOpaque(true);
        jPanel1.add(lblStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 160, 20));

        jLabel17.setFont(new java.awt.Font("URW Gothic L", 1, 11)); // NOI18N
        jLabel17.setText("Product ID:");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 70, 20));

        lblId.setBackground(new java.awt.Color(255, 255, 255));
        lblId.setFont(new java.awt.Font("URW Gothic", 0, 11)); // NOI18N
        lblId.setOpaque(true);
        jPanel1.add(lblId, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 160, 20));

        lblEditStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editIcon.png"))); // NOI18N
        lblEditStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEditStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEditStockMouseClicked(evt);
            }
        });
        jPanel1.add(lblEditStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 410, 16, 16));

        lblEditPrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editIcon.png"))); // NOI18N
        lblEditPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEditPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEditPriceMouseClicked(evt);
            }
        });
        jPanel1.add(lblEditPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 380, 16, 16));

        lblEditName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editIcon.png"))); // NOI18N
        lblEditName.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEditName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEditNameMouseClicked(evt);
            }
        });
        jPanel1.add(lblEditName, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 16, 16));

        lblEditCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editIcon.png"))); // NOI18N
        lblEditCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblEditCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEditCategoryMouseClicked(evt);
            }
        });
        jPanel1.add(lblEditCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, 16, 16));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 0, 270, 550));

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

        jLabel3.setFont(new java.awt.Font("Umpush", 1, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Data-Export-20.png"))); // NOI18N
        jLabel3.setText("+");
        jPanelExport.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 2, 19, 20));

        jLabel4.setFont(new java.awt.Font("AnjaliOldLipi", 0, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Export");
        jPanelExport.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 5, 50, 18));

        add(jPanelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 80, 25));

        cmbSort.setBackground(new java.awt.Color(255, 255, 255));
        cmbSort.setFont(new java.awt.Font("URW Gothic L", 0, 12)); // NOI18N
        cmbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Added Today", "Critical Stocks" }));
        cmbSort.setBorder(new javax.swing.border.MatteBorder(null));
        cmbSort.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSortItemStateChanged(evt);
            }
        });
        cmbSort.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbSortMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cmbSortMouseReleased(evt);
            }
        });
        add(cmbSort, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 170, 25));

        jLabel2.setFont(new java.awt.Font("URW Gothic L", 0, 14)); // NOI18N
        jLabel2.setText("Sort:");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, 60, 25));

        jPanelAdd.setBackground(new java.awt.Color(5, 192, 12));
        jPanelAdd.setForeground(new java.awt.Color(51, 153, 0));
        jPanelAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelAddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelAddMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelAddMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelAddMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanelAddMouseReleased(evt);
            }
        });
        jPanelAdd.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Umpush", 1, 30)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("+");
        jPanelAdd.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 2, 19, 20));

        jLabel6.setBackground(new java.awt.Color(51, 51, 51));
        jLabel6.setFont(new java.awt.Font("AnjaliOldLipi", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Add New");
        jPanelAdd.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 5, 60, 18));

        add(jPanelAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 80, 25));

        jPanelDelete.setBackground(new java.awt.Color(204, 0, 0));
        jPanelDelete.setForeground(new java.awt.Color(51, 153, 0));
        jPanelDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelDeleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelDeleteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelDeleteMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanelDeleteMouseReleased(evt);
            }
        });
        jPanelDelete.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Umpush", 1, 30)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/delete-24.png"))); // NOI18N
        jLabel7.setText("+");
        jPanelDelete.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 2, 19, 20));

        jLabel8.setFont(new java.awt.Font("AnjaliOldLipi", 0, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Delete");
        jPanelDelete.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 5, 60, 18));

        add(jPanelDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 40, 80, 25));

        jLabel9.setBackground(new java.awt.Color(153, 153, 153));
        jLabel9.setFont(new java.awt.Font("Chandas", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("STOCKS");
        jLabel9.setToolTipText("");
        jLabel9.setOpaque(true);
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 78, 150, 20));

        jLabel12.setBackground(new java.awt.Color(153, 153, 153));
        jLabel12.setFont(new java.awt.Font("Chandas", 1, 14)); // NOI18N
        jLabel12.setText("ITEM NAME");
        jLabel12.setToolTipText("");
        jLabel12.setOpaque(true);
        add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 78, 387, 20));

        jLabel14.setBackground(new java.awt.Color(153, 153, 153));
        jLabel14.setFont(new java.awt.Font("Chandas", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("PRICE");
        jLabel14.setToolTipText("");
        jLabel14.setOpaque(true);
        add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(388, 78, 138, 20));
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        lblChange.setVisible(true);
        lblChange.setText("Change");
        selectDataFromTable();
        HideShowEditLabel(true);

        txtPrice.setHorizontalAlignment(txtPrice.RIGHT);

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered

    }//GEN-LAST:event_jTable1MouseEntered

    private void jPanelAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelAddMouseEntered
        jPanelAdd.setBackground(new Color(5, 255, 0));
    }//GEN-LAST:event_jPanelAddMouseEntered

    private void jPanelAddMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelAddMouseExited
        jPanelAdd.setBackground(new Color(5, 192, 12));
    }//GEN-LAST:event_jPanelAddMouseExited

    private void jPanelExportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMouseEntered
        jPanelExport.setBackground(new Color(0, 141, 168));
    }//GEN-LAST:event_jPanelExportMouseEntered

    private void jPanelExportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMouseExited
        jPanelExport.setBackground(new Color(0, 51, 102));
    }//GEN-LAST:event_jPanelExportMouseExited

    private void jPanelDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDeleteMouseEntered
        jPanelDelete.setBackground(new Color(255, 39, 9));
    }//GEN-LAST:event_jPanelDeleteMouseEntered

    private void jPanelDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDeleteMouseExited
        jPanelDelete.setBackground(new Color(204, 0, 0));
    }//GEN-LAST:event_jPanelDeleteMouseExited

    private void jPanelAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelAddMouseClicked
        AddNewItem add = new AddNewItem();
        add.setUser(user);
        add.show();
    }//GEN-LAST:event_jPanelAddMouseClicked

    private void jPanelAddMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelAddMousePressed
        //jPanelAdd.setBackground(new Color(5,101,3));
        jPanelAdd.setBackground(new Color(5, 192, 12));
    }//GEN-LAST:event_jPanelAddMousePressed

    private void jPanelAddMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelAddMouseReleased
        jPanelAdd.setBackground(new Color(5, 255, 0));
    }//GEN-LAST:event_jPanelAddMouseReleased

    private void jPanelExportMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMousePressed
        jPanelExport.setBackground(new Color(0, 51, 102));
    }//GEN-LAST:event_jPanelExportMousePressed

    private void jPanelExportMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMouseReleased
        jPanelExport.setBackground(new Color(0, 141, 168));
    }//GEN-LAST:event_jPanelExportMouseReleased

    private void jPanelDeleteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDeleteMousePressed
        jPanelDelete.setBackground(new Color(204, 0, 0));
    }//GEN-LAST:event_jPanelDeleteMousePressed

    private void jPanelDeleteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDeleteMouseReleased
        jPanelDelete.setBackground(new Color(255, 39, 9));
    }//GEN-LAST:event_jPanelDeleteMouseReleased

    private void lblChangeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChangeMouseEntered
        lblChange.setText("<html><u>Change</u></html>");
    }//GEN-LAST:event_lblChangeMouseEntered

    private void lblChangeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChangeMouseExited
        lblChange.setText("Change");
    }//GEN-LAST:event_lblChangeMouseExited

    private void jPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseEntered
        lblChange.setText("Change");
    }//GEN-LAST:event_jPanel3MouseEntered

    private void jPanel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseExited
        lblChange.setText("");
    }//GEN-LAST:event_jPanel3MouseExited

    private void lblChangeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChangeMouseClicked
        uploadImage();


    }//GEN-LAST:event_lblChangeMouseClicked

    private void cmbSortItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSortItemStateChanged

        //   System.out.println(evt.getStateChange());

    }//GEN-LAST:event_cmbSortItemStateChanged

    private void lblEditNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEditNameMouseClicked
        hideShowComponents();
        txtName.setVisible(true);
        txtName.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        txtName.setText(lblName.getText());
        txtName.requestFocus();
    }//GEN-LAST:event_lblEditNameMouseClicked

    private void lblEditPriceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEditPriceMouseClicked
        hideShowComponents();
        txtPrice.setVisible(true);
        txtPrice.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        txtPrice.setText(lblPrice.getText());
        txtPrice.requestFocus();
    }//GEN-LAST:event_lblEditPriceMouseClicked

    private void lblEditCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEditCategoryMouseClicked
        hideShowComponents();
        cmbCategory.setVisible(true);
        cmbCategory.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));

        cmbCategory.requestFocus();
    }//GEN-LAST:event_lblEditCategoryMouseClicked

    private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            if (txtName.getText().replaceAll(" ", "").equals("")) {
                JOptionPane.showMessageDialog(null, "Field is Blank");
                txtName.requestFocus();

            } else {
                if (txtName.getText().replaceAll(" ", "").length() < 4) {
                    JOptionPane.showMessageDialog(null, "Please Enter Valid Name");
                } else {
                    updateField("tbl_items", "item_name", txtName.getText());
                    log.insertToLog(user.getUserName(), "Updated item name from "+lblName.getText()+" to "+txtName.getText());
                    lblName.setText(txtName.getText());
                    JOptionPane.showMessageDialog(null, "Success");

                }
            }

            //
               hideTextBox();
                    displayAllItem(fetchAll);
        searchItemByName();
        }
     
    }//GEN-LAST:event_txtNameKeyPressed

    private void txtPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {

            if (txtPrice.getText().replaceAll("P", "").length() < 4) {
                JOptionPane.showMessageDialog(null, "Please Follow Price format(P##.##)");
                txtPrice.requestFocus();
            } else {
                String _price = txtPrice.getText().replace("P", "");
                updateField("tbl_iteminfo", "item_price", _price);
                 log.insertToLog(user.getUserName(), "Updated "+lblName.getText()+" price from "+lblPrice.getText()+" to "+txtPrice.getText());
                JOptionPane.showMessageDialog(null, "Success");
                lblPrice.setText(txtPrice.getText());
            }
hideTextBox();
displayAllItem(fetchAll);
        searchItemByName();
        }
        
    }//GEN-LAST:event_txtPriceKeyPressed

    private void txtPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyReleased
        txtPrice.setHorizontalAlignment(txtPrice.RIGHT);
        String str = txtPrice.getText();

        //str = str.replaceAll("[^\\d]", "");
        str = str.replaceAll("[^0-9]", "");
        String res = "";

        if (str.length() >= 2) {
            //public String addChar(String str, char ch, int position) {
            res = str.substring(0, str.length() - 2) + "." + str.substring(str.length() - 2);
            txtPrice.setText("P" + res);
        } else {
            txtPrice.setText("P" + str);
        }


    }//GEN-LAST:event_txtPriceKeyReleased

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        searchItemByName();
        if (txtSearch.getText().length() == 0) {
            displayAllItem(fetchAll);
        }
    }//GEN-LAST:event_txtSearchKeyPressed

    private void jPanelDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDeleteMouseClicked
        if (lblName.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "No item Selected");
        } else {
            int reply = JOptionPane.showConfirmDialog(null, "Delete " + lblName.getText() + "?", "Delete", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                deleteItem();
                
                log.insertToLog(user.getUserName(), "Deleted "+lblName.getText());
                displayAllItem(fetchAll);
                searchItemByName();
                JOptionPane.showMessageDialog(null, "Item Deleted");
            }
        }

       
    }//GEN-LAST:event_jPanelDeleteMouseClicked

    private void jPanelExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelExportMouseClicked
        getAllTableData();
    }//GEN-LAST:event_jPanelExportMouseClicked

    private void cmbSortMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbSortMouseClicked

    }//GEN-LAST:event_cmbSortMouseClicked

    private void cmbSortMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbSortMouseReleased

    }//GEN-LAST:event_cmbSortMouseReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        RestrockFrame restock = new RestrockFrame();
        
        if(lblId.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Select an Item first");
        }else{
            restock.setDetails("Restock", lblId.getText(), lblStock.getText(), lblName.getText(), user);
            restock.setInstance(this);

            restock.loadDefault();
            restock.show();
        }

      
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cmbCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCategoryItemStateChanged
        hideTextBox();
        
        updateField("tbl_items", "item_Category", cmbCategory.getSelectedItem().toString());
       // log.insertToLog(user.getUserName(), "Updated "+lblName.getText()+" category to "+cmbCategory.getSelectedItem().toString());
    }//GEN-LAST:event_cmbCategoryItemStateChanged

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockActionPerformed

    private void txtStockKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockKeyPressed
           if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {

            if (txtStock.getText().length() ==0) {
                JOptionPane.showMessageDialog(null, "Please Enter a value");
                txtStock.requestFocus();
            } else {
                String _stock =txtStock.getText();
                updateField("tbl_iteminfo", "item_stock", _stock);
                
               log.insertToLog(user.getUserName(), "Adjusted "+lblName.getText()+" stock from "+lblStock.getText()+" to "+txtStock.getText());
                JOptionPane.showMessageDialog(null, "Success");
                lblStock.setText(txtStock.getText());
            }
hideTextBox();
displayAllItem(fetchAll);
        searchItemByName();
        }
        
    }//GEN-LAST:event_txtStockKeyPressed

    private void lblEditStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEditStockMouseClicked
        hideShowComponents();
        txtStock.setVisible(true);
        txtStock.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        txtStock.setText(lblStock.getText());
        txtStock.requestFocus();
    }//GEN-LAST:event_lblEditStockMouseClicked

    private void txtStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockKeyReleased
        txtStock.setHorizontalAlignment(txtPrice.RIGHT);
        String str = txtStock.getText();

        //str = str.replaceAll("[^\\d]", "");
        str = str.replaceAll("[^0-9]", "");
        txtStock.setText(str);
    }//GEN-LAST:event_txtStockKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbSort;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelAdd;
    private javax.swing.JPanel jPanelDelete;
    private javax.swing.JPanel jPanelExport;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblChange;
    private javax.swing.JLabel lblEditCategory;
    private javax.swing.JLabel lblEditName;
    private javax.swing.JLabel lblEditPrice;
    private javax.swing.JLabel lblEditStock;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblTableLabel;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
//    DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
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
//    

    void modifyTable() {

        //remove header
        jTable1.setTableHeader(null);

        //remove the first column
        //jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);

        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(50);
        columnModel.getColumn(3).setPreferredWidth(50);

        jTable1.setOpaque(false);

        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);

        alternateCellColor();

    }

    void alternateCellColor() {

        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                int align = DefaultTableCellRenderer.RIGHT;
                if (column == 1) {
                    align = DefaultTableCellRenderer.LEFT;
                }
                c.setBackground(row % 2 == 0 ? new Color(137, 162, 201) : Color.WHITE);
                ((DefaultTableCellRenderer) c).setHorizontalAlignment(align);
                return c;
            }
        });
    }

    void tableDesign() {

        Object[] columns = {"ProductCode", "Name", "Price", "Stock"};
        model = new DefaultTableModel(null, columns) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        jTable1.setSelectionForeground(Color.red);

    }

    public static class MyComboBoxUI extends BasicComboBoxUI {

        @Override
        protected void installDefaults() {
            super.installDefaults();
            LookAndFeel.uninstallBorder(comboBox);
        }

        @Override
        protected JButton createArrowButton() {
            final JButton button = new JButton("  V  ");
            button.setName("ComboBox.arrowButton"); //Mandatory, as per BasicComboBoxUI#createArrowButton().
            return button;
        }

        @Override
        public void configureArrowButton() {
            super.configureArrowButton(); //Do not forget this!
            arrowButton.setFont(new Font("Manjari", Font.BOLD, 16));
            arrowButton.setBackground(new Color(51, 51, 51));
            arrowButton.setForeground(Color.WHITE);
            arrowButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        }

        //Overrided getMinimumSize to take into account the button's insets for both width and height:
        @Override
        public Dimension getMinimumSize(final JComponent c) {
            //final Dimension mindim = super.getMinimumSize(c);
            final Dimension mindim = new Dimension(30, 30);
            final Insets buttonInsets = arrowButton.getInsets();
            return new Dimension(mindim.width + buttonInsets.left + buttonInsets.right, mindim.height + buttonInsets.top + buttonInsets.bottom);
        }
    }

    private void loadDefault() {
        ImageIcon ii = new ImageIcon(this.getClass().getClassLoader().getResource("img/med.jpg"));
        Image img = ii.getImage().getScaledInstance(190, 190, Image.SCALE_SMOOTH);
        lblImage.setIcon(new ImageIcon(img));
        lblChange.setVisible(false);

        lblTableLabel.setVisible(false);

        //comboBox design
        UIManager.put("ComboBox.squareButton", Boolean.FALSE);
        cmbSort.setUI(new MyComboBoxUI());
        cmbCategory.setUI(new MyComboBoxUI());
        cmbSort.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));

        //hide/show edit textbox
        hideShowComponents();
        HideShowEditLabel(false);

        addItemToComboBox();
        comboBox();
    }

    private void addItemToComboBox() {
        for (String str : hs) {
            cmbCategory.addItem(str);
            cmbSort.addItem(str);
        }
    }

    private void hideShowComponents() {
        txtName.setVisible(false);
        cmbCategory.setVisible(false);
        txtPrice.setVisible(false);
        txtStock.getText();
    }

    private void HideShowEditLabel(boolean b) {

        lblEditName.setVisible(b);
        lblEditCategory.setVisible(b);
        lblEditPrice.setVisible(b);
        lblEditStock.setVisible(b);
    }

    private void comboBox() {
        cmbSort.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    String item = (String) e.getItem();
                    if (item.equals("All")) {
                        displayAllItem(fetchAll);

                    } else if (item.equals("Critical Stocks")) {

                        try {
                            String input = JOptionPane.showInputDialog(null, "Enter Max Value");

                            System.out.println(input);
                            displayCritical(Integer.valueOf(input));

                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                        }

                    } else if (item.equals("MEDICAL SUPPLY") || item.equals("MEDICINE SUPPLY")) {
                        if (item.equals("MEDICAL SUPPLY")) {
                            displayAllItem("Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code where item_category = 'MEDICAL SUPPLY'");
                        } else {
                            displayAllItem("Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code where item_category = 'MEDICINE SUPPLY'");
                        }

                    } else if (item.equals("Added Today")) {

                        displayAllItem("Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code where item_category = ' SUPPLY'");
                    }
                }

            }
        });

    }

    private void hideTextBox() {
        txtName.hide();
        cmbCategory.hide();
        txtPrice.hide();
        txtStock.hide();
    }

}
