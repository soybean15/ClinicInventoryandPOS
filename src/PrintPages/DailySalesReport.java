/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PrintPages;

import Classes.PrintComponent;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;

/**
 *
 * @author padilla
 */
public class DailySalesReport extends javax.swing.JFrame {

    /**
     * Creates new form DailySalesReport
     */
    public DailySalesReport() {
        initComponents();
 

    }
    
    public void populateList( DefaultListModel itemList){
        lstItems.setModel(itemList);
    }
    
    public void setTotalItems( int count){
         lblItemCount.setText("Total Item(s):" + count);

    }
    
    public void setTotalPrice(float x ){
        lblTotal.setText("Total: P" + x);
    }
    public void setDate(String date){
        lblDate.setText("Date: "+date);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        PanelReport = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstItems = new javax.swing.JList<>();
        lblTotal = new javax.swing.JLabel();
        lblItemCount = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblReceiptNum = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblDate1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 102, 255));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("AnjaliOldLipi", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Print ");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 6, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/printer-32.png"))); // NOI18N
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 26, 26));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 610, 70, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelReport.setBackground(new java.awt.Color(255, 255, 255));
        PanelReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/MAXILIFE LOGO TRANSPARENT (1).png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelReport.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 70, 70));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);
        jScrollPane2.setOpaque(false);

        lstItems.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        lstItems.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstItems.setMaximumSize(new java.awt.Dimension(100, 85));
        lstItems.setMinimumSize(new java.awt.Dimension(0, 85));
        lstItems.setPreferredSize(new java.awt.Dimension(0, 85));
        jScrollPane2.setViewportView(lstItems);

        PanelReport.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 380, 320));

        lblTotal.setBackground(new java.awt.Color(255, 255, 255));
        lblTotal.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotal.setText("total");
        lblTotal.setOpaque(true);
        PanelReport.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 560, 110, 20));

        lblItemCount.setBackground(new java.awt.Color(255, 255, 255));
        lblItemCount.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        lblItemCount.setText("count");
        lblItemCount.setOpaque(true);
        PanelReport.add(lblItemCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 540, 160, 20));

        lblDate.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
        PanelReport.add(lblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 220, 160, 20));

        lblReceiptNum.setFont(new java.awt.Font("AnjaliOldLipi", 1, 14)); // NOI18N
        PanelReport.add(lblReceiptNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 160, 20));

        jLabel2.setFont(new java.awt.Font("Nimbus Sans Narrow", 1, 27)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("<html><p>Maxilife Maternity Lying-in and Multispecialty Clinic Co.</p></html>");
        PanelReport.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 340, 70));

        jLabel3.setFont(new java.awt.Font("Noto Sans Mono CJK TC", 0, 14)); // NOI18N
        jLabel3.setText("Email: maxilife_clinic2018@yahoo.com");
        PanelReport.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 310, 20));

        jLabel4.setFont(new java.awt.Font("Noto Sans Mono CJK TC", 0, 14)); // NOI18N
        jLabel4.setText("<html><p>Jose Abad Santos Avenue, San Fernando Sur,         Cabiao, Philippines</p></html>");
        PanelReport.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 310, 40));

        jLabel5.setFont(new java.awt.Font("Noto Sans Mono CJK TC", 0, 14)); // NOI18N
        jLabel5.setText("Tel No.: 0933 818 5130");
        PanelReport.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 310, 20));

        lblDate1.setFont(new java.awt.Font("DejaVu Sans Condensed", 1, 18)); // NOI18N
        lblDate1.setText("Daily Sales Report :");
        PanelReport.add(lblDate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 190, 30));

        jLabel8.setFont(new java.awt.Font("AnjaliOldLipi", 0, 11)); // NOI18N
        jLabel8.setText("Prepared by: _____________________________");
        PanelReport.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 600, 260, 30));

        jPanel1.add(PanelReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 500, 690));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/close.png"))); // NOI18N
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, -1, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked

        dispose();
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        PrintComponent print = new PrintComponent();

        print.printComponent(PanelReport);
    }//GEN-LAST:event_jPanel2MouseClicked

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
            java.util.logging.Logger.getLogger(DailySalesReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DailySalesReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DailySalesReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DailySalesReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DailySalesReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelReport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDate1;
    private javax.swing.JLabel lblItemCount;
    private javax.swing.JLabel lblReceiptNum;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JList<String> lstItems;
    // End of variables declaration//GEN-END:variables
}
