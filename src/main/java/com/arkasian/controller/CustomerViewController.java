package com.arkasian.controller;

import com.arkasian.model.OfferModel;
import com.arkasian.model.OffersToUsersModel;
import com.arkasian.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {
    @FXML
    private ListView<OfferModel> offersList, usersOffers;
    private DatabaseHandlerFactory offerdatabaseHandlerFactory = DatabaseHandlerFactory.getInstance(DatabaseType.TRAVEL_AGENCY);
    private DatabaseHandlerFactory userDatabaseHandlerFactory;
    private int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userId = ((DefaultBundle)resources).getUser("user").getUid();
        loadData();
    }

    private void loadData() {
        offersList.getItems().clear();
        usersOffers.getItems().clear();

        ArrayList<String> params = new ArrayList<>();
        params.add("*");

        Query.QueryContent queryContent = new Query.QueryContent(params, null, false);

        Query q = new Query(queryContent, "offers", QueryType.SELECT);
        try {
            OfferModel[] offers = (OfferModel[]) offerdatabaseHandlerFactory.createHandler().executeQuery(q, OfferModel.class, true);
            offersList.setItems(FXCollections.observableArrayList(offers));
            offersList.setCellFactory(list -> new OfferCell());

            ArrayList<String> params2 = new ArrayList<>();
            params2.add("*");

            HashMap<String, Pair<String, Boolean>> values2 = new HashMap<>();
            values2.put("uid", new Pair<>(String.valueOf(userId), true));
            Query.QueryContent queryContent2 = new Query.QueryContent(params2, values2, true);

            Query q2 = new Query(queryContent2, "offers_to_users", QueryType.SELECT);
            OffersToUsersModel[] userOffers = (OffersToUsersModel[]) offerdatabaseHandlerFactory.createHandler().executeQuery(q2, OffersToUsersModel.class, true);
            if(userOffers.length > 0) {
                for (int i = 0; i < offers.length; i++) {
                    OfferModel offer = offers[i];
                    if (offer != null && offer.getOid() == userOffers[i].getOid()) {
                        offers[i] = offer;
                    }
                }
                usersOffers.setItems(FXCollections.observableArrayList(offers));
                usersOffers.setCellFactory(list -> new BoughtOfferCell());
            }

        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    private class OfferCell extends ListCell<OfferModel> {
        @Override
        public void updateItem(OfferModel item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty && item != null) {
                this.setText(item.getOfferName());
                this.setOnMouseClicked(click -> {
                    if (click.getClickCount() == 2) {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setHeaderText("Oferta - informacje");
                        a.setContentText(item.getOfferName() + "\n" + item.getLocaleType() + "\n" + item.getLocation() + "\n" + item.getCost());
                        a.getButtonTypes().add(ButtonType.CANCEL);
                        Optional options = a.showAndWait();
                        options.ifPresent(present -> {
                            if (present == ButtonType.OK) {
                                ArrayList<String> params = new ArrayList<String>();
                                params.add("oid");
                                params.add("uid");
                                HashMap<String, Pair<String, Boolean>> values = new HashMap<>();
                                values.put("uid", new Pair<>(String.valueOf(item.getOid()), false));
                                values.put("oid", new Pair<>(String.valueOf(userId), false));
                                Query.QueryContent queryContent = new Query.QueryContent(params, values, false);
                                Query query = new Query(queryContent, "offers_to_users", QueryType.INSERT);
                                try {
                                    offerdatabaseHandlerFactory.createHandler().executeUpdate(query);
                                    loadData();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                a.close();
                            }
                        });
                    }
                });
            }
        }
    }

    private class BoughtOfferCell extends ListCell<OfferModel> {
        @Override
        public void updateItem(OfferModel item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty && item != null) {
                this.setText(item.getOfferName());
                this.setOnMouseClicked(click -> {
                    if (click.getClickCount() == 2) {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setHeaderText("Oferta - informacje");
                        a.setContentText("Usuwanie oferty:\n"+item.getOfferName() + "\n" + item.getLocaleType() + "\n" + item.getLocation() + "\n" + item.getCost());
                        a.getButtonTypes().add(ButtonType.CANCEL);
                        Optional options = a.showAndWait();
                        options.ifPresent(present -> {
                            if (present == ButtonType.OK) {
                                ArrayList<String> params = new ArrayList<>();
                                HashMap<String, Pair<String, Boolean>> values = new HashMap<>();
                                values.put("uid", new Pair<>(String.valueOf(userId), false));
                                Query.QueryContent queryContent = new Query.QueryContent(params, values, true);
                                Query query = new Query(queryContent, "offers_to_users", QueryType.DELETE);
                                try {
                                    offerdatabaseHandlerFactory.createHandler().executeUpdate(query);
                                    loadData();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                a.close();
                            }
                        });
                    }
                });
            }
        }
    }
}
