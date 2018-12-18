package com.arkasian.util;

import com.arkasian.model.UserModel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

public class DefaultBundle extends ResourceBundle {
    private HashMap<String, Object> bundledObjects = new HashMap<>();

    @Override
    protected Object handleGetObject(String key) {
        return bundledObjects.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        Vector<String> v = new Vector<>(bundledObjects.keySet());
        return v.elements();
    }

    public void setUser(UserModel user){
        this.bundledObjects.put(user.getUsername(), user);
    }

    public UserModel getUser(String userName){
        return (UserModel) handleGetObject(userName);
    }
}
