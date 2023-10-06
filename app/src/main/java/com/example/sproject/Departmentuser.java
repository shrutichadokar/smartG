package com.example.sproject;

public class Departmentuser {

        String userName, id, email, password, phoneNumber,area,adharno,address;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

    public String getArea() {
        return area;
    }

    public String getAdharno() {
        return adharno;
    }

    public String getAddress() {
        return address;
    }

    public void setArea(String area) {this.area = area;}

    public void setAdharno(String adharno) {this.adharno = adharno;}

    public void setAddress(String address) {this.address = address;}

        public Departmentuser() {
        }

        public Departmentuser(String userName, String id, String email, String password, String phoneNumber ,String area,String adharno,String address) {
            this.userName = userName;
            this.id = id;
            this.area = area;
            this.adharno = adharno;
            this.address= address;
            this.email = email;
            this.password = password;
            this.phoneNumber = phoneNumber;

        }
    }


