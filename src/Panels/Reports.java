/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panels;

import Panels.mini.Receipt;
import Classes.DbConnection;
import Classes.User;

import PrintPages.FramePrintReport;


import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.GridLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import java.util.ArrayList;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

/**
 *
 * @author padilla
 */
public class Reports extends javax.swing.JPanel {

    /**
     * Creates new form Reports
     */
    FramePrintReport printer = new FramePrintReport();
    Connection conn = null;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    LocalDateTime now = LocalDateTime.now();

    static List<Receipt> receipts = new ArrayList<>();
    
    String today;
    
    User user;

    public Reports() {
        conn = DbConnection.dbConnect();
        initComponents();
      // init();

    }

    public void init() {
        
        receipts.clear();
        String[] date = dtf.format(now).split("-");

        int day = Integer.parseInt(date[2]);
     
        showLineChart("Daily", "day(tbl_Sales.date)", "Days", day);
        panelButtonActive(panelDaily, lblDaily);
        loadReport("SELECT tbl_Sales.transaction_num, sum(tbl_salesdetails.qty) as qty, sum(tbl_salesdetails.qty * tbl_iteminfo.item_price) as totalPrice, tbl_Sales.date FROM tbl_salesdetails INNER JOIN tbl_Sales on tbl_salesdetails.transaction_num = tbl_Sales.transaction_num INNER JOIN tbl_iteminfo on tbl_iteminfo.item_code = tbl_salesdetails.item_code where tbl_Sales.date = curdate() GROUP BY tbl_salesdetails.transaction_num");

        table1.setLayout(new GridLayout(receipts.size() + 15, 1));
        loadTable();

       // loadReceiptItem("2205-00001");

        ftxtSearch.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));

    }

    void showLineChart(String category, String query, String label, int last) {
           
        String sql = "SELECT  count(tbl_salesdetails.transaction_num) as count, " + query + " as date, tbl_iteminfo.item_price, tbl_salesdetails.qty as qty, SUM(tbl_salesdetails.qty *tbl_iteminfo.item_price) as daily FROM tbl_Sales INNER JOIN tbl_salesdetails on tbl_salesdetails.transaction_num = tbl_Sales.transaction_num INNER JOIN tbl_iteminfo ON tbl_iteminfo.item_code = tbl_salesdetails.item_code GROUP by " + query + " order by " + query + " limit 10";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            String count = "";
            String totalItem = "";
            String totalSales = "";

            while (rs.next()) {
               
                String date = rs.getString("date");
                String daily = rs.getString("daily");

                dataset.setValue(Float.parseFloat(daily), "Amount", date);
                if (Integer.parseInt(date) == last) {
                  
                    if(category.equals("Daily")){
                        today =daily;
                      
                                
                    }
                    count = rs.getString("count");
                    totalItem = rs.getString("qty");
                    totalSales = daily;
                }
            }
            lblToday.setText(today);
            lblTransCount.setText(count);
            lblTotalItems.setText(String.valueOf(totalItem));
            lblTotalSales.setText(String.valueOf("P" + totalSales));
        } catch (Exception e) {
            System.out.println(e);
        }

        JFreeChart linechart = ChartFactory.createLineChart(category + " Sales", label, "Sales", dataset, PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot lineCategory = linechart.getCategoryPlot();
        lineCategory.setBackgroundPaint(Color.WHITE);

        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategory.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        ChartPanel lineChartPanel = new ChartPanel(linechart);
        panelLineChart.removeAll();
        panelLineChart.add(lineChartPanel, BorderLayout.CENTER);
        panelLineChart.validate();

    }

    private void loadReport(String sql) {
        Receipt receipt = null;

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            receipt = new Receipt();
            receipt.addDetails("Transaction No");
            receipt.addDetails("Qty");
            receipt.addDetails("Total Price");
            receipt.addDetails("Date");
            receipts.add(receipt);

            int totalItem = 0;
            float totalSales = 0;
            while (rs.next()) {
                receipt = new Receipt();
                receipt.addDetails(rs.getString("transaction_num"));
                receipt.addDetails(rs.getString("qty"));
                receipt.addDetails(rs.getString("totalPrice"));
                receipt.addDetails(rs.getString("date"));

                totalItem += Integer.valueOf(rs.getString("qty"));
                totalSales += Float.parseFloat(rs.getString("totalPrice"));
                receipts.add(receipt);

            }
            rs.last();
            String count = String.valueOf(rs.getRow());
            lblTransCount.setText(count);
            lblTotalItems.setText(String.valueOf(totalItem));
            lblTotalSales.setText(String.valueOf("P" + totalSales));

        } catch (Exception e) {
            System.out.println("report " + e);
        }
    }

    public void loadTable() {

        table1.removeAll();
        table1.revalidate();

        int i = 0;
        for (Receipt item : receipts) {

            if (i % 2 == 0) {
                item.setBackground(Color.WHITE);
            } else {
                item.setBackground(new Color(153, 153, 255));
            }
            item.setInstance(this);
            item.loadLabels();
            table1.add(item);
            i++;
        }
//        if(receipts.size()<15){
//            for (int j = i; j<15; j++){
//                Receipt rece
//                table1.add(new Receipt());
//            }
//            
//        }
        table1.repaint();
    }

    String getDateFormat(String str) {
        String[] date = dateChooserCombo1.getText().split("/");

        return date[2] + "-" + date[1] + "-" + date[0];
    }

    public void loadReceiptItem(String receiptNum) {
       
        lblReceiptNum.setText(receiptNum);
        printer.setReciept(receiptNum);
        String sql = "Select tbl_Sales.date as date, tbl_items.item_name as name, tbl_iteminfo.item_price as price, tbl_salesdetails.qty as qty,(tbl_iteminfo.item_price * tbl_salesdetails.qty) as total from tbl_salesdetails INNER join tbl_iteminfo on tbl_iteminfo.item_code = tbl_salesdetails.item_code INNER JOIN tbl_items on tbl_items.item_code = tbl_iteminfo.item_code INNER join tbl_Sales on tbl_Sales.transaction_num = tbl_salesdetails.transaction_num WHERE tbl_salesdetails.transaction_num = '" + receiptNum + "'";
        try {
            DefaultListModel itemList = new DefaultListModel();

            String format = "%1$-30s %2$10s %3$10s";
            String header = String.format(format, "Name", "Price", "Total");
            itemList.addElement(header);
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            String date = "";
            int count = 0;
            float x = 0;//total price
            while (rs.next()) {
                String name = rs.getString("name");
                String price = rs.getString("price");
                String qty = rs.getString("qty");
                String total = rs.getString("total");

                count += Integer.parseInt(qty);
                x += Float.parseFloat(total);

                String line = String.format(format, qty + " " + name, "P" + price, "P" + total);
                itemList.addElement(line);
                date = rs.getString("date");
                
            }

            lblDate.setText(date);
            printer.setDate(date);
            lblItemCount.setText(String.valueOf(count));
            printer.setCount(count);
            lblTotal.setText("TOTAL: P" + String.valueOf(x));
            printer.setTotal(x);
            lstItems.setModel(itemList);
            printer.populateList(itemList);
        } catch (Exception e) {
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

        panelLineChart = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        panelNumberOfReceipt = new javax.swing.JPanel();
        lblTransCount = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        panelNumberOfItems = new javax.swing.JPanel();
        lblTotalItems = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        panelTotalSales = new javax.swing.JPanel();
        lblTotalSales = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        PanelTodaySales = new javax.swing.JPanel();
        lblToday = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstItems = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblItemCount = new javax.swing.JLabel();
        lblReceiptNum = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblDate1 = new javax.swing.JLabel();
        label1 = new javax.swing.JLabel();
        lblTransNum2 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblTransNum3 = new javax.swing.JLabel();
        ftxtSearch = new javax.swing.JFormattedTextField();
        panelMonthly = new javax.swing.JPanel();
        lblMonthly = new javax.swing.JLabel();
        panelYearly = new javax.swing.JPanel();
        lblYearly = new javax.swing.JLabel();
        panelDaily = new javax.swing.JPanel();
        lblDaily = new javax.swing.JLabel();
        panelWeekly = new javax.swing.JPanel();
        lblWeekly = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelLineChart.setBackground(new java.awt.Color(153, 255, 255));
        panelLineChart.setLayout(new java.awt.BorderLayout());
        add(panelLineChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 540, 160));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("URW Gothic L", 0, 14)); // NOI18N
        jLabel13.setText("Filter by Date");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 25));

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
    jPanel1.add(dateChooserCombo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 120, 25));

    jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    table1.setLayout(new java.awt.GridLayout(1, 0));
    jScrollPane1.setViewportView(table1);

    jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 530, 320));

    add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 550, 370));

    jPanel7.setBackground(new java.awt.Color(255, 255, 255));
    jPanel7.setForeground(new java.awt.Color(255, 255, 255));
    jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    panelNumberOfReceipt.setBackground(new java.awt.Color(204, 153, 0));
    panelNumberOfReceipt.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    lblTransCount.setFont(new java.awt.Font("URW Gothic L", 1, 24)); // NOI18N
    lblTransCount.setForeground(new java.awt.Color(255, 255, 255));
    lblTransCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblTransCount.setText("100000");
    panelNumberOfReceipt.add(lblTransCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 120, 40));

    jLabel9.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
    jLabel9.setForeground(new java.awt.Color(255, 255, 255));
    jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel9.setText("Number of Transactions");
    panelNumberOfReceipt.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 160, 20));

    jPanel7.add(panelNumberOfReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 171, 90));

    panelNumberOfItems.setBackground(new java.awt.Color(204, 0, 0));
    panelNumberOfItems.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    lblTotalItems.setFont(new java.awt.Font("URW Gothic L", 1, 24)); // NOI18N
    lblTotalItems.setForeground(new java.awt.Color(255, 255, 255));
    lblTotalItems.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblTotalItems.setText("100000");
    panelNumberOfItems.add(lblTotalItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 120, 40));

    jLabel10.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
    jLabel10.setForeground(new java.awt.Color(255, 255, 255));
    jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel10.setText("Total items");
    panelNumberOfItems.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 160, 20));

    jPanel7.add(panelNumberOfItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 171, 90));

    panelTotalSales.setBackground(new java.awt.Color(0, 0, 204));
    panelTotalSales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    lblTotalSales.setFont(new java.awt.Font("URW Gothic L", 1, 24)); // NOI18N
    lblTotalSales.setForeground(new java.awt.Color(255, 255, 255));
    lblTotalSales.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblTotalSales.setText("100000");
    panelTotalSales.add(lblTotalSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 120, 40));

    jLabel11.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
    jLabel11.setForeground(new java.awt.Color(255, 255, 255));
    jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel11.setText("Total Sales");
    panelTotalSales.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 160, 20));

    jPanel7.add(panelTotalSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 171, 90));

    PanelTodaySales.setBackground(new java.awt.Color(51, 153, 0));
    PanelTodaySales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    lblToday.setFont(new java.awt.Font("URW Gothic L", 1, 24)); // NOI18N
    lblToday.setForeground(new java.awt.Color(255, 255, 255));
    lblToday.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblToday.setText("100000");
    PanelTodaySales.add(lblToday, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 120, 40));

    jLabel12.setFont(new java.awt.Font("URW Gothic L", 1, 14)); // NOI18N
    jLabel12.setForeground(new java.awt.Color(255, 255, 255));
    jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel12.setText("Today Sales");
    PanelTodaySales.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 160, 20));

    jPanel7.add(PanelTodaySales, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 171, 90));

    jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
    jScrollPane2.setBorder(null);
    jScrollPane2.setOpaque(false);

    lstItems.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    lstItems.setModel(new javax.swing.AbstractListModel<String>() {
        String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
        public int getSize() { return strings.length; }
        public String getElementAt(int i) { return strings[i]; }
    });
    jScrollPane2.setViewportView(lstItems);

    jPanel7.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 380, 200));

    jPanel2.setBackground(new java.awt.Color(0, 102, 255));
    jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jPanel2MouseClicked(evt);
        }
    });
    jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jLabel1.setBackground(new java.awt.Color(255, 255, 255));
    jLabel1.setFont(new java.awt.Font("AnjaliOldLipi", 1, 16)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(255, 255, 255));
    jLabel1.setText("Print Report");
    jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 6, -1, -1));

    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/printer-32.png"))); // NOI18N
    jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 26, 26));

    jPanel7.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 130, 30));

    lblItemCount.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    jPanel7.add(lblItemCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 160, 20));

    lblReceiptNum.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    jPanel7.add(lblReceiptNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 160, 20));

    lblDate.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    jPanel7.add(lblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, 160, 20));

    lblDate1.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    lblDate1.setText("Date:");
    jPanel7.add(lblDate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 251, 100, 20));

    label1.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    label1.setText("Item(s)");
    jPanel7.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 100, 20));

    lblTransNum2.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    lblTransNum2.setText("Search");
    jPanel7.add(lblTransNum2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 512, 70, 20));

    lblTotal.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
    lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jPanel7.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 500, 110, 20));

    lblTransNum3.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    lblTransNum3.setText("Reciept No.");
    jPanel7.add(lblTransNum3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 20));

    ftxtSearch.setBorder(new javax.swing.border.MatteBorder(null));
    try {
        ftxtSearch.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-#####")));
    } catch (java.text.ParseException ex) {
        ex.printStackTrace();
    }
    ftxtSearch.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    ftxtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            ftxtSearchKeyPressed(evt);
        }
    });
    jPanel7.add(ftxtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 510, 100, 25));

    add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 400, 550));

    panelMonthly.setBackground(new java.awt.Color(255, 255, 255));
    panelMonthly.setBorder(new javax.swing.border.MatteBorder(null));
    panelMonthly.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    panelMonthly.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            panelMonthlyMouseClicked(evt);
        }
    });
    panelMonthly.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    lblMonthly.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    lblMonthly.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblMonthly.setText("Monthly");
    panelMonthly.add(lblMonthly, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 5, 86, -1));

    add(panelMonthly, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, -1, 30));

    panelYearly.setBackground(new java.awt.Color(255, 255, 255));
    panelYearly.setBorder(new javax.swing.border.MatteBorder(null));
    panelYearly.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    panelYearly.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            panelYearlyMouseClicked(evt);
        }
    });
    panelYearly.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    lblYearly.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    lblYearly.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblYearly.setText("Yearly");
    panelYearly.add(lblYearly, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 5, 86, -1));

    add(panelYearly, new org.netbeans.lib.awtextra.AbsoluteConstraints(324, 0, -1, 30));

    panelDaily.setBackground(new java.awt.Color(255, 255, 255));
    panelDaily.setBorder(new javax.swing.border.MatteBorder(null));
    panelDaily.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    panelDaily.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            panelDailyMouseClicked(evt);
        }
    });
    panelDaily.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    lblDaily.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    lblDaily.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblDaily.setText("Daily");
    panelDaily.add(lblDaily, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 5, 86, -1));

    add(panelDaily, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 110, 30));

    panelWeekly.setBackground(new java.awt.Color(255, 255, 255));
    panelWeekly.setBorder(new javax.swing.border.MatteBorder(null));
    panelWeekly.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    panelWeekly.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            panelWeeklyMouseClicked(evt);
        }
    });
    panelWeekly.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    lblWeekly.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
    lblWeekly.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblWeekly.setText("Weekly");
    panelWeekly.add(lblWeekly, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 5, 86, -1));

    add(panelWeekly, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 110, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void dateChooserCombo1OnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dateChooserCombo1OnCommit

        String sql = "SELECT tbl_Sales.transaction_num, sum(tbl_salesdetails.qty) as qty, sum(tbl_salesdetails.qty * tbl_iteminfo.item_price) as totalPrice, tbl_Sales.date FROM tbl_salesdetails INNER JOIN tbl_Sales on tbl_salesdetails.transaction_num = tbl_Sales.transaction_num INNER JOIN tbl_iteminfo on tbl_iteminfo.item_code = tbl_salesdetails.item_code  WHERE DATE(tbl_Sales.date) = '" + getDateFormat(dateChooserCombo1.getText()) + "' GROUP BY tbl_salesdetails.transaction_num";

        System.out.println(getDateFormat(dateChooserCombo1.getText()));
        receipts.clear();
        loadReport(sql);

        loadTable();

    }//GEN-LAST:event_dateChooserCombo1OnCommit

    private void panelDailyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelDailyMouseClicked
        String[] date = dtf.format(now).split("-");

        int day = Integer.parseInt(date[2]);
        showLineChart("Daily", "day(tbl_Sales.date)", "Days", day);
        panelButtonActive(panelDaily, lblDaily);
    }//GEN-LAST:event_panelDailyMouseClicked

    private void panelWeeklyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelWeeklyMouseClicked

        String[] date = dtf.format(now).split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        int week = localDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        showLineChart("Weekly", "week(tbl_Sales.date)", "Weeks", week);
        System.out.println(week);
        panelButtonActive(panelWeekly, lblWeekly);
    }//GEN-LAST:event_panelWeeklyMouseClicked

    private void panelMonthlyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMonthlyMouseClicked
        String[] date = dtf.format(now).split("-");

        int month = Integer.parseInt(date[1]);
        showLineChart("Monthly", "month(tbl_Sales.date)", "Month", month);
        panelButtonActive(panelMonthly, lblMonthly);
    }//GEN-LAST:event_panelMonthlyMouseClicked

    private void panelYearlyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelYearlyMouseClicked
        String[] date = dtf.format(now).split("-");
        int year = Integer.parseInt(date[0]);
        showLineChart("Yearly", "year(tbl_Sales.date)", "Year", year);
        panelButtonActive(panelYearly, lblYearly);
    }//GEN-LAST:event_panelYearlyMouseClicked

    private void ftxtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftxtSearchKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            loadReceiptItem(ftxtSearch.getText());
        }
    }//GEN-LAST:event_ftxtSearchKeyPressed

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked

       
        if(lblReceiptNum.getText().length()==0){
            JOptionPane.showMessageDialog(null, "Nothing to Print");
        }else{
             printer.show();
        
        }
    }//GEN-LAST:event_jPanel2MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelTodaySales;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private javax.swing.JFormattedTextField ftxtSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel lblDaily;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDate1;
    private javax.swing.JLabel lblItemCount;
    private javax.swing.JLabel lblMonthly;
    private javax.swing.JLabel lblReceiptNum;
    private javax.swing.JLabel lblToday;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalItems;
    private javax.swing.JLabel lblTotalSales;
    private javax.swing.JLabel lblTransCount;
    private javax.swing.JLabel lblTransNum2;
    private javax.swing.JLabel lblTransNum3;
    private javax.swing.JLabel lblWeekly;
    private javax.swing.JLabel lblYearly;
    private javax.swing.JList<String> lstItems;
    private javax.swing.JPanel panelDaily;
    private javax.swing.JPanel panelLineChart;
    private javax.swing.JPanel panelMonthly;
    private javax.swing.JPanel panelNumberOfItems;
    private javax.swing.JPanel panelNumberOfReceipt;
    private javax.swing.JPanel panelTotalSales;
    private javax.swing.JPanel panelWeekly;
    private javax.swing.JPanel panelYearly;
    private javax.swing.JPanel table1;
    // End of variables declaration//GEN-END:variables
    private void panelButtonActive(JPanel panel, JLabel label) {
        panelDaily.setBackground(new Color(255, 255, 255));
        panelWeekly.setBackground(new Color(255, 255, 255));
        panelMonthly.setBackground(new Color(255, 255, 255));
        panelYearly.setBackground(new Color(255, 255, 255));

        lblDaily.setForeground(new Color(51, 51, 51));
        lblWeekly.setForeground(new Color(51, 51, 51));
        lblMonthly.setForeground(new Color(51, 51, 51));
        lblYearly.setForeground(new Color(51, 51, 51));

        panel.setBackground(new Color(0, 51, 102));
        label.setForeground(new Color(255, 255, 255));
    }
}
