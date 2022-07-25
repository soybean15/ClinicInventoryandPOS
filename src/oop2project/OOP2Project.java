/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package oop2project;

import Classes.CreateDirectory;
import Classes.SavePhotoToFolder;
import Classes.Directories;
import Frames.LoginFrame;

/**
 *
 * @author padilla
 */
public class OOP2Project {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //change your directories here for windows or linux OS
        Directories.linuxPath = "/home/padilla/";
        Directories.windowsPath = "C:\\";
        
        String linuxPath = Directories.OS.LINUX.getPath();
        String windowsPath = Directories.OS.WINDOWS.getPath();
        System.out.println(windowsPath);
        
        //create directory for linux      
        String item_path = linuxPath+"Maxilife/img/items"; 
         String xcel_path = linuxPath+"Maxilife/spreadsheets"; 
         
         //windows
         //String item_path = windowsPath+"Maxilife/img/items"; //create directory for windows
         // String xcel_path = windowsPath+"Maxilife/spreadsheets";
        
        
        CreateDirectory create = new CreateDirectory();
        create.createDIR(item_path);
        create.createDIR(xcel_path);
        
        SavePhotoToFolder spf = new SavePhotoToFolder();
        spf.setDefaultItemImage();
        
        LoginFrame login = new LoginFrame();
        // start login frame
        login.setVisible(true);
    }
    
}
