/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.awt.image.BufferedImage;
import Classes.Directories;
import java.io.File;

import javax.imageio.ImageIO;

/**
 *
 * @author padilla
 */
public class SavePhotoToFolder {
    
    public void setDefaultItemImage(){
        
        //linux
        String location = Directories.OS.LINUX.getPath()+"Maxilife/img/items/Default.jpg";
        
        //windows
        //String location = Directories.OS.WINDOWS.getPath()+"Maxilife/img/items/Default.jpg";
        
        
        //"C:\\Maxilife/img/items/Default.jpg"
        File dir = new File(location);
        if (!dir.exists()) {
            try {
                File file = new File(this.getClass().getClassLoader().getResource("img/med.jpg").toURI());
                BufferedImage image = ImageIO.read(file);
                ImageIO.write(image, "jpg", new File(location));
                System.out.println("Default photo Saved");
            } catch (Exception e) {
                System.out.println("Something went Wrong" + e);
            }

        } else {
            System.out.println("Default Image Available");
        }
 
        
      
    }
    
    public String saveToFolder(File file, String fileName, String ext){
         
        String location = "/home/padilla/Maxilife/img/items/"+fileName+"."+ext;
        try{
            BufferedImage image = ImageIO.read(file);
            ImageIO.write(image , ext, new File(location));
        }catch(Exception e){
            return "/home/padilla/Maxilife/img/items/Default.jpg";
        }
        return location;
 
     }
    
    public void deletePhoto(String path){
         File dir = new File(path);
         File defaultDir = new File("/home/padilla/Maxilife/img/items/Default.jpg");
       
        if (dir.equals(defaultDir)) {
            System.out.println("equals");
        } else {
            if (dir.delete()) {
                System.out.println("Photo Deleted");
            } else {
                System.out.println("Photo not exist");
            }

        }


    }
    public static void main(String[] args) {
       
    }

}

