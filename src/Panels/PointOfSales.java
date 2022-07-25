/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panels;

import Panels.mini.CartItem;
import Panels.mini.Item;
import Panels.mini.Item_empty;
import Classes.DbConnection;
import Classes.LogsClass;
import Classes.User;
import Frames.mini.AddQty;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import javax.swing.JViewport;

/**
 *
 * @author padilla
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

public class PointOfSales extends javax.swing.JPanel {

    /**
     * Creates new form PointOfSales
     */
    
    LogsClass log = new LogsClass();
   
    int totalRow;
    List<Item> item = new ArrayList<>();
    Connection conn = null;

    int count = -1;
    public static List<Item> cartItem = new ArrayList<>();
    LinkedHashSet<String> recent = new LinkedHashSet<>();
    User user;
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    LocalDateTime now = LocalDateTime.now();
    
    String all = "Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code order by tbl_items.item_name";
    
    
    
    public PointOfSales() {

        conn = DbConnection.dbConnect();
        initComponents();
        init();
       
      
        System.out.println(dtf.format(now));
    }
    
    public void init(){
         //gridBagLayout();
        itemGrid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        displayItem(all);
        

        //Scroll speed
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(10);
        jScrollPane2.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        
        
        txtSearch.setBorder(new MatteBorder(1,1,1,1, Color.BLACK));
        lblDate.setText("Date: "+dtf.format(now));
        
        generateTransactionID();
        
         panelActive(panelAll, new Color(0,102,0), jLabel10);
    }
    
    public void setUserInstance(User user){
        this.user = user;
//        System.out.println(user.getFirstName());
    }
    
    void populateCart(){
        CartItem _cartItem = null;
        
        for(int i =0; i<5; i++){
           
            _cartItem = new CartItem();
             if(i%2==0){
                _cartItem.setBackground(Color.GRAY);
            }
             _cartItem.setInstance(this);
            _cartItem.setName(String.valueOf(i));
            cart.add(_cartItem);
            
        }
    }
    public void loadCart(){
        
        double totalPrice=0;
        int totalQty=0;
        CartItem _cartItem = null;
        cart.removeAll();
        cart.revalidate();

        int i = 0;
        // cart.setLayout(new GridLayout(cartItem.size(),1,5,5));
        for (Item _item : cartItem) {
            
            _cartItem = new CartItem();
            if (i % 2 == 0) {
                _cartItem.setBackground(new Color(137,162,201));
            }
       
            _cartItem.setInstance(this);
            
            _cartItem.index = i;//index for accessing item from cart
            
            _cartItem.setItem(_item.getItemName(), _item.getQty(), _item.getItemPrice() );
            System.out.println("QTY"+_item.getQty());
              //compute total
            int qty = Integer.parseInt(_item.getQty());
            double price =_cartItem.getTotal();
            totalPrice += price;
            totalQty+=qty;
            
            cart.add(_cartItem);
            i++;
        }
        
        cart.repaint();
        
        lblTotalItem.setText(String.valueOf(totalQty));
        lblTotalPrice.setText("P"+String.valueOf(totalPrice));
    }
    

    
    
    
    
    private void displayItem(String sql){
       itemGrid.removeAll();
       itemGrid.revalidate();
       try{
           PreparedStatement pst = conn.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
           rs.last();
           totalRow = rs.getRow();
           
           
        
          double maxrow = Math.ceil(rs.getRow()/3)+1;
           System.out.println("max row " +maxrow);
            System.out.println("total row " +totalRow);
          if(maxrow < 2){
              maxrow =2;
          }
          itemGrid.setLayout(new GridLayout((int)maxrow,3,5,5));
            rs.beforeFirst();
          
           int i = 0;
          
            
           while (rs.next()) {
               Item it = new Item();
               item.add(it);

               String id = rs.getString("item_code");
               String name = rs.getString("item_name");
               String price = rs.getString("item_price");
               String stocks = rs.getString("item_stock");
               
               it.setId(id);
               String path = rs.getString("item_image");
               it.setItem(name, price, stocks);
               it.setInstance(this);
               displayImage(it, path);

               if (Integer.parseInt(stocks) < 1) {
                   it.enableItem(false);
               } else {
                   it.setEnabled(true);
               }
               itemGrid.add(it);
               i++;
           }
           
           if(i<6){
            for(int j = i ; j<6; j++){
                 itemGrid.add(new Item_empty());
            }
           }
            itemGrid.repaint();
       }catch(Exception e){
           System.out.println(e);
       }
    }
    
        private void displayImage(Item item, String path) {
        File dir = new File(path);
        ImageIcon ii = new ImageIcon(path);
        try {
            if (!dir.exists()) {
                ii = new ImageIcon(this.getClass().getClassLoader().getResource("img/med.jpg"));

            }
            Image img = ii.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            item.setImage(new ImageIcon(img));

        } catch (Exception e) {

        }

    }
        
    private void insertToSales(String receipt, String date, String userName){
        String sqlsales = "insert into tbl_Sales(transaction_num,username,date) values(?,?,?)";
        
        
        try{
            PreparedStatement pst = conn.prepareStatement(sqlsales);
            pst.setString(1, receipt);
            pst.setString(2, userName);
            pst.setString(3, date);
            pst.execute();
            
        }catch(Exception e){
            
        }
    }
    
     private void insertToSalesDetails(String receipt, String itemCode, String qty){
        
        String sqlsalesinfo = "insert into tbl_salesdetails(transaction_num,item_code,qty) values(?,?,?)";
        
        try{
        
            
            PreparedStatement pst = conn.prepareStatement(sqlsalesinfo);
            pst.setString(1, receipt);
            pst.setString(2, itemCode);
            pst.setString(3, qty);
            pst.execute();
          
            subtractQty(itemCode,  qty);
        }catch(Exception e){
            System.out.println(e);
        }
    }
     
     private void subtractQty(String itemCode, String qty){
         String sql ="UPDATE tbl_iteminfo SET tbl_iteminfo.item_stock = tbl_iteminfo.item_stock-"+qty+" WHERE tbl_iteminfo.item_code = '"+itemCode+"'";
         try{
             PreparedStatement pst = conn.prepareStatement(sql);
             pst.execute();
               System.out.println("subtracted to qty");
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
        jScrollPane1 = new javax.swing.JScrollPane();
        itemGrid = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        lblDate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblReceipt = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cart = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTotalPrice = new javax.swing.JLabel();
        lblTotalItem = new javax.swing.JLabel();
        panelSave = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblQty = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        lblTotal1 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        panelTopSeller = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        panelRecent = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        panelAll = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
        });
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBorder(new javax.swing.border.MatteBorder(null));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane1KeyPressed(evt);
            }
        });

        itemGrid.setBackground(new java.awt.Color(255, 255, 255));
        itemGrid.setBorder(new javax.swing.border.MatteBorder(null));
        itemGrid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                itemGridMouseEntered(evt);
            }
        });
        itemGrid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemGridKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemGridKeyReleased(evt);
            }
        });
        itemGrid.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane1.setViewportView(itemGrid);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 470, 390));

        txtSearch.setBorder(new javax.swing.border.MatteBorder(null));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        jPanel1.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 220, 20));

        lblDate.setFont(new java.awt.Font("URW Gothic L", 0, 14)); // NOI18N
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 160, 20));

        jLabel6.setFont(new java.awt.Font("URW Gothic L", 0, 12)); // NOI18N
        jLabel6.setText("Search");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 60, 20));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, 486, 448));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        jLabel1.setText("Receipt no.");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 90, 30));

        jLabel2.setFont(new java.awt.Font("URW Gothic L", 1, 18)); // NOI18N
        jLabel2.setText("Cart");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 90, 30));

        lblReceipt.setBackground(new java.awt.Color(204, 204, 204));
        lblReceipt.setFont(new java.awt.Font("URW Gothic L", 0, 14)); // NOI18N
        jPanel3.add(lblReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, 30));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 436, 40));

        jScrollPane2.setBorder(new javax.swing.border.MatteBorder(null));

        cart.setBackground(new java.awt.Color(255, 255, 255));
        cart.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        cart.setLayout(new java.awt.GridLayout(100, 1));
        jScrollPane2.setViewportView(cart);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 100, 430, 350));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        jLabel4.setText("Total Price:");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 30));

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        jLabel5.setText("Total Item(s):");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 30));

        lblTotalPrice.setBackground(new java.awt.Color(204, 204, 204));
        lblTotalPrice.setFont(new java.awt.Font("URW Gothic L", 1, 12)); // NOI18N
        lblTotalPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(lblTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 80, 30));

        lblTotalItem.setBackground(new java.awt.Color(204, 204, 204));
        lblTotalItem.setFont(new java.awt.Font("URW Gothic L", 1, 12)); // NOI18N
        lblTotalItem.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(lblTotalItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 80, 30));

        panelSave.setBackground(new java.awt.Color(0, 0, 153));
        panelSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Save");

        javax.swing.GroupLayout panelSaveLayout = new javax.swing.GroupLayout(panelSave);
        panelSave.setLayout(panelSaveLayout);
        panelSaveLayout.setHorizontalGroup(
            panelSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSaveLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        panelSaveLayout.setVerticalGroup(
            panelSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jPanel4.add(panelSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 170, 70));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 450, 430, 110));

        lblName.setBackground(new java.awt.Color(153, 153, 153));
        lblName.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblName.setText("Name");
        lblName.setOpaque(true);
        jPanel2.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 70, 170, 25));

        lblQty.setBackground(new java.awt.Color(153, 153, 153));
        lblQty.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        lblQty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblQty.setText("Qty");
        lblQty.setOpaque(true);
        jPanel2.add(lblQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(183, 70, 69, 25));

        lblPrice.setBackground(new java.awt.Color(153, 153, 153));
        lblPrice.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        lblPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrice.setText("Price");
        lblPrice.setOpaque(true);
        jPanel2.add(lblPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 70, 69, 25));

        lblTotal1.setBackground(new java.awt.Color(153, 153, 153));
        lblTotal1.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        lblTotal1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotal1.setText("Total");
        lblTotal1.setOpaque(true);
        jPanel2.add(lblTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(323, 70, 69, 25));

        lblTotal.setBackground(new java.awt.Color(153, 153, 153));
        lblTotal.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotal.setOpaque(true);
        jPanel2.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(373, 70, 69, 25));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(498, 0, 460, 570));

        panelTopSeller.setBackground(new java.awt.Color(153, 0, 0));
        panelTopSeller.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelTopSeller.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelTopSellerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelTopSellerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelTopSellerMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelTopSellerMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelTopSellerMouseReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("FreeSans", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Top Seller");

        javax.swing.GroupLayout panelTopSellerLayout = new javax.swing.GroupLayout(panelTopSeller);
        panelTopSeller.setLayout(panelTopSellerLayout);
        panelTopSellerLayout.setHorizontalGroup(
            panelTopSellerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTopSellerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTopSellerLayout.setVerticalGroup(
            panelTopSellerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        add(panelTopSeller, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 470, 150, 70));

        panelRecent.setBackground(new java.awt.Color(153, 153, 0));
        panelRecent.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelRecent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRecentMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelRecentMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelRecentMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelRecentMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelRecentMouseReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("FreeSans", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Recent");

        javax.swing.GroupLayout panelRecentLayout = new javax.swing.GroupLayout(panelRecent);
        panelRecent.setLayout(panelRecentLayout);
        panelRecentLayout.setHorizontalGroup(
            panelRecentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRecentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelRecentLayout.setVerticalGroup(
            panelRecentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        add(panelRecent, new org.netbeans.lib.awtextra.AbsoluteConstraints(167, 470, 150, 70));

        panelAll.setBackground(new java.awt.Color(0, 102, 0));
        panelAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelAllMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelAllMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelAllMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelAllMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelAllMouseReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("FreeSans", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("All");

        javax.swing.GroupLayout panelAllLayout = new javax.swing.GroupLayout(panelAll);
        panelAll.setLayout(panelAllLayout);
        panelAllLayout.setHorizontalGroup(
            panelAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllLayout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        panelAllLayout.setVerticalGroup(
            panelAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        add(panelAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 150, 70));
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void itemGridKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemGridKeyReleased

    }//GEN-LAST:event_itemGridKeyReleased

    private void itemGridMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemGridMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_itemGridMouseEntered

    private void itemGridKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemGridKeyPressed
        
    }//GEN-LAST:event_itemGridKeyPressed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && count==-1) {
            txtSearch.setFocusTraversalKeysEnabled(false);
        }
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_TAB ) {

            // item[0].requestFocus();
            System.out.println(count);
          
            count++;
            highlightItem();
            
            setView(jScrollPane1, item.get(count));
            if (count == totalRow) {
                count = 0;
            }
        }
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && count>-1 && item.get(count).isItemEnabled()) {
            txtSearch.setFocusTraversalKeysEnabled(true);
          //  cartItem.add(item[count]);
            AddQty add = new AddQty();
            add.open(item.get(count), this);
            add.show();
        }
         
    }//GEN-LAST:event_txtSearchKeyPressed

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1KeyPressed

    private void jScrollPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane1KeyPressed
             
    }//GEN-LAST:event_jScrollPane1KeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
         
    }//GEN-LAST:event_formKeyPressed

    private void panelSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMouseClicked
        
        String receipt = lblReceipt.getText();
        String date =dtf.format(now);
        String userName ="Soybean";
        insertToSales( receipt,  date, userName);
        
        if(cartItem.isEmpty()){
             JOptionPane.showMessageDialog(null,"No item in Cart");
        }else{
              int reply = JOptionPane.showConfirmDialog(null, "Proceed to save "+cartItem.size()+" items(s)", "Transaction", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            for (Item i : cartItem) {
                System.out.println(receipt + " " + i.getId() + " " + i.getQty());
                insertToSalesDetails(receipt, i.getId(), i.getQty());
                log.insertToLog(user.getUserName(), " transacted "+lblTotalItem.getText()+" item(s) to "+lblReceipt.getText());
                 generateTransactionID();
            }
            JOptionPane.showMessageDialog(null,"Transaction Complete");
            cartItem.clear();
            loadCart();

        }
        }
        
      
        
      
    }//GEN-LAST:event_panelSaveMouseClicked

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
            String sql = "Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code where tbl_items.item_name like '%"+txtSearch.getText()+"%'";
        displayItem(sql);
        }
        
    }//GEN-LAST:event_txtSearchKeyReleased

    private void panelTopSellerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopSellerMouseClicked
       panelActive(panelTopSeller, new Color(153,0,0), jLabel9);
        String sql ="SELECT tbl_salesdetails.item_code, tbl_items.item_name, tbl_items.item_image, tbl_iteminfo.item_price, tbl_iteminfo.item_stock, SUM(tbl_salesdetails.qty) AS total FROM tbl_salesdetails INNER JOIN tbl_items on tbl_items.item_code = tbl_salesdetails.item_code INNER JOIN tbl_iteminfo ON tbl_iteminfo.item_code = tbl_items.item_code GROUP BY tbl_salesdetails.item_code ORDER BY total DESC limit 20";
        displayItem(sql);
    }//GEN-LAST:event_panelTopSellerMouseClicked

    private void panelAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAllMouseClicked
       panelActive(panelAll, new Color(0,102,0), jLabel10);
        displayItem(all);
    }//GEN-LAST:event_panelAllMouseClicked

    private void panelRecentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRecentMouseClicked
        panelActive(panelRecent, new Color(153,153,0), jLabel11);
        String sql ="SELECT tbl_salesdetails.id, tbl_salesdetails.item_code, tbl_items.item_name, tbl_items.item_image, tbl_iteminfo.item_price, tbl_iteminfo.item_stock FROM `tbl_Sales` inner join tbl_salesdetails on tbl_Sales.transaction_num = tbl_salesdetails.transaction_num INNER JOIN tbl_items on tbl_salesdetails.item_code = tbl_items.item_code INNER join tbl_iteminfo on tbl_iteminfo.item_code = tbl_items.item_code WHERE DATE(tbl_Sales.date) = CURDATE() or DATE(tbl_Sales.date) = CURDATE()-1 group by tbl_salesdetails.item_code ORDER by tbl_salesdetails.id DESC LIMIT 20";
       displayItem(sql);
 
    }//GEN-LAST:event_panelRecentMouseClicked

    private void panelAllMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAllMouseEntered
        System.out.println(panelAll.getBackground().getRGB());
       // if(panelAll.getBackground().getRGB()!=-16751104)
       panelColor(panelAll,  new Color(51,204,0));
       // panelAll.setBackground(new Color(51,204,0));
    }//GEN-LAST:event_panelAllMouseEntered

    private void panelAllMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAllMouseExited
        resetPanelColor(panelAll,  new Color(0,102,0));
       // panelAll.setBackground(new Color(0,102,0));
    }//GEN-LAST:event_panelAllMouseExited

    private void panelRecentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRecentMouseEntered
       //  panelRecent.setBackground(new Color(255,204,0));
         
         panelColor(panelRecent, new Color(255,204,0));
    }//GEN-LAST:event_panelRecentMouseEntered

    private void panelRecentMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRecentMouseExited
      //  panelRecent.setBackground(new Color(153,153,0));
         resetPanelColor(panelRecent, new Color(153,153,0));
    }//GEN-LAST:event_panelRecentMouseExited

    private void panelTopSellerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopSellerMouseEntered
        panelColor(panelTopSeller, new Color(255,0,0));
        //panelTopSeller.setBackground(new Color(255,0,0));
    }//GEN-LAST:event_panelTopSellerMouseEntered

    private void panelTopSellerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopSellerMouseExited
        resetPanelColor(panelTopSeller, new Color(153,0,0));
     //   panelTopSeller.setBackground(new Color(153,0,0));
    }//GEN-LAST:event_panelTopSellerMouseExited

    private void panelSaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMouseEntered
       panelSave.setBackground(new Color(0,51,204));
    }//GEN-LAST:event_panelSaveMouseEntered

    private void panelSaveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMouseExited
        panelSave.setBackground(new Color(0,0,153));
    }//GEN-LAST:event_panelSaveMouseExited

    private void panelAllMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAllMousePressed
         panelAll.setBackground(new Color(0,102,0));
         jLabel10.setForeground(Color.WHITE);
    }//GEN-LAST:event_panelAllMousePressed

    private void panelAllMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAllMouseReleased
       panelAll.setBackground(new Color(51,204,0));
    }//GEN-LAST:event_panelAllMouseReleased

    private void panelRecentMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRecentMousePressed
        panelRecent.setBackground(new Color(153,153,0));
        jLabel11.setForeground(Color.WHITE);
    }//GEN-LAST:event_panelRecentMousePressed

    private void panelRecentMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRecentMouseReleased
        panelRecent.setBackground(new Color(255,204,0));
    }//GEN-LAST:event_panelRecentMouseReleased

    private void panelTopSellerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopSellerMousePressed
        panelTopSeller.setBackground(new Color(153,0,0));
        jLabel9.setForeground(Color.WHITE);
    }//GEN-LAST:event_panelTopSellerMousePressed

    private void panelTopSellerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopSellerMouseReleased
          panelTopSeller.setBackground(new Color(255,0,0));
    }//GEN-LAST:event_panelTopSellerMouseReleased

    private void panelSaveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMousePressed
        panelSave.setBackground(new Color(0,0,153));
    }//GEN-LAST:event_panelSaveMousePressed

    private void panelSaveMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSaveMouseReleased
         panelSave.setBackground(new Color(0,51,204));
    }//GEN-LAST:event_panelSaveMouseReleased

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
       
    }//GEN-LAST:event_formMouseEntered

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        System.out.println(jPanel1.getBackground().getRGB());
    }//GEN-LAST:event_jPanel1MouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cart;
    private javax.swing.JPanel itemGrid;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblQty;
    private javax.swing.JLabel lblReceipt;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JLabel lblTotalItem;
    private javax.swing.JLabel lblTotalPrice;
    private javax.swing.JPanel panelAll;
    private javax.swing.JPanel panelRecent;
    private javax.swing.JPanel panelSave;
    private javax.swing.JPanel panelTopSeller;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
    
    private void highlightItem(){
       
    
        for (int i = 0 ;i < totalRow; i++){
            
            item.get(i).setBackground(new Color(137,162,201));
        }
        item.get(count).setBackground(Color.GRAY);
    }
    
     public void highlightItemMouseClick(JPanel panel){
       
    
        for (int i = 0 ;i < totalRow; i++){
            
            item.get(i).setBackground(new Color(137,162,201));
        }
        panel.setBackground(Color.GRAY);
    }
    
     public void setView(JScrollPane scroll, Component comp) {
        JViewport view = scroll.getViewport();
        Point p = comp.getLocation();
        view.setViewPosition(p);
    }
     
     
    public void loadDefault(){
      //  displayItem("Select * from tbl_items inner join tbl_iteminfo on tbl_items.item_code = tbl_iteminfo.item_code where tbl_items.item_name like '%"+txtSearch.getText()+"%'");
        txtSearch.setText("");
        txtSearch.requestFocus();
        
        txtSearch.setFocusTraversalKeysEnabled(true);
        count = -1;
    }
    
    private void generateTransactionID() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYMM");
        LocalDateTime now = LocalDateTime.now();

        String sql = "select transaction_num from tbl_Sales";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.last();
            if (rs.getRow() == 0) {
                lblReceipt.setText(dtf.format(now) + "-00001");
            }else{
                lblReceipt.setText( updateReceiptNum(rs));
               
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private String updateReceiptNum(ResultSet rs) throws SQLException{
        
        String[] str = rs.getString("transaction_num").split("-");
        int res = Integer.parseInt(str[1])+1;
        
        String padded = "00000".substring(String.valueOf(res).length())+String.valueOf(res);
        
        return str[0]+"-"+padded;
    }
    
    private void panelActive(JPanel panel, Color color, JLabel label){
        
        jLabel10.setForeground(Color.WHITE);
        jLabel11.setForeground(Color.WHITE);
        jLabel9.setForeground(Color.WHITE);
        panelAll.setBackground(new Color(0,102,0));
        panelRecent.setBackground(new Color(153,153,0));
        panelTopSeller.setBackground(new Color(153,0,0));
        
        
        
        panel.setBackground(Color.WHITE);
        panel.setBorder(new MatteBorder(4, 4, 4, 4, color));
        label.setForeground(color);
    }
    
     private void panelColor(JPanel panel,  Color color){
        if(panel.getBackground().getRGB()!=-1){
            panel.setBackground(color);
        }
        
    }
    private void resetPanelColor(JPanel panel,  Color color){
        if(panel.getBackground().getRGB()!=-1){
             panel.setBackground(color);
        }
       
    }
    
}
