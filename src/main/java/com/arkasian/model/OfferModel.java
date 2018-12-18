package com.arkasian.model;

public class OfferModel {
    private int oid;
    private String offerName;
    private float cost;
    private String location;
    private String localeType;

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocaleType() {
        return localeType;
    }

    public void setLocaleType(String locale_type) {
        this.localeType = locale_type;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }
}
