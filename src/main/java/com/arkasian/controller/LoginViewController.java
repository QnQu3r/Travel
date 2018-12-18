package com.arkasian.controller;

import com.arkasian.model.UserModel;
import com.arkasian.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {
    @FXML
    private ButtonBar bottomBar;
    @FXML
    private TextField loginText;
    @FXML
    private PasswordField passwordField;
    @FXML
    private BorderPane loginAnchor;

    private DatabaseHandlerFactory databaseHandlerFactory = DatabaseHandlerFactory.getInstance(DatabaseType.USERS);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button okButton = (Button)bottomBar.getButtons().get(0);
        DatabaseHandler databaseHandler = databaseHandlerFactory.createHandler();
        okButton.setOnAction(action ->{
           if(loginText.getText() != null && passwordField.getText() != null && !loginText.getText().isEmpty() && !passwordField.getText().isEmpty()){
               ArrayList<String> params = new ArrayList<>();
               params.add("*");
               HashMap<String, Pair<String, Boolean>> values = new HashMap<>();
               values.put("username", new Pair<>(loginText.getText(), true));
               Query.QueryContent queryContent = new Query.QueryContent(params, values, true);
               Query query = new Query(queryContent, "users", QueryType.SELECT);

               try {
                   UserModel o = (UserModel)databaseHandler.executeQuery(query, UserModel.class, false);
                   if(o.getUsername().equals(loginText.getText()) && passwordField.getText().equals(o.getPasswordHash())){
                       DefaultBundle defaultUserBundle = new DefaultBundle();
                        defaultUserBundle.setUser(o);
                       Parent parent = FXMLLoader.load(this.getClass().getResource("/"+o.getUserType().toString().toLowerCase()+"_part.fxml"), defaultUserBundle);
                       AnchorPane mainAnchor = ((AnchorPane)loginAnchor.getParent());
                       AnchorPane.setBottomAnchor(parent, 0d);
                       AnchorPane.setTopAnchor(parent, 0d);
                       AnchorPane.setLeftAnchor(parent, 0d);
                       AnchorPane.setRightAnchor(parent, 0d);
                       ((TabPane)parent).setPrefWidth(mainAnchor.getWidth());
                       mainAnchor.getChildren().clear();
                       mainAnchor.getChildren().add(parent);
                   }else{
                       Alert a = new Alert(Alert.AlertType.ERROR);
                       a.setHeaderText("Błąd logowania");
                       a.setContentText("Błędne dane logowania.");
                       a.show();
                   }
               } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException | IOException e) {
                   e.printStackTrace();
               }
           }else{
               Alert a = new Alert(Alert.AlertType.ERROR);
               a.setHeaderText("Błąd logowania.");
               a.setContentText("Żadne z pól nie może być puste.");
               a.show();
           }
        });
    }
}
