/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panels;

import java.sql.Connection;

import Classes.DbConnection;
import Classes.LogsClass;
import Frames.mini.Logs;

import PrintPages.DailySalesReport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author padilla
 */
public class DashBoard extends javax.swing.JPanel {

    /**
     * Creates new form DashBoard
     */
    Connection conn;
    
    DailySalesReport report = new DailySalesReport();
    
     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    LocalDateTime now = LocalDateTime.now();

    public DashBoard() {
        conn = DbConnection.dbConnect();
        initComponents();

        lstItems.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        jList1.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
    }

    public void init() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        String[] date = dtf.format(now).split("-");

        int day = Integer.parseInt(date[2]);
        
        
        //load logs,Bar Chart, DSR
        showBarChart("Daily", "day(tbl_Sales.date)", "Days", day);
        loadTodaySales(dtf.format(now));
        loadLogs();
        
        
        String countUsers = "Select COUNT(*) FROM tbl_users; ";
        countRows(countUsers, lblTotalUsers );
        
         String countItems = "Select COUNT(*) FROM tbl_items ";
         countRows(countItems, lbltotalItems );
        
         String countTransaction = "Select COUNT(tbl_salesdetails.transaction_num), tbl_Sales.date FROM tbl_Sales INNER JOIN tbl_salesdetails on tbl_salesdetails.transaction_num = tbl_Sales.transaction_num WHERE tbl_Sales.date = CURDATE(); ";
         countRows(countTransaction, lblTransCount );
    }

    void showBarChart(String category, String query, String label, int last) {

        String sql = "SELECT  count(tbl_salesdetails.transaction_num) as count, " + query + " as date, tbl_iteminfo.item_price, tbl_salesdetails.qty as qty, SUM(tbl_salesdetails.qty *tbl_iteminfo.item_price) as daily FROM tbl_Sales INNER JOIN tbl_salesdetails on tbl_salesdetails.transaction_num = tbl_Sales.transaction_num INNER JOIN tbl_iteminfo ON tbl_iteminfo.item_code = tbl_salesdetails.item_code GROUP by " + query + " order by " + query + " limit 10";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String date = rs.getString("date");
                String daily = rs.getString("daily");

                dataset.setValue(Float.parseFloat(daily), "Amount", date);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        JFreeChart Barchart = ChartFactory.createBarChart(category + " Sales", label, "Sales", dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot lineCategory = Barchart.getCategoryPlot();
        lineCategory.setBackgroundPaint(Color.WHITE);

        BarRenderer lineRenderer = (BarRenderer) lineCategory.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        ChartPanel lineChartPanel = new ChartPanel(Barchart);
        panelBarGraph.removeAll();
        panelBarGraph.add(lineChartPanel, BorderLayout.CENTER);
        panelBarGraph.validate();

    }
    
    
    private void countRows(String sql, JLabel label){
       
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int total= rs.getInt(1);
            label.setText(String.valueOf(total));
            
           
        }catch(Exception e){
            
        }
    }

    private void loadTodaySales(String date) {
        String sql = "SELECT tbl_Sales.date as date, tbl_items.item_name as name, sum(tbl_salesdetails.qty) as qty, sum(tbl_salesdetails.qty*tbl_iteminfo.item_price)AS total from tbl_items INNER JOIN tbl_iteminfo on tbl_iteminfo.item_code = tbl_items.item_code INNER JOIN tbl_salesdetails ON tbl_salesdetails.item_code = tbl_items.item_code INNER JOIN tbl_Sales on tbl_Sales.transaction_num = tbl_salesdetails.transaction_num WHERE tbl_Sales.date = '"+date+"' GROUP by tbl_items.item_name; ";
        try {

            DefaultListModel itemList = new DefaultListModel();
            String format = "%1$-30s %2$10s %3$10s";
            String header = String.format(format, "Name", "QTY", "Total");
            itemList.addElement(header);
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            int count = 0;
            float x = 0;//total price
            while (rs.next()) {
                String name = rs.getString("name");

                String qty = rs.getString("qty");
                String total = rs.getString("total");

                count += Integer.parseInt(qty);
                x += Float.parseFloat(total);

                String line = String.format(format, name, qty, "P" + total);
                itemList.addElement(line);
                date = rs.getString("date");

            }
            
            lblToday.setText("P"+String.valueOf(x));
            lstItems.setModel(itemList);
            lblTotal.setText("Total: P" + x);
            lblTotalItems.setText("Total Item(s):" + count);
            
            report.populateList(  itemList);
            report.setTotalItems(count);
            report.setTotalPrice(x);
            report.setDate(date);
        } catch (Exception e) {

        }
    }
    String getDateFormat(String str) {
        String[] date = dateChooserCombo1.getText().split("/");

        return date[2] + "-" + date[1] + "-" + date[0];
    }
    
    private void loadLogs(){
        DefaultListModel itemList = new DefaultListModel();
        String format =  "%1$-17s %2$-10s";
        String header = String.format(format,  "TimeStamp", "Details");
        itemList.addElement(header);

        LogsClass log = new LogsClass();
        ResultSet rs = log.loadLogs();
        try {
            while (rs.next()) {
                String timeStamp = rs.getString("date") + " " + rs.getString("time");
                String details = rs.getString("username") + " " + rs.getString("details");
               
                
                String line = String.format(format, timeStamp, details);
                itemList.addElement(line);
                
            }
            jList1.setModel(itemList);
        } catch (Exception ex) {

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

        panelNumberOfReceipt = new javax.swing.JPanel();
        lblTotalUsers = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        panelNumberOfReceipt1 = new javax.swing.JPanel();
        lbltotalItems = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        panelNumberOfReceipt2 = new javax.swing.JPanel();
        lblTransCount = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        panelNumberOfReceipt3 = new javax.swing.JPanel();
        lblToday = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstItems = new javax.swing.JList<>();
        panelBarGraph = new javax.swing.JPanel();
        lblDate1 = new javax.swing.JLabel();
        lblTotalItems = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        lblViewAll = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelNumberOfReceipt.setBackground(new java.awt.Color(204, 153, 0));
        panelNumberOfReceipt.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalUsers.setFont(new java.awt.Font("URW Gothic L", 1, 24)); // NOI18N
        lblTotalUsers.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalUsers.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalUsers.setText("100000");
        panelNumberOfReceipt.add(lblTotalUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 120, 40));

        jLabel9.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Number of Users");
        panelNumberOfReceipt.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 160, 20));

        add(panelNumberOfReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 171, -1));

        panelNumberOfReceipt1.setBackground(new java.awt.Color(0, 51, 255));
        panelNumberOfReceipt1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbltotalItems.setFont(new java.awt.Font("URW Gothic L", 1, 24)); // NOI18N
        lbltotalItems.setForeground(new java.awt.Color(255, 255, 255));
        lbltotalItems.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbltotalItems.setText("100000");
        panelNumberOfReceipt1.add(lbltotalItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 120, 40));

        jLabel10.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Total Number of Items");
        panelNumberOfReceipt1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 160, 20));

        add(panelNumberOfReceipt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 171, -1));

        panelNumberOfReceipt2.setBackground(new java.awt.Color(255, 0, 51));
        panelNumberOfReceipt2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTransCount.setFont(new java.awt.Font("URW Gothic L", 1, 24)); // NOI18N
        lblTransCount.setForeground(new java.awt.Color(255, 255, 255));
        lblTransCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransCount.setText("100000");
        panelNumberOfReceipt2.add(lblTransCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 120, 40));

        jLabel11.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Today's Transaction");
        panelNumberOfReceipt2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 160, 20));

        add(panelNumberOfReceipt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 171, -1));

        panelNumberOfReceipt3.setBackground(new java.awt.Color(0, 204, 0));
        panelNumberOfReceipt3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblToday.setFont(new java.awt.Font("URW Gothic L", 1, 24)); // NOI18N
        lblToday.setForeground(new java.awt.Color(255, 255, 255));
        lblToday.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToday.setText("100000");
        panelNumberOfReceipt3.add(lblToday, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 120, 40));

        jLabel12.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Today's Sales");
        panelNumberOfReceipt3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 160, 20));

        add(panelNumberOfReceipt3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 40, 171, -1));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);
        jScrollPane2.setOpaque(false);

        lstItems.setBorder(new javax.swing.border.MatteBorder(null));
        lstItems.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        lstItems.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstItems);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 230, 380, 260));

        panelBarGraph.setLayout(new java.awt.BorderLayout());
        add(panelBarGraph, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 470, 220));

        lblDate1.setFont(new java.awt.Font("DejaVu Sans Condensed", 1, 18)); // NOI18N
        lblDate1.setText("Daily Sales Report ");
        add(lblDate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 190, 180, 30));

        lblTotalItems.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        lblTotalItems.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        add(lblTotalItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 500, 160, 20));

        lblTotal.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 500, 110, 20));

        dateChooserCombo1.setCurrentView(new datechooser.view.appearance.AppearancesList("Dali",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(51, 51, 51),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(51, 51, 51),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(51, 51, 51),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(51, 51, 51),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dateChooserCombo1.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dateChooserCombo1OnCommit(evt);
        }
    });
    add(dateChooserCombo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 190, 120, 25));

    jPanel2.setBackground(new java.awt.Color(0, 102, 255));
    jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jPanel2MouseClicked(evt);
        }
    });
    jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/printer-32.png"))); // NOI18N
    jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 26, 26));

    add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 190, 30, 30));

    jScrollPane1.setBorder(null);
    jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jList1.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    jList1.setModel(new javax.swing.AbstractListModel<String>() {
        String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
        public int getSize() { return strings.length; }
        public String getElementAt(int i) { return strings[i]; }
    });
    jScrollPane1.setViewportView(jList1);

    add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 470, 90));

    jLabel1.setFont(new java.awt.Font("DejaVu Sans Condensed", 1, 18)); // NOI18N
    jLabel1.setText("Logs");
    add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 150, 30));

    lblViewAll.setFont(new java.awt.Font("AnjaliOldLipi", 0, 12)); // NOI18N
    lblViewAll.setForeground(new java.awt.Color(0, 51, 204));
    lblViewAll.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblViewAll.setText("View all");
    lblViewAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    lblViewAll.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            lblViewAllMouseClicked(evt);
        }
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            lblViewAllMouseEntered(evt);
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            lblViewAllMouseExited(evt);
        }
    });
    add(lblViewAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 430, 130, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void dateChooserCombo1OnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dateChooserCombo1OnCommit

    
         loadTodaySales( getDateFormat(dateChooserCombo1.getText())) ;
     
    }//GEN-LAST:event_dateChooserCombo1OnCommit

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        
        report.show();
    }//GEN-LAST:event_jPanel2MouseClicked

    private void lblViewAllMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewAllMouseEntered
       lblViewAll.setText("<html><u>View All</u></html>");
    }//GEN-LAST:event_lblViewAllMouseEntered

    private void lblViewAllMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewAllMouseExited
         lblViewAll.setText("View All");
    }//GEN-LAST:event_lblViewAllMouseExited

    private void lblViewAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewAllMouseClicked
      Logs l = new Logs();
      l.show();
    }//GEN-LAST:event_lblViewAllMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDate1;
    private javax.swing.JLabel lblToday;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalItems;
    private javax.swing.JLabel lblTotalUsers;
    private javax.swing.JLabel lblTransCount;
    private javax.swing.JLabel lblViewAll;
    private javax.swing.JLabel lbltotalItems;
    private javax.swing.JList<String> lstItems;
    private javax.swing.JPanel panelBarGraph;
    private javax.swing.JPanel panelNumberOfReceipt;
    private javax.swing.JPanel panelNumberOfReceipt1;
    private javax.swing.JPanel panelNumberOfReceipt2;
    private javax.swing.JPanel panelNumberOfReceipt3;
    // End of variables declaration//GEN-END:variables
}
