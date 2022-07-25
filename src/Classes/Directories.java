/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author padilla
 */



public class Directories {
    public static String linuxPath;
    public static String windowsPath;
    
    
    public enum OS{
    WINDOWS,LINUX;
    public String getPath(){
        
        switch(this){
            case WINDOWS:
                return windowsPath;
            case LINUX:
                return linuxPath;
            default:
                return windowsPath;
        }
    }
    }
    

}
