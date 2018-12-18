package com.arkasian.controller;

import com.arkasian.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AgentController implements Initializable {
    @FXML
    private MenuItem addStayItem, deleteStayItem, addOfferItem, deleteOfferItem;

    private DatabaseHandlerFactory databaseHandlerFactory = DatabaseHandlerFactory.getInstance(DatabaseType.TRAVEL_AGENCY);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addStayItem.setOnAction(action ->{
            showAddStayDialog();
        });
    }

    private void showAddStayDialog(){
        // Create the custom dialog.
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Agent - dodawanie turnusów");
        dialog.setHeaderText("Dodaj turnus");

// Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Nazwa");
        TextField costSpinner = new TextField();
        costSpinner.setEditable(true);
        DatePicker fromWhen = new DatePicker(), toWhen = new DatePicker();

        grid.add(new Label("Nazwa:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Koszt:"), 0, 1);
        grid.add(costSpinner, 1, 1);
        grid.add(new Label("Od kiedy: "), 0, 2);
        grid.add(fromWhen, 1,2);
        grid.add(new Label("Do kiedy"), 0, 3);
        grid.add(toWhen, 1,3);

// Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());



        Optional result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            ArrayList<String> params = new ArrayList<>();
            params.add("stayName");
            params.add("cost");
            params.add("toWhen");
            params.add("fromWhen");

            HashMap<String, Pair<String, Boolean>> values = new HashMap<>();
            values.put("fromWhen", new Pair<>(fromWhen.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yy")), false));
            values.put("toWhen", new Pair<>(toWhen.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yy")), false));
            values.put("cost", new Pair<>(costSpinner.getText(), false));
            values.put("stayName", new Pair<>(username.getText(), false));

            Query.QueryContent queryContent = new Query.QueryContent(params, values, false);
            Query query = new Query(queryContent, "stays", QueryType.INSERT);
            try {
                databaseHandlerFactory.createHandler().executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }
}
