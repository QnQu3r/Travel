package com.arkasian.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ViewState implements ObservableValue {
    private ViewStateListener viewStateListener;
    private String viewName;

    public ViewState(String viewName){
        this.viewName = viewName;
    }

    @Override
    public void addListener(ChangeListener listener) {
        this.viewStateListener = (ViewStateListener)listener;
    }

    @Override
    public void removeListener(ChangeListener listener) {
        this.viewStateListener = null;
    }

    @Override
    public Object getValue() {
        return this.viewName;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        /*Ignored*/
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        /*Ignored*/
    }
}
