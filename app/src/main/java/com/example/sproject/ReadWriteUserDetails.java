package com.example.sproject;

public class ReadWriteUserDetails {

    public String name,phone ,uaddress;

    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String username, String mobilenumber, String address){
        this.name=username;
        this.phone=mobilenumber;
        this.uaddress=address;
    }
}
