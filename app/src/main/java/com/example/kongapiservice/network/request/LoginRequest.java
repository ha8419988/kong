package com.example.kongapiservice.network.request;

public class LoginRequest {
    String email;
    String password;

    public LoginRequest(String mail, String pass) {
        this.email = mail;
        this.password = pass;
    }


    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }

    public String getPass() {
        return password;
    }

    public void setPass(String pass) {
        this.password = pass;
    }
}
