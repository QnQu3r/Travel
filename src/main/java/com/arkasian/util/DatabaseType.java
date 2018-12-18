package com.arkasian.util;

public enum DatabaseType {
    USERS("users"),
    TRAVEL_AGENCY("travel_agency"),
    FLIGHT_AGENCY("flight_agency");

    private String dbName;

    DatabaseType(String dbName){
        this.dbName = dbName;
    }

    public String getDbName(){
        return this.dbName;
    }
}
