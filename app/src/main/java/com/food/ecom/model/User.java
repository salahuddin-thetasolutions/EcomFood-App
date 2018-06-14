package com.food.ecom.model;

/**
 * Created by ThetaTeam2 on 14/06/2018.
 */

public class User {
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
public  User(String Name,String Email,String Phone,String Address,String Token){
this.Name=Name;
    this.Email=Email;
    this.Phone=Phone;
    this.Address=Address;
    this.Token=Token;
}
    public  User(String Name,String Email,String Phone,String Address){
        this.Name=Name;
        this.Email=Email;
        this.Phone=Phone;
        this.Address=Address;
    }
public  User(){

}
    String Name;
    String Email;
    String Phone;
    String Address;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    String Token;
}
