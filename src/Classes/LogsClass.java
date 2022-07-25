/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author padilla
 */
public class LogsClass {
    Connection conn = DbConnection.dbConnect();
    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:MM");
    LocalDateTime DateNow = LocalDateTime.now();
    
    public  void insertToLog(String username,String details){
        String sql = "insert into log(details, date, time, username) values(?,?,?,?)";
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, details);
            pst.setString(2, dtf1.format(DateNow));
            pst.setString(3, dtf2.format(DateNow));
             pst.setString(4, username);
             pst.executeUpdate();
             System.out.println("inserted");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    public ResultSet loadLogs(){
        String sql = "Select * from log order by id desc limit 5";
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
           return rs;
            
        }catch(Exception e){
            return null;
        }
    }
    
//    public static void main(String[] args) {
//        LogsClass l = new LogsClass();
//        l.insertToLog("Soybean", "Deleted an Item");
//    }
}
