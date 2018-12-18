package com.arkasian.model;

public class UserModel {
    private int uid;
    private String username;
    private String passwordHash;
    private String mail;
    private UserType userType;

    public UserModel(){}

    public UserModel(String username, String passwordHash, String mail, UserType userType) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.mail = mail;
        this.userType = userType;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
