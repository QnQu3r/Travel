package com.arkasian.model;

import java.util.Date;

public class StayModel {
    private int sid;
    private String stayName;
    private float cost;
    private Date fromWhen, toWhen;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getStayName() {
        return stayName;
    }

    public void setStayName(String stayName) {
        this.stayName = stayName;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Date getFromWhen() {
        return fromWhen;
    }

    public void setFromWhen(Date fromWhen) {
        this.fromWhen = fromWhen;
    }

    public Date getToWhen() {
        return toWhen;
    }

    public void setToWhen(Date toWhen) {
        this.toWhen = toWhen;
    }
}
