/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import javax.swing.ImageIcon;
/**
 *
 * @author padilla
 */
public class User {
    int id;
    String firstName;
    String lastName;
    String contact;
    String email;
    String role;
    ImageIcon img;


    public User(int id, String firstName, String lastName, String role, String contact, String email, ImageIcon img) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.contact = contact;
        this.img = img;
        
       //System.out.println(id+" "+firstName+" "+lastName+" "+role+" "+email+" "+contact);
    }
    
   
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public ImageIcon getImg() {
        return img;
    }
    
    
    
}
