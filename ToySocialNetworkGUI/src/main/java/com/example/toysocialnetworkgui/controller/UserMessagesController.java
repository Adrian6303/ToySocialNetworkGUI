package com.example.toysocialnetworkgui.controller;

import com.example.toysocialnetworkgui.domain.Cerere;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.service.ServiceCerere;
import com.example.toysocialnetworkgui.service.ServiceMessage;
import com.example.toysocialnetworkgui.service.ServicePrietenie;
import com.example.toysocialnetworkgui.service.ServiceUtilizator;
import com.example.toysocialnetworkgui.utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class UserMessagesController implements Observer {

    public ListView<Message> messagesListView;
    ObservableList<Message> model = FXCollections.observableArrayList();

    private ServiceUtilizator serviceUser;
    private ServiceMessage serviceMessage;
    private Utilizator user1;
    private Utilizator user2;

    public ComboBox<Utilizator> userFromComboBox;
    public ComboBox<Utilizator> userToComboBox;


    public void setServiceMessages(ServiceMessage serviceMessage, ServiceUtilizator serviceUser) {
        this.serviceMessage = serviceMessage;
        this.serviceUser = serviceUser;
        serviceMessage.addObserver(this);
        initData();
    }

    public void initialize() {
        messagesListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Message> call(ListView param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(Message message, boolean empty) {
                        super.updateItem(message, empty);
                        if (empty || message == null) {
                            setText(null);
                        } else {
                            Long userID = message.getId_from();
                            Utilizator utilizator = serviceUser.findOne(userID);
                            setText(utilizator.getFirstName() + " " + utilizator.getLastName() + ": " + "\n" + message.getMessage());
                        }
                    }
                };
            }
        });
        messagesListView.setItems(model);
    }

    public void initData()
    {
        userFromComboBox.setPromptText("Primul user");
        ObservableList<Utilizator> lista1 = FXCollections.observableArrayList();
        lista1.addAll(StreamSupport.stream(serviceUser.findAll().spliterator(), false).toList());
        userFromComboBox.getItems().addAll(lista1);

        userToComboBox.setPromptText("Al doilea user");
        ObservableList<Utilizator> lista2 = FXCollections.observableArrayList();
        lista2.addAll(StreamSupport.stream(serviceUser.findAll().spliterator(), false).toList());
        userToComboBox.getItems().addAll(lista2);
    }

    @Override
    public void update() {
        initData();
        messagesListView.setItems(model);
    }

    public void accessButtonHandler() throws IOException {
        user1 = userFromComboBox.getSelectionModel().getSelectedItem();
        user2 = userToComboBox.getSelectionModel().getSelectedItem();
        messagesListView.setItems(
                FXCollections.observableArrayList(
                        new ArrayList<>(StreamSupport.stream(serviceMessage.filtrare(user1.getId(),user2.getId()).spliterator(), false).toList())
        ));
    }
}
