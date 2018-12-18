package com.arkasian.controller;

import com.arkasian.model.UserModel;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable, ViewStateListener {
    @FXML
    private VBox mainAnchor;
    @FXML
    private AnchorPane insideAnchor;
    @FXML
    private Label loggedInLabel;

    private UserModel user;
    private ViewState viewState = new ViewState("main");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(user == null){
            try {
                viewState.addListener(this);
                Parent parent = FXMLLoader.load(this.getClass().getResource("/login.fxml"));
                insideAnchor.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void viewStateChanged() {

    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {

    }
}
