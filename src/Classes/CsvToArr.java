/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.List;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author padilla
 */
public class CsvToArr {
    Connection conn= null;
     File file ;
    
    static void print(Object[][] arr){
        for(int i = 0 ; i<arr.length; i++){
            for(int j = 0 ; j<arr[i].length; j++){
                System.out.print(arr[i][j]+" ");
            }
            System.out.println("");
        }
    }
    
    void start(){
        conn = DbConnection.dbConnect();
        
    }
    
    void insert(String producID,String itemName, String cat, float itemPrice,int itemStock){
        String sql1 = "INSERT INTO tbl_items(item_code,item_name, item_category, item_image) VALUES(?,?,?,?)";
        String sql2 = "INSERT INTO tbl_itemstock(item_code, item_price, item_stock) VALUES(?,?,?);";
        
        try {
             file = new File(this.getClass().getClassLoader().getResource("img/med.jpg").toURI());
            InputStream is = new FileInputStream(file);

            PreparedStatement pst = conn.prepareStatement(sql1);
            pst.setString(1, producID);
            pst.setString(2, itemName);
            pst.setString(3, cat);
           
            pst.setBlob(4, is);
            pst.executeUpdate();

            pst = conn.prepareStatement(sql2);
            pst.setString(1, producID);
            pst.setFloat(2, itemPrice);
            pst.setInt(3, itemStock);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Inserted");

        } catch (Exception e) {
            System.out.println("Error inserting " + e);
        }
    }
    
    
     void changeProductCode(String[][] arr){
        for(int i =1; i<arr.length; i++){
            int hc = arr[i][1].hashCode();
            String hashStr =String.valueOf(hc).substring(String.valueOf(hc).length()-5, String.valueOf(hc).length());
           // System.out.println(hc+" str:" + hashStr);
            
            String prodIdpart1="";
            if(!arr[i][2].equals("MEDICINE SUPPLY")){
                prodIdpart1 = "MS1";
            }else{
                 prodIdpart1 = "MS2";
            }
            String prodId=prodIdpart1 +"-"+hashStr;
            insert( prodId,arr[i][1], arr[i][2], Float.parseFloat(arr[i][3]),Integer.parseInt(arr[i][4]));
            
        }
    }
    public static void main(String[] args) {
       CsvToArr csv = new CsvToArr();
       csv.start();
      // String path = "/home/padilla/Videos/inventory.csv";
       
       
       String path = "yourFile.csv";
       String thisLine;
       try{
           FileInputStream fs = new FileInputStream(path);
           DataInputStream input = new DataInputStream(fs);
           
           //create 2d ArrayList
           List<String[]> lines = new ArrayList<>();
           
           while((thisLine = input.readLine())!= null)
               lines.add(thisLine.split(","));
           
           
           //convert to 2d Object array
           Object[][] arr = new Object[lines.size()][4];    
           lines.toArray(arr);
           
          print(arr);
           //csv.changeProductCode(arr);
       }catch(IOException e){
           System.out.println("Error"+ e);
       }
       
    }
}
