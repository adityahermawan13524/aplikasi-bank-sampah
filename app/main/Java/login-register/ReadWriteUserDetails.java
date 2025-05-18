package com.dev.banksampahdigital.LoginRegister;

public class ReadWriteUserDetails {
    public String fullName, email, doB, gender, mobile;

    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String textFullName,String textEmail, String textDoB,String textGender, String textMobile){
        this.fullName = textFullName;
        this.email = textEmail;
        this.doB = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
    }
}
