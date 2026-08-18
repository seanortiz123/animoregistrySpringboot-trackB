package com.dlsu.animoregistry.dto;

public class LoginRequest {

    private String dlsuEmail;
    private String password;

    public LoginRequest() {
    }

    public String getDlsuEmail() {
        return dlsuEmail;
    }

    public void setDlsuEmail(String dlsuEmail) {
        this.dlsuEmail = dlsuEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
