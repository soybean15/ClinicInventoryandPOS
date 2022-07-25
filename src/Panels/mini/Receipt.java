/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panels.mini;

import Panels.Reports;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

/**
 *
 * @author padilla
 */
public class Receipt extends javax.swing.JPanel {

    /**
     * Creates new form Receipt
     * 
     */
    private List<String> data = new ArrayList<>();
    Reports report;
    public Receipt() {
     
       //
       // this.setLayout(new FlowLayout(FlowLayout.LEFT));
        initComponents();
       //this.setLayout(new GridLayout(1,data.size()));
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
       
       
       
    }
    public void setInstance(Reports report){
        this.report = report;
    }
    public void addDetails(String str){
        data.add(str);
    }
    
   public void loadLabels(){
//        JLabel[] lbl = new JLabel[data.size()];
//        int i =0;
//        int x =100;
//         for(String str : data){           
//             System.out.println(data);
//            lbl[i]= new JLabel(str);
//            lbl[i].setBounds(x, 5, 100, 30);
//             lbl[i].setFont(new Font("URW Gothic L", Font.PLAIN, 14));
//           // lbl[i].setBorder(new MatteBorder(0,1,0,0, Color.BLACK));
//            this.add(lbl[i]);
//            i++;
//            x+=100;
//        }
        lbl1.setText(data.get(0));
        lbl2.setText(data.get(1));
        lbl3.setText(data.get(2));
        lbl4.setText(data.get(3));
    }
   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl4 = new javax.swing.JLabel();
        lbl1 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 153, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setMaximumSize(new java.awt.Dimension(400, 25));
        setMinimumSize(new java.awt.Dimension(400, 25));
        setName(""); // NOI18N
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl4.setFont(new java.awt.Font("URW Gothic L", 0, 14)); // NOI18N
        lbl4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(lbl4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 140, 25));

        lbl1.setFont(new java.awt.Font("URW Gothic L", 0, 14)); // NOI18N
        lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(lbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 25));

        lbl2.setFont(new java.awt.Font("URW Gothic L", 0, 14)); // NOI18N
        lbl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(lbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 120, 25));

        lbl3.setFont(new java.awt.Font("URW Gothic L", 0, 14)); // NOI18N
        lbl3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(lbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 140, 25));
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        report.loadReceiptItem(data.get(0));
        report.loadTable();
        this.setBackground(Color.GRAY);
        lbl1.setForeground(Color.red);
        lbl2.setForeground(Color.red);
        lbl3.setForeground(Color.red);
        lbl4.setForeground(Color.red);
    }//GEN-LAST:event_formMouseClicked

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost

    }//GEN-LAST:event_formFocusLost

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
       
    }//GEN-LAST:event_formMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    // End of variables declaration//GEN-END:variables
}