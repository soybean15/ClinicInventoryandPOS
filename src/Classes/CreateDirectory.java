/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.io.File;

/**
 *
 * @author padilla
 */

public class CreateDirectory {
    
    public void createDIR(String path){
        
        File dir = new File(path);
        if(!dir.isDirectory()){
            dir.mkdirs();
            
            System.out.println("Directory Created");
        }else{
            System.out.println("Directory Available");    
        }
        
    }
    
    
    public void addDefaultImage(){
        
    }
    

    
}
