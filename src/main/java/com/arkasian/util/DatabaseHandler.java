package com.arkasian.util;

import com.arkasian.model.UserType;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

public class DatabaseHandler {
    private Connection connection;
    private DatabaseType databaseType;

    DatabaseHandler(String connection, DatabaseType databaseType){
        try {
            this.connection = DriverManager.getConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.databaseType = databaseType;
    }

    public Connection getConnection(){
        return this.connection;
    }

    public Object executeQuery(Query query, Class retTypeClazz, boolean toArray) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query.toString());
        Field[] fields = retTypeClazz.getDeclaredFields();
        String[] fieldNames = new String[fields.length];

        for(int i = 0; i < fields.length; i++){
            fieldNames[i] = fields[i].getName();
        }

        Object retObj = null;

        if(!toArray) {
            Object userModel = retTypeClazz.getConstructor(new Class<?>[]{}).newInstance();
            while (resultSet.next()) {
                int fieldNameIndex = 0;
                invokeSettersOf(resultSet, fieldNames, userModel, fieldNameIndex);
            }
            retObj = userModel;
        }else{
            Object userModel = retTypeClazz.getConstructor(new Class<?>[]{}).newInstance();
            int count = getRowCount(query.getDatabaseName());
            retObj = Array.newInstance(retTypeClazz, count);
            int overallIndex = 0;
            while (resultSet.next()) {
                int fieldNameIndex = 0;
                invokeSettersOf(resultSet, fieldNames, userModel, fieldNameIndex);
                Array.set(retObj, overallIndex, userModel);
                overallIndex++;
            }
        }
        return retObj;
    }

    private int invokeSettersOf(ResultSet resultSet, String[] fieldNames, Object userModel, int fieldNameIndex) throws NoSuchMethodException, NoSuchFieldException, SQLException, IllegalAccessException, InvocationTargetException {
        for (; fieldNameIndex < fieldNames.length; fieldNameIndex++) {
            String methodName = "set" + fieldNames[fieldNameIndex].substring(0, 1).toUpperCase() + fieldNames[fieldNameIndex].substring(1);
            Method m = userModel.getClass().getMethod(methodName, userModel.getClass().getDeclaredField(fieldNames[fieldNameIndex]).getType());
            Object dataObject = resultSet.getObject(fieldNames[fieldNameIndex]);
            Object castObject = dataObject;

            if (userModel.getClass().getDeclaredField(fieldNames[fieldNameIndex]).getType().isEnum()) {
                castObject = convertToEnumType(dataObject);
            }
            m.invoke(userModel, castObject);

        }
        return fieldNameIndex;
    }

    private Object convertToEnumType(Object dataObject) {
        switch (databaseType){
            case USERS:
                return UserType.valueOf(dataObject.toString());
            case FLIGHT_AGENCY:
                return new Object();
            case TRAVEL_AGENCY:
                return new Object();
        }
        return null;
    }

    public void executeUpdate(Query update) throws SQLException {
        Statement stmt = this.connection.createStatement();
        stmt.executeUpdate(update.toString());
    }

    private int getRowCount(String name) throws SQLException {
        ResultSet resultSet = this.connection.createStatement().executeQuery("select count(*) from "+name);
        resultSet.next();
        return resultSet.getInt(1);
    }
}
