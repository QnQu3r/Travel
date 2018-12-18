package com.arkasian.util;

public class DatabaseHandlerFactory {
    private static DatabaseHandlerFactory ourInstance = new DatabaseHandlerFactory();
    private static DatabaseType databaseType = DatabaseType.USERS;
    private String[] connectionStrings = {"jdbc:mysql://127.0.0.1:3306/users?user=travel_user&password=travelpassword",
                                          "jdbc:mysql://127.0.0.1:3306/travel_agency?user=travel_user&password=travelpassword",
                                          "jdbc:mysql://127.0.0.1:3306/flight_agency?user=travel_user&password=travelpassword"};

    public static DatabaseHandlerFactory getInstance(DatabaseType type) {
        databaseType = type;
        return ourInstance;
    }

    private DatabaseHandlerFactory() { }

    public DatabaseHandler createHandler(){
        switch(databaseType){
            case USERS:
                return new DatabaseHandler(connectionStrings[0], databaseType);
            case FLIGHT_AGENCY:
                return new DatabaseHandler(connectionStrings[2], databaseType);
            case TRAVEL_AGENCY:
                return new DatabaseHandler(connectionStrings[1], databaseType);
            default:
                return new DatabaseHandler(connectionStrings[0], DatabaseType.USERS);
        }
    }
}
