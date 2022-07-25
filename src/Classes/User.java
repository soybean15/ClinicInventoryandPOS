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
    private String username;
    private String firstName;
    private String lastName;
    private String contact;
    private String email;
    private String role;
    private ImageIcon img;


    public User(int id, String firstName, String lastName, String role, String contact, String email, ImageIcon img,String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.contact = contact;
        this.img = img;
        this.username = username;
        
       //System.out.println(id+" "+firstName+" "+lastName+" "+role+" "+email+" "+contact);
    }
    
   
    public int getId() {
        return id;
    }
    public String getUserName(){
        return username;
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
