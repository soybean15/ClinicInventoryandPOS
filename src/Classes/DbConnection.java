/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;


import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author padilla
 */
public class DbConnection {
    private static Connection conn = null;
    
    public static Connection dbConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Maxilife_db","root", "");
            
            
            System.out.println("Connected");
           
            return conn;
        }catch(Exception e){
            
            System.out.println("Noconnection"+e);
            return null;
        }
        
    }
    public static void main(String[] args) {
        dbConnect();
    }

}
